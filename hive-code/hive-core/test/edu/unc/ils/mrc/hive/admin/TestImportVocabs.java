package edu.unc.ils.mrc.hive.admin;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.SKOSServer;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSServerImpl;
import edu.unc.ils.mrc.hive.testframework.HiveScratchAreaCreator;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class TestImportVocabs {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;
	private static SKOSServer skosServer;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();
		if (testingPropertiesHelper.checkTestHiveFuntionalSetup() == false) {
			throw new Exception(
					"test hive not set up, run HiveTestInstanceSetup");
		}

		hiveScratchAreaCreator = new HiveScratchAreaCreator(testingProperties);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		skosServer.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadMesh() throws Exception {

		String testSubdir = "testLoadMesh";
		String vocabName = "mesh";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "mesh", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		skosServer = new SKOSServerImpl(parentOfTest + "/hive.properties");

		Assert.assertNotNull("null skosServer", skosServer);

		skosServer.getSKOSSearcher();

		TreeMap<String, SKOSScheme> vocabularies = skosServer.getSKOSSchemas();
		Set<String> keys = vocabularies.keySet();
		Iterator<String> it = keys.iterator();
		boolean gotVocab = false;
		long nbrConcepts = 0;
		while (it.hasNext()) {
			SKOSScheme voc = vocabularies.get(it.next());
			if (voc.getName().equals("mesh")) {
				gotVocab = true;
			}
			System.out.println("NAME: " + voc.getName());
			System.out.println("\t LONG NAME: " + voc.getLongName());
			System.out.println("\t NUMBER OF CONCEPTS: "
					+ voc.getNumberOfConcepts());

			nbrConcepts = voc.getNumberOfConcepts();

			System.out.println("\t NUMBER OF RELATIONS: "
					+ voc.getNumberOfRelations());
			System.out.println("\t DATE: " + voc.getLastDate());
			System.out.println();
			System.out.println("\t SIZE: " + voc.getSubAlphaIndex("a").size());
			System.out.println();

		}

		skosServer.close();
		Assert.assertTrue("did not find just loaded vocab", gotVocab);
		Assert.assertTrue("did not find any concepts", nbrConcepts > 0);

	}

	@Test
	public void testLoadAgrovoc() throws Exception {

		String testSubdir = "testLoadAgrovoc";
		String vocabName = "agrovoc";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "agrovoc", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		skosServer = new SKOSServerImpl(parentOfTest + "/hive.properties");

		Assert.assertNotNull("null skosServer", skosServer);

		skosServer.getSKOSSearcher();

		/**
		 * Statistics test
		 */

		TreeMap<String, SKOSScheme> vocabularies = skosServer.getSKOSSchemas();
		Set<String> keys = vocabularies.keySet();
		Iterator<String> it = keys.iterator();
		boolean gotVocab = false;
		long nbrConcepts = 0;
		while (it.hasNext()) {
			SKOSScheme voc = vocabularies.get(it.next());
			if (voc.getName().equals("AGROVOC")) {
				gotVocab = true;
			}
			System.out.println("NAME: " + voc.getName());
			System.out.println("\t LONG NAME: " + voc.getLongName());
			System.out.println("\t NUMBER OF CONCEPTS: "
					+ voc.getNumberOfConcepts());

			nbrConcepts = voc.getNumberOfConcepts();

			System.out.println("\t NUMBER OF RELATIONS: "
					+ voc.getNumberOfRelations());
			System.out.println("\t DATE: " + voc.getLastDate());
			System.out.println();
			System.out.println("\t SIZE: " + voc.getSubAlphaIndex("a").size());
			System.out.println();

		}

		skosServer.close();
		Assert.assertTrue("did not find just loaded vocab", gotVocab);
		Assert.assertTrue("did not find any concepts", nbrConcepts > 0);

	}

	/**
	 * Test method for
	 * {@link edu.unc.ils.mrc.hive.admin.TaggerTrainer#trainKEAAutomaticIndexingModule()}
	 * .
	 */
	@Test
	public void testLoadUat() throws Exception {

		String testSubdir = "testLoadUat";
		String vocabName = "uat";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "uat", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		skosServer = new SKOSServerImpl(parentOfTest + "/hive.properties");

		Assert.assertNotNull("null skosServer", skosServer);

		skosServer.getSKOSSearcher();

		/**
		 * Statistics test
		 */

		TreeMap<String, SKOSScheme> vocabularies = skosServer.getSKOSSchemas();
		Set<String> keys = vocabularies.keySet();
		Iterator<String> it = keys.iterator();
		boolean gotVocab = false;
		long nbrConcepts = 0;
		while (it.hasNext()) {
			SKOSScheme voc = vocabularies.get(it.next());
			if (voc.getName().equals("uat")) {
				gotVocab = true;
			}
			System.out.println("NAME: " + voc.getName());
			System.out.println("\t LONG NAME: " + voc.getLongName());
			System.out.println("\t NUMBER OF CONCEPTS: "
					+ voc.getNumberOfConcepts());

			nbrConcepts = voc.getNumberOfConcepts();

			System.out.println("\t NUMBER OF RELATIONS: "
					+ voc.getNumberOfRelations());
			System.out.println("\t DATE: " + voc.getLastDate());
			System.out.println();
			System.out.println("\t SIZE: " + voc.getSubAlphaIndex("a").size());
			System.out.println();
			// System.out.println("\t TOP CONCEPTS: " +
			// voc.getNumberOfTopConcepts());
		}

		skosServer.close();
		Assert.assertTrue("did not find just loaded vocab", gotVocab);
		Assert.assertTrue("did not find any concepts", nbrConcepts > 0);

	}

}
