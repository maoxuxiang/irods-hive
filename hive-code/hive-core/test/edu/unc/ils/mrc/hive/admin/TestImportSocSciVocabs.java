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

public class TestImportSocSciVocabs {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();

		hiveScratchAreaCreator = new HiveScratchAreaCreator(testingProperties);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {

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
	public void testLoadSocialScienceOrg() throws Exception {

		String testSubdir = "testLoadSocialScienceOrg";
		String vocabName = "social_science_org";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, vocabName, true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		SKOSServer skosServer = new SKOSServerImpl(parentOfTest
				+ "/hive.properties");

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
			if (voc.getName().equals(vocabName)) {
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

	@Test
	public void testLoadSocialSciencePerson() throws Exception {

		String testSubdir = "testLoadSocialSciencePerson";
		String vocabName = "social_science_person";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, vocabName, true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		SKOSServer skosServer = new SKOSServerImpl(parentOfTest
				+ "/hive.properties");

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
			if (voc.getName().equals(vocabName)) {
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

	@Test
	public void testLoadSocialSciencePlace() throws Exception {

		String testSubdir = "testLoadSocialSciencePlace";
		String vocabName = "social_science_place";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, vocabName, true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		SKOSServer skosServer = new SKOSServerImpl(parentOfTest
				+ "/hive.properties");

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
			if (voc.getName().equals(vocabName)) {
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

	@Test
	public void testLoadSocialScienceSubject() throws Exception {

		String testSubdir = "testLoadSocialScienceSubject";
		String vocabName = "social_science_subject";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, vocabName, true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		SKOSServer skosServer = new SKOSServerImpl(parentOfTest
				+ "/hive.properties");

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
			if (voc.getName().equals(vocabName)) {
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
