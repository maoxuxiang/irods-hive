package edu.unc.ils.mrc.hive2.api.impl;

import java.util.Properties;

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

public class HiveLuceneIndexImplTest {

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
		String testSubdir = "HiveLuceneIndexImplTest";
		String vocabName = "uat";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "uat", true);

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
	public void testGetNumConcepts() throws Exception {

		HiveVocabularyImpl hiveVocabulary = (HiveVocabularyImpl) skosServer
				.getSKOSSchemas().get("uat").getHiveVocabulary();

		HiveLuceneIndexImpl actual = (HiveLuceneIndexImpl) hiveVocabulary
				.getLuceneIndex();
		long nbrConcepts = actual.getNumConcepts();
		Assert.assertTrue("didnt find any concepts bro", nbrConcepts > 0);
	}

}
