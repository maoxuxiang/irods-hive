package org.irods.jargon.hive.external.query;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexer;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerServiceImpl;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerVisitorTest;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.hive.irods.HiveVocabularyEntry;
import org.irods.jargon.hive.irods.IRODSHiveService;
import org.irods.jargon.hive.irods.IRODSHiveServiceImpl;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.filemanip.ScratchFileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

public class JargonHiveQueryServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "JargonHiveQueryServiceImplTest";
	public static final String DERBY_DIR = "derbyDir";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;
	private static File jenaVocabFile = null;
	private static File ontFile = null;
	private static ScratchFileUtils scratchFileUtils = null;
	private static JenaHiveConfiguration configuration;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();

		ClassLoader loader = JenaHiveIndexerVisitorTest.class.getClassLoader();
		URL resc = loader.getResource("agrovoc.rdf");

		if (resc == null) {
			throw new Exception("unable to load agrovoc");
		}

		String vocabFileName = resc.getFile();

		jenaVocabFile = new File(vocabFileName);

		if (!jenaVocabFile.exists()) {
			throw new Exception("unable to load agrovoc test vocabulary");
		}

		URL ont = loader.getResource("irodsSchema.xml");

		if (ont == null) {
			throw new Exception("unable to load ont");
		}

		String ontFileName = ont.getFile();

		ontFile = new File(ontFileName);

		if (!ontFile.exists()) {
			throw new Exception("unable to load irods ontology");
		}

		scratchFileUtils = new ScratchFileUtils(testingProperties);
		scratchFileUtils
				.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		String pathToDerby = scratchFileUtils
				.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH
						+ "/" + DERBY_DIR);
		File derbyDirFile = new File(pathToDerby);
		derbyDirFile.mkdirs();

		String testCollection = "testExecuteOnt";

		String[] terms = { "http://www.fao.org/aos/agrovoc#c_28638",
				"http://www.fao.org/aos/agrovoc#c_1669",
				"http://www.fao.org/aos/agrovoc#c_20",
				"http://www.fao.org/aos/agrovoc#c_5630",
				"http://www.fao.org/aos/agrovoc#c_16137",
				"http://www.fao.org/aos/agrovoc#c_919",
				"http://www.fao.org/aos/agrovoc#c_24004",
				"http://www.fao.org/aos/agrovoc#c_250",
				"http://www.fao.org/aos/agrovoc#c_24029",
				"http://www.fao.org/aos/agrovoc#c_25196",
				"http://www.fao.org/aos/agrovoc#c_24847",
				"http://www.fao.org/aos/agrovoc#c_3705",
				"http://www.fao.org/aos/agrovoc#c_34295" };

		int colls = 3;
		int filesPerColl = 10;
		int termsPerFile = 5;
		int termsPerColl = 4;

		/*
		 * pathological int colls = 50; int filesPerColl = 20; int termsPerFile
		 * = 3; int termsPerColl = 2;
		 */

		int termIdx = -1;

		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsCollection);
		vocabCollection.mkdirs();

		IRODSFile vocabFile;

		HiveVocabularyEntry entry;

		for (int i = 0; i < colls; i++) {

			vocabCollection = irodsFileSystem.getIRODSFileFactory(irodsAccount)
					.instanceIRODSFile(
							targetIrodsCollection + "/" + "subdirectory" + i);
			vocabCollection.mkdirs();

			entry = new HiveVocabularyEntry();
			entry.setComment("comment");
			entry.setDomainObjectUniqueName(vocabCollection.getAbsolutePath());
			entry.setMetadataDomain(MetadataDomain.COLLECTION);

			for (int t = 0; t < termsPerColl; t++) {
				termIdx++;
				if (termIdx >= terms.length) {
					termIdx = 0;
				}

				entry.setPreferredLabel(terms[t]);
				entry.setTermURI(terms[t]);
				entry.setVocabularyName("agrovoc");

				irodsHiveService.saveOrUpdateVocabularyTerm(entry);
			}

			for (int d = 0; d < filesPerColl; d++) {
				vocabFile = irodsFileSystem.getIRODSFileFactory(irodsAccount)
						.instanceIRODSFile(
								vocabCollection.getAbsolutePath() + "/"
										+ "hivefile" + d);
				vocabFile.createNewFile();

				entry = new HiveVocabularyEntry();
				entry.setComment("comment");
				entry.setDomainObjectUniqueName(vocabFile.getAbsolutePath());
				entry.setMetadataDomain(MetadataDomain.DATA);

				for (int t = 0; t < termsPerFile; t++) {
					termIdx++;
					if (termIdx >= terms.length) {
						termIdx = 0;
					}

					entry.setPreferredLabel(terms[t]);
					entry.setTermURI(terms[t]);
					entry.setVocabularyName("agrovoc");

					irodsHiveService.saveOrUpdateVocabularyTerm(entry);
				}
			}
		}

		configuration = new JenaHiveConfiguration();
		configuration.getVocabularyRDFFileNames().add(jenaVocabFile.getPath());
		configuration.setIrodsRDFFileName(ontFile.getPath());
		configuration.setJenaModelType(JenaModelType.DATABASE_ONT);
		configuration.setAutoCloseJenaModel(false);
		configuration
				.setJenaDbDriverClass("org.apache.derby.jdbc.EmbeddedDriver");
		configuration.setJenaDbUser("test");
		configuration.setJenaDbPassword("test");
		configuration.setJenaDbType(JenaHiveConfiguration.JENA_DERBY_DB_TYPE);
		configuration.setIdropContext("http://localhost:8080/idrop-web2");

		configuration.setJenaDbUri("jdbc:derby:" + pathToDerby + "/"
				+ IRODS_TEST_SUBDIR_PATH + ";create=true");

		JenaHiveIndexer jenaHiveIndexerService = new JenaHiveIndexerServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount,
				configuration);

		Model jenaModel = jenaHiveIndexerService.execute();
		Assert.assertNotNull("null jena model", jenaModel);
		jenaModel.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testQueryForUri() throws Exception {
		testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		JargonHiveQueryService queryService = new JargonHiveQueryServiceImpl(

		configuration);

		String testUri = "http://www.fao.org/aos/agrovoc#c_1669";
		String json = queryService.queryForUri(testUri);
		Assert.assertNotNull("null json returned", json);
		Assert.assertFalse("empty json returned", json.isEmpty());
		Assert.assertTrue("did not find file in json",
				json.indexOf(IRODS_TEST_SUBDIR_PATH) > -1);

	}

	@Test
	public void testQueryForRelatedUri() throws Exception {
		testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		JargonHiveQueryService queryService = new JargonHiveQueryServiceImpl(

		configuration);

		String testUri = "http://www.fao.org/aos/agrovoc#c_8879";
		String json = queryService.queryForUriRelated(testUri);
		Assert.assertNotNull("null json returned", json);
		Assert.assertFalse("empty json returned", json.isEmpty());
		Assert.assertTrue("did not find file in json",
				json.indexOf(IRODS_TEST_SUBDIR_PATH) > -1);

	}
}
