package org.irods.jargon.hive.service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.exception.VocabularyNotFoundException;
import org.irods.jargon.hive.service.domain.VocabularyInfo;
import org.irods.jargon.hive.testing.HiveConfigurationTestUtilities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

public class VocabularyServiceTest {

	private static HiveConfiguration hiveConfiguration;
	private static HiveContainer hiveContainer = new HiveContainerImpl();

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();

		hiveConfiguration = new HiveConfigurationTestUtilities(
				testingProperties).buildHiveConfiguration();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}

	@Test
	public void testGetAgrovoc() throws Exception {

		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		SKOSScheme actual = vocabularyService.getVocabularyByName("agrovoc");
		TestCase.assertNotNull("did not get vocab", actual);
	}

	@Test
	public void testGetOriginFromUATReference() throws Exception {

		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		ConceptProxy conceptProxy = vocabularyService.getConceptByURI(
				"http://purl.org/astronomy/uat", "T854");

		TestCase.assertNotNull("did not get concept", conceptProxy);
		TestCase.assertEquals("did not get origin", "uat",
				conceptProxy.getOrigin());
	}

	@Test
	public void testGetUAT() throws Exception {

		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		SKOSScheme actual = vocabularyService.getVocabularyByName("uat");
		TestCase.assertNotNull("did not get vocab", actual);
	}

	@Test
	public void testGetFirstConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy x = vocabularyService.getFirstConcept("agrovoc");
		TestCase.assertNotNull("did not load first concept", x);
	}

	@Test
	public void testGetFirstConceptForUat() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy x = vocabularyService.getFirstConcept("uat");
		TestCase.assertNotNull("did not load first concept", x);
	}

	@Test
	public void testSuggestTermsFor() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<AutocompleteTerm> x = null;
		x = vocabularyService.suggestTermsFor("agrovoc", "ability", 3);
		TestCase.assertFalse("did not find suggested terms", x == null);
	}

	@Test
	public void testListVocabularyNames() throws Exception {

		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		List<String> allVocabNames = vocabularyService.getAllVocabularyNames();
		TestCase.assertFalse("did not load vocabularies",
				allVocabNames.isEmpty());
	}

	@Test
	public void testgetConceptByURI() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy X = vocabularyService.getConceptByURI(
				"http://www.fao.org/aos/agrovoc#", "c_49830");
		TestCase.assertFalse("did not get concept by URI", X == null);
	}

	@Test
	public void testSearchConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		Set<ConceptProxy> rankedSet = null;
		List<String> openVocabularies = vocabularyService
				.getAllVocabularyNames();
		rankedSet = vocabularyService
				.searchConcept("ability", openVocabularies);
		TestCase.assertFalse("did not find concept", rankedSet.isEmpty());
	}

	@Test
	public void testGetChildConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> ChildrenList = null;
		ChildrenList = vocabularyService.getChildConcept(
				"http://www.fao.org/aos/agrovoc#", "c_49830");
		TestCase.assertFalse("did not load child concept",
				ChildrenList.isEmpty());
	}

	@Test
	public void testGetSubTopConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("uat", "A", true);
		TestCase.assertFalse("did not load sub top concept",
				fatherList.isEmpty());
	}

	@Test
	public void testGetSubTopConceptWithBriefSettoFalse() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("uat", "A", false);
		TestCase.assertFalse("did not load sub top concept",
				fatherList.isEmpty());
	}

	@Test
	public void testGetConceptProxyForTopOfVocabulary() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy proxy = vocabularyService
				.getConceptProxyForTopOfVocabulary("uat", "", true);
		Assert.assertNotNull("null proxy", proxy);
		Assert.assertTrue("did not set as top level", proxy.isTopLevel());
		Assert.assertEquals("did not set vocab name", "uat", proxy.getOrigin());
		Assert.assertFalse("did not set child (narrower) terms", proxy
				.getNarrower().isEmpty());
	}

	@Test(expected = VocabularyNotFoundException.class)
	public void testGetConceptProxyForTopOfVocabularyNotExists()
			throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getConceptProxyForTopOfVocabulary("bogusvocabhere",
				"", true);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConceptProxyForTopOfVocabularyNullVocabNullName()
			throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getConceptProxyForTopOfVocabulary(null, "", true);
	}

	@Test
	public void testGetNumberOfConceptAndRelations() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		long x = vocabularyService.getNumberOfConcept("uat");
		long y = vocabularyService.getNumerOfRelations("uat");
		System.out.println("uat, number of concept: " + x);
		System.out.println("uat, number of relations: " + y);
	}

	@Test
	public void testGetLastUpdate() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getLastUpdateDate("uat");
	}

	@Test
	public void testGetAllVocabulary() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<VocabularyInfo> allVocabs = vocabularyService.getAllVocabularies();

		TestCase.assertFalse(
				"did not load vocabularies with properties to List",
				allVocabs.isEmpty());

	}

	@Test
	public void testGetVocabularyProperties() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		HashMap<String, HashMap<String, String>> vocabProps = vocabularyService
				.getVocabularyProperties();
		TestCase.assertFalse("did not load vocabulary properties",
				vocabProps.isEmpty());
	}

	@Test
	public void testGetFirstConceptMesh() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy x = vocabularyService.getFirstConcept("mesh");
		TestCase.assertNotNull("did not load first concept", x);
	}

}
