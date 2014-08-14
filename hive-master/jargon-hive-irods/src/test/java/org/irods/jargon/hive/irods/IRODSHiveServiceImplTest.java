package org.irods.jargon.hive.irods;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.FileNotFoundException;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.hive.irods.exception.IRODSHiveException;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IRODSHiveServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "IRODSHiveServiceImplTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;
	private static File vocabFile = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();

		ClassLoader loader = IRODSHiveServiceImplTest.class.getClassLoader();
		URL resc = loader.getResource("agrovoc.rdf");

		if (resc == null) {
			throw new Exception("unable to load agrovoc");
		}

		String vocabFileName = resc.getFile();

		vocabFile = new File(vocabFileName);

		if (!vocabFile.exists()) {
			throw new Exception("unable to load agrovoc test vocabulary");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testSaveOrUpdateVocabularyTerm() throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTerm";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
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

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsCollection);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsCollection,
						testURI);
		Assert.assertNotNull("did not find vocabulary entry I just added",
				actual);

		// test round-trip data values

		Assert.assertEquals("did not set preferredlabel",
				entry.getPreferredLabel(), actual.getPreferredLabel());
		Assert.assertEquals("did not set comment", entry.getComment(),
				actual.getComment());
		Assert.assertEquals("did not set unique name",
				entry.getDomainObjectUniqueName(),
				actual.getDomainObjectUniqueName());
		Assert.assertEquals("did not set URI", entry.getTermURI(),
				actual.getTermURI());
		Assert.assertEquals("did not set vocbulary name",
				entry.getVocabularyName(), actual.getVocabularyName());
		Assert.assertEquals("did not set metadata domain",
				entry.getMetadataDomain(), actual.getMetadataDomain());
	}

	@Test
	public void testSaveOrUpdateVocabularyTermCollectionModifyURI()
			throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTermCollectionModifyURI";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		String modifiedURI = "http://a.vocabulary#termmodified";
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

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsCollection);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);
		entry.setTermURI(modifiedURI);
		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsCollection,
						modifiedURI);
		Assert.assertNotNull("did not find vocabulary entry I just modified",
				actual);

		// test round-trip data values

		Assert.assertEquals("did not set preferredlabel",
				entry.getPreferredLabel(), actual.getPreferredLabel());
		Assert.assertEquals("did not set comment", entry.getComment(),
				actual.getComment());
		Assert.assertEquals("did not set unique name",
				entry.getDomainObjectUniqueName(),
				actual.getDomainObjectUniqueName());
		Assert.assertEquals("did not set URI", entry.getTermURI(),
				actual.getTermURI());
		Assert.assertEquals("did not set vocbulary name",
				entry.getVocabularyName(), actual.getVocabularyName());
		Assert.assertEquals("did not set metadata domain",
				entry.getMetadataDomain(), actual.getMetadataDomain());
	}

	@Test(expected = FileNotFoundException.class)
	public void testSaveOrUpdateVocabularyTermMissingCollection()
			throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTermMissingCollection";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsCollection);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

	}

	@Test
	public void testSaveOrUpdateVocabularyTermDataObject() throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTermDataObject";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsFile);
		vocabCollection.createNewFile();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsFile, testURI);
		Assert.assertNotNull("did not find vocabulary entry I just added",
				actual);

		// test round-trip data values

		Assert.assertEquals("did not set preferredlabel",
				entry.getPreferredLabel(), actual.getPreferredLabel());
		Assert.assertEquals("did not set comment", entry.getComment(),
				actual.getComment());
		Assert.assertEquals("did not set unique name",
				entry.getDomainObjectUniqueName(),
				actual.getDomainObjectUniqueName());
		Assert.assertEquals("did not set URI", entry.getTermURI(),
				actual.getTermURI());
		Assert.assertEquals("did not set vocbulary name",
				entry.getVocabularyName(), actual.getVocabularyName());
		Assert.assertEquals("did not set metadata domain",
				entry.getMetadataDomain(), actual.getMetadataDomain());
	}

	@Test
	public void testSaveOrUpdateVocabularyTermDataObjectAsModify()
			throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTermDataObjectAsModify";
		String testVocabTerm = testCollection;
		String modifiedVocabTerm = "modified1";
		String testURI = "http://a.vocabulary#term";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsFile);
		vocabCollection.createNewFile();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);
		entry.setPreferredLabel(modifiedVocabTerm);
		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsFile, testURI);
		Assert.assertNotNull("did not find vocabulary entry I just added",
				actual);

		// test round-trip data values

		Assert.assertEquals("did not set preferredlabel", modifiedVocabTerm,
				actual.getPreferredLabel());
		Assert.assertEquals("did not set comment", entry.getComment(),
				actual.getComment());
		Assert.assertEquals("did not set unique name",
				entry.getDomainObjectUniqueName(),
				actual.getDomainObjectUniqueName());
		Assert.assertEquals("did not set URI", entry.getTermURI(),
				actual.getTermURI());
		Assert.assertEquals("did not set vocbulary name",
				entry.getVocabularyName(), actual.getVocabularyName());
		Assert.assertEquals("did not set metadata domain",
				entry.getMetadataDomain(), actual.getMetadataDomain());
	}

	@Test(expected = FileNotFoundException.class)
	public void testSaveOrUpdateVocabularyTermDataObjectSayItsACollection()
			throws Exception {

		String testCollection = "testSaveOrUpdateVocabularyTermDataObjectSayItsACollection";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsFile);
		vocabCollection.createNewFile();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

	}

	@Test
	public void testValidateHiveVocabularyEntry() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("blah");
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidateHiveVocabularyEntryNullEntry() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = null;
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryNullVocab() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("blah");
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName(null);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryBlankVocab() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("blah");
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName("");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryNullName() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName(null);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryBlankName() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("");
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryNullDomain() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("xxx");
		entry.setMetadataDomain(null);
		entry.setPreferredLabel("label");
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryNullLabel() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("xxx");
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(null);
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryBlankLabel() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("xxx");
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel("");
		entry.setTermURI("xxx");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryNullURI() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("xxx");
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel("xxx");
		entry.setTermURI(null);
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test(expected = IRODSHiveException.class)
	public void testValidateHiveVocabularyEntryBlankURI() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName("xxx");
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel("xxx");
		entry.setTermURI("");
		entry.setVocabularyName("xxx");
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);
		irodsHiveService.validateHiveVocabularyEntry(entry);

	}

	@Test
	public void testListVocabularyTermsForIRODSAbsolutePathCollection()
			throws Exception {
		String testCollection = "testListVocabularyTermsForIRODSAbsolutePathCollection";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		String testVocabTerm2 = testCollection + "2";
		String testURI2 = "http://a.vocabulary#term2";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsFile);
		vocabCollection.mkdirs();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm2);
		entry.setTermURI(testURI2);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		List<HiveVocabularyEntry> actual = irodsHiveService
				.listVocabularyTermsForIRODSAbsolutePath(targetIrodsFile);

		Assert.assertEquals("did not return expected 2 entries", 2,
				actual.size());

	}

	@Test
	public void testListVocabularyTermsForIRODSAbsolutePathDataObject()
			throws Exception {
		String testCollection = "testListVocabularyTermsForIRODSAbsolutePathDataObject";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		String testVocabTerm2 = testCollection + "2";
		String testURI2 = "http://a.vocabulary#term2";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSHiveService irodsHiveService = new IRODSHiveServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount);

		String targetIrodsFile = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH + "/"
								+ testCollection);

		IRODSFile vocabCollection = irodsFileSystem.getIRODSFileFactory(
				irodsAccount).instanceIRODSFile(targetIrodsFile);
		vocabCollection.createNewFile();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		entry = new HiveVocabularyEntry();
		entry.setComment("");
		entry.setDomainObjectUniqueName(targetIrodsFile);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel(testVocabTerm2);
		entry.setTermURI(testURI2);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);

		List<HiveVocabularyEntry> actual = irodsHiveService
				.listVocabularyTermsForIRODSAbsolutePath(targetIrodsFile);

		Assert.assertEquals("did not return expected 2 entries", 2,
				actual.size());

	}

	@Test
	public void testDeleteVocabularyTermCollection() throws Exception {

		String testCollection = "testDeleteVocabularyTermCollection";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
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

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsCollection);
		entry.setMetadataDomain(MetadataDomain.COLLECTION);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);
		irodsHiveService.deleteVocabularyEntryForPathAndURI(
				targetIrodsCollection, testURI);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsCollection,
						testURI);
		Assert.assertNull("did not delete vocabulary entry I just added",
				actual);

	}

	@Test
	public void testDeleteVocabularyTermDataObject() throws Exception {

		String testCollection = "testDeleteVocabularyTermDataObject";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
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
		vocabCollection.createNewFile();

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setComment("comment");
		entry.setDomainObjectUniqueName(targetIrodsCollection);
		entry.setMetadataDomain(MetadataDomain.DATA);
		entry.setPreferredLabel(testVocabTerm);
		entry.setTermURI(testURI);
		entry.setVocabularyName("agrovoc");

		irodsHiveService.saveOrUpdateVocabularyTerm(entry);
		irodsHiveService.deleteVocabularyEntryForPathAndURI(
				targetIrodsCollection, testURI);

		HiveVocabularyEntry actual = irodsHiveService
				.findHiveVocabularyEntryForPathAndURI(targetIrodsCollection,
						testURI);
		Assert.assertNull("did not delete vocabulary entry I just added",
				actual);

	}

}
