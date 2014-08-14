package org.irods.jargon.hive.external.indexer;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.core.utils.IRODSUriUtils;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.hive.irods.HiveVocabularyEntry;
import org.irods.jargon.hive.irods.IRODSHiveService;
import org.irods.jargon.hive.irods.IRODSHiveServiceImpl;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

public class JenaHiveIndexerServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "JenaHiveIndexerServiceImplTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;
	private static File jenaVocabFile = null;
	private static File ontFile = null;

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

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testExecute() throws Exception {
		String testCollection = "testExecute";

		String[] terms = { "http://www.fao.org/aos/agrovoc#c_28638",
				"http://www.fao.org/aos/agrovoc#c_1669",
				"http://www.fao.org/aos/agrovoc#c_431",
				"http://www.fao.org/aos/agrovoc#c_5630",
				"http://www.fao.org/aos/agrovoc#c_16137",
				"http://www.fao.org/aos/agrovoc#c_919" };

		int colls = 3;
		int filesPerColl = 50;
		int termsPerFile = 4;
		int termsPerColl = 3;

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
					entry.setTermURI(IRODSUriUtils
							.buildURIForAnAccountWithNoUserInformationIncluded(
									irodsAccount, vocabFile.getAbsolutePath())
							.toString());
					entry.setVocabularyName("agrovoc");

					irodsHiveService.saveOrUpdateVocabularyTerm(entry);
				}

			}

		}

		JenaHiveConfiguration jenaHiveConfiguration = new JenaHiveConfiguration();
		jenaHiveConfiguration.setAutoCloseJenaModel(false);
		JenaHiveConfiguration configuration = new JenaHiveConfiguration();
		configuration.getVocabularyRDFFileNames().add(jenaVocabFile.getPath());
		configuration.setIrodsRDFFileName(ontFile.getPath());
		configuration.setJenaModelType(JenaModelType.MEMORY_ONT);

		JenaHiveIndexer jenaHiveIndexerService = new JenaHiveIndexerServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount,
				configuration);

		Model jenaModel = jenaHiveIndexerService.execute();
		Assert.assertNotNull("null jena model", jenaModel);

	}
}
