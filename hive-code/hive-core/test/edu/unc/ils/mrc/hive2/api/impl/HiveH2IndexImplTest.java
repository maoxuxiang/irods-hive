package edu.unc.ils.mrc.hive2.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

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
import edu.unc.ils.mrc.hive2.api.HiveConcept;

public class HiveH2IndexImplTest {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;
	static SKOSServer skosServer = null;

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
		String testSubdir = "HiveH2IndexImplTest";
		String vocabName = "mesh";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "mesh", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		skosServer = new SKOSServerImpl(parentOfTest + "/hive.properties");

		Assert.assertNotNull("null skosServer", skosServer);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (skosServer != null) {
			skosServer.close();
		}
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
	public void testExists() {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();
		Assert.assertTrue(h2.exists());

	}

	@Test
	public void testGetLastUpdate() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();
		Date lastUpdate = h2.getLastUpdate();
		Assert.assertNotNull(lastUpdate);
	}

	@Test
	public void testGetStats() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();
		Map<String, Long> stats = h2.getStats();
		Assert.assertNotNull("null stats", stats);
		Assert.assertFalse("no stats", stats.isEmpty());
	}

	@Test
	public void testGetCreated() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();
		Date actual = h2.getCreated();
		Assert.assertNotNull(actual);
	}

	@Test
	public void testAddConcept() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();

		HiveConcept concept = new HiveConcept();
		concept.setPrefLabel("testAddConcept");
		concept.setQName(new QName("http://www.test.com", "testAddConcept"));

		h2.addConcept(concept);

		HiveConcept actual = h2.findConceptByName(concept.getPrefLabel());
		Assert.assertNotNull("concept not added and found", actual);
	}

	@Test
	public void testUpdateConcept() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();

		HiveConcept concept = new HiveConcept();
		concept.setPrefLabel("testUpdateConcept");
		concept.setQName(new QName("http://www.test.com", "testUpdateConcept"));

		h2.addConcept(concept);

		concept.setPrefLabel("testUpdateConceptAfter");
		h2.updateConcept(concept);

		HiveConcept actual = h2.findConceptByName(concept.getPrefLabel());
		Assert.assertNotNull("concept not updated and found", actual);
	}

	@Test
	public void testRemoveConcept() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();

		HiveConcept concept = new HiveConcept();
		concept.setPrefLabel("testUpdateConcept");
		concept.setQName(new QName("http://www.test.com", "testUpdateConcept"));

		h2.addConcept(concept);
		h2.removeConcept(concept.getQName());

		HiveConcept actual = h2.findConceptByName(concept.getPrefLabel());
		Assert.assertNull("concept not removed", actual);
	}

	@Test
	public void testFindConceptsByName() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();

		HiveConcept concept = new HiveConcept();
		concept.setPrefLabel("testUpdateConcept");
		concept.setQName(new QName("http://www.test.com", "testUpdateConcept"));

		h2.addConcept(concept);
		List<HiveConcept> actual = h2.findConceptsByName(
				concept.getPrefLabel(), false);
		Assert.assertTrue("did not find the  concept", actual.size() > 0);

	}

	@Test
	public void testFindAllConcepts() throws Exception {
		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("mesh").getHiveVocabulary();
		HiveH2IndexImpl h2 = (HiveH2IndexImpl) hiveVocabulary.getH2Index();

		Map<String, QName> actual = h2.findAllConcepts(false);
		Assert.assertFalse("did not find concepts", actual.size() == 0);
	}

}
