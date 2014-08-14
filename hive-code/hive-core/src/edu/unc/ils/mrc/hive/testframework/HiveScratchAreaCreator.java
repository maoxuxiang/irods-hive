/**
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.testutils.TestingUtilsException;

import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * Service to create a test scratch hive for a chosen vocab in a fresh area
 * 
 * @author Mike Conway (DICE)
 * 
 */
public class HiveScratchAreaCreator {

	private final Properties testingProperties;
	private static final Log log = LogFactory
			.getLog(HiveScratchAreaCreator.class);

	/**
	 * Initialize with a set of testing properties
	 * 
	 * @param testingProperties
	 */
	public HiveScratchAreaCreator(final Properties testingProperties) {
		if (testingProperties == null || testingProperties.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty testingProperties");
		}
		this.testingProperties = testingProperties;
	}

	/**
	 * Get the path to the root that is the source of hive vocab data used to
	 * create tests, this will also verify that it exists
	 * 
	 * @return <code>String</code> with the absolute path to the source of the
	 *         hive data
	 * @throws TestingUtilsException
	 */
	public String getSourceRootForHiveVocabs() throws TestingUtilsException {
		log.info("getSourceRootForHiveVocabs()");

		String sourceRootPath = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_SOURCE_DIR);

		if (sourceRootPath == null || sourceRootPath.isEmpty()) {
			throw new IllegalArgumentException("null or empty sourceRootPath");
		}

		File hiveSourceRoot = new File(sourceRootPath);

		if (!hiveSourceRoot.exists()) {
			throw new TestingUtilsException("source root not found");
		}

		return sourceRootPath;

	}

	/**
	 * Method will just return the path it thinks is where this test subdir
	 * would be. It does not setup or checking
	 * 
	 * @param testSubdir
	 *            <code>String</code> with a test subdir name under scratch
	 * @return a full path to the test subdir
	 * @throws TestingUtilsException
	 */
	public String getPathToScratch(final String testSubdir)
			throws TestingUtilsException {
		if (testSubdir == null || testSubdir.isEmpty()) {
			throw new IllegalArgumentException("null or empty testSubdir");
		}

		String scratchSubdir = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_SCRATCH_DIR);

		if (scratchSubdir == null || scratchSubdir.isEmpty()) {
			throw new TestingUtilsException(
					"null or empty scratch dir, check if property is set for test.hive.scratch.dir");
		}

		File scratchParent = new File(scratchSubdir);

		File scratchChild = new File(scratchParent, scratchSubdir);
		return scratchChild.getAbsolutePath();

	}

	/**
	 * Give a test subdir under the configured scratch directory and it will
	 * create a scratch area for that test
	 * <p/>
	 * This will return the root of the parent of the vocabulary, there will be
	 * a subdirectory under this with the vocab name
	 * 
	 * @param testSubdir
	 * @throws TestingUtilsException
	 */
	public String clearAndInitializeScratchAreaWithTestVocab(
			final String testSubdir, final String vocabName)
			throws TestingUtilsException {

		log.info("clearAndInitializeScratchAreaWithTestVocab()");

		if (testSubdir == null || testSubdir.isEmpty()) {
			throw new IllegalArgumentException("null or empty testSubdir");
		}

		if (vocabName == null || vocabName.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabName");
		}

		log.info("testSubdir:" + testSubdir);
		log.info("vocabName:" + vocabName);

		String scratchSubdir = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_SCRATCH_DIR);

		if (scratchSubdir == null || scratchSubdir.isEmpty()) {
			throw new TestingUtilsException(
					"null or empty scratch dir, check if property is set for test.hive.scratch.dir");
		}

		File scratchParent = new File(scratchSubdir);
		scratchParent.delete();
		scratchParent.mkdirs();

		File scratchChild = new File(scratchParent, testSubdir);
		scratchChild.mkdirs();

		log.info("set up scratch child as:" + scratchChild);

		String hiveSourceRoot = getSourceRootForHiveVocabs();

		log.info("source is:" + hiveSourceRoot);

		provisionVocabProperties(vocabName, scratchChild.getAbsolutePath());

		return scratchChild.getAbsolutePath();

	}

	/**
	 * Give a test subdir under the configured scratch directory and it will
	 * create a scratch directory with that name and return the full path
	 * 
	 * @param testSubdir
	 * @throws TestingUtilsException
	 */
	public String clearAndInitializeScratchArea(final String testSubdir)
			throws TestingUtilsException {

		log.info("clearAndInitializeScratchAreaWithTestVocab()");

		if (testSubdir == null || testSubdir.isEmpty()) {
			throw new IllegalArgumentException("null or empty testSubdir");
		}

		log.info("testSubdir:" + testSubdir);

		String scratchSubdir = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_SCRATCH_DIR);

		if (scratchSubdir == null || scratchSubdir.isEmpty()) {
			throw new TestingUtilsException(
					"null or empty scratch dir, check if property is set for test.hive.scratch.dir");
		}

		File scratchParent = new File(scratchSubdir);
		scratchParent.delete();
		scratchParent.mkdirs();

		File scratchChild = new File(scratchParent, testSubdir);
		scratchChild.mkdirs();

		return scratchChild.getAbsolutePath();

	}

	/**
	 * Given a vocabulary that exists in the configured source, and a target dir
	 * (a relative subdir under scratch), provision the vocabulary file and hive
	 * properties
	 * <p/>
	 * Note that this doesn't do any hive imports, rather it builds a bare
	 * directory to do hive things to, with a specified vocab .rdf file and a
	 * .properties file in the right place
	 * 
	 * @param vocabName
	 * @param targetDir
	 *            absolute path to the parent scratch subdirectory for the
	 *            particular test (hive scratch plus testing subdir)
	 * @throws TestingUtilsException
	 */
	public void provisionVocabProperties(final String vocabName,
			final String targetDir) throws TestingUtilsException {

		if (vocabName == null || vocabName.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabName");
		}

		if (targetDir == null || targetDir.isEmpty()) {
			throw new IllegalArgumentException("null or empty targetDir");
		}

		log.info("targetDir:" + targetDir);
		log.info("vocabName:" + vocabName);

		String sourceRoot = getSourceRootForHiveVocabs();

		log.info("create hive.properties for vocab");
		File hiveProps = new File(targetDir, "hive.properties");

		PrintWriter hivePropOut;
		try {
			hivePropOut = new PrintWriter(hiveProps);
		} catch (FileNotFoundException e1) {
			throw new TestingUtilsException(
					"unable to create hive.properties in test target");
		}
		hivePropOut.println("hive.vocabulary =" + vocabName);
		hivePropOut.println("hive.tagger = dummy");
		hivePropOut.flush();
		hivePropOut.close();

		log.info("copy the props over to the target dir");

		File targetVocabProps = new File(targetDir, vocabName + ".properties");

		Properties props = new Properties();
		props.setProperty("resource.loader", "file");
		props.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		props.setProperty("file.resource.loader.path", sourceRoot);
		VelocityEngine ve = new VelocityEngine(props);

		ve.init();
		/* next, get the Template */
		Template t = ve.getTemplate(vocabName + ".properties");
		/* create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put("hiveRoot", targetDir.replace("\\", "/"));
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		log.info("writer:" + writer.toString());

		try {
			PrintWriter out = new PrintWriter(targetVocabProps);
			out.print(writer);
			out.close();
		} catch (FileNotFoundException e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}

		log.info("copying vocab subdir...");

		File sourceVocab = new File(sourceRoot, vocabName);
		File targetVocab = new File(targetDir, vocabName);

		try {
			FileUtils.copyDirectory(sourceVocab, targetVocab);
		} catch (IOException e) {
			throw new JargonRuntimeException("unable to copy hive props");
		}

	}
}
