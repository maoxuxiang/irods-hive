/*
 * 
 */
package edu.unc.ils.mrc.hive.testframework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.testutils.TestingUtilsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliasi.util.Files;

import edu.unc.ils.mrc.hive.HiveException;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * Setup driver for building the HIVE used for unit and functional testing
 * 
 * @author Mike Conway
 * 
 */

public class HiveTestInstanceSetup {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();

	private File hiveSourceRoot;
	private File hiveTargetRoot;

	static Logger log = LoggerFactory.getLogger(HiveTestInstanceSetup.class);

	public void init() throws JargonException, TestingUtilsException {
		log.info("init()");

		if (!hiveSourceRoot.exists()) {
			throw new IllegalStateException("cannot find hive source root");
		}

		if (hiveTargetRoot.exists()) {
			log.info("target root exists...delete it all");
			try {
				FileUtils.deleteDirectory(hiveTargetRoot);
			} catch (IOException e) {
				throw new JargonRuntimeException("unable to delete target dir");

			}
		}

		log.info("mkdirs for target root...");
		hiveTargetRoot.mkdirs();

		log.info("source and target dirs set, now move and configure the target properties");

		copyHiveProperties();

		provisionVocabProperties("agrovoc");
		provisionVocabProperties("uat");
		provisionVocabProperties("mesh");

	}

	private void initializeSourceAndTargetRoots() throws TestingUtilsException {

		testingProperties = testingPropertiesHelper.getTestProperties();
		log.info("see if the source hive stuff is there");

		hiveSourceRoot = new File(
				testingProperties
						.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_SOURCE_DIR));

		log.info("source root:{}", hiveSourceRoot);

		hiveTargetRoot = new File(
				testingProperties
						.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR));

		log.info("target root:{}", hiveTargetRoot);

	}

	private void provisionVocabProperties(final String vocabName) {

		log.info("provisionVocabProperties");
		log.info("vocab:{}", vocabName);
		new File(hiveSourceRoot, "/" + vocabName + ".properties");

		log.info("copy the props over to the target dir");

		File targetVocabProps = new File(hiveTargetRoot, vocabName
				+ ".properties");

		Properties props = new Properties();
		props.setProperty("resource.loader", "file");
		props.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		props.setProperty("file.resource.loader.path",
				hiveSourceRoot.getAbsolutePath());
		VelocityEngine ve = new VelocityEngine(props);

		ve.init();
		/* next, get the Template */
		Template t = ve.getTemplate(vocabName + ".properties");
		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("hiveRoot", testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR));
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		log.info("writer:{}", writer.toString());

		try {
			PrintWriter out = new PrintWriter(targetVocabProps);
			out.print(writer);
			out.close();
		} catch (FileNotFoundException e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}

		log.info("copying vocab subdir...");

		File sourceVocab = new File(hiveSourceRoot, vocabName);
		File targetVocab = new File(hiveTargetRoot, vocabName);

		try {
			FileUtils.copyDirectory(sourceVocab, targetVocab);
		} catch (IOException e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}

		log.info("do hive import....");

		SKOSScheme schema;
		try {
			schema = new SKOSSchemeImpl(
					testingProperties
							.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR),
					vocabName, true);
			schema.importConcepts(schema.getRdfPath(), true, true, true, false,
					true);

			log.info("imported");

		} catch (HiveException e) {
			throw new JargonRuntimeException("unable to copy hive props");

		} catch (Exception e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}
	}

	/**
	 * Look to see if the configured testing dir where a set of generic vocabs
	 * was loaded configured? This can be used to tell whether they need to be
	 * initialized by calling init();
	 * 
	 * @return
	 */
	public boolean isFunctionalTestingDirConfigured() {
		log.info("isFunctionalTestingDirConfigured");
		return hiveTargetRoot.exists();
	}

	private void copyHiveProperties() {

		log.info("copyHiveProperties()");
		String hivePropsName = "hive.properties";

		File sourceHiveProps = new File(hiveSourceRoot, hivePropsName);
		File targetHiveProps = new File(hiveTargetRoot, hivePropsName);
		try {
			Files.copyFile(sourceHiveProps, targetHiveProps);
		} catch (IOException e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}

		log.info("copied");
	}

	public HiveTestInstanceSetup() throws TestingUtilsException {
		initializeSourceAndTargetRoots();
	}

	public static void main(final String args[]) {
		HiveTestInstanceSetup setup;
		try {
			setup = new HiveTestInstanceSetup();
		} catch (TestingUtilsException e) {
			throw new JargonRuntimeException("unable to set up test dir", e);
		}
		try {
			setup.init();
		} catch (JargonException e) {
			throw new JargonRuntimeException("unable to set up test dir", e);
		} catch (TestingUtilsException e) {
			throw new JargonRuntimeException("error seting up test properties",
					e);
		}
	}
}
