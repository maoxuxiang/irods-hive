package org.irods.jargon.hive.external.utils.template;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SPARQLTemplateUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSPARQLTemplateAndSubstituteValues() throws Exception {
		Map<String, String> replacementValues = new HashMap<String, String>();
		replacementValues.put("hello", "hello");
		String templateData = SPARQLTemplateUtils
				.getSPARQLTemplateAndSubstituteValues(
						"/test-sparql-template/helloTest.txt",
						replacementValues);
		Assert.assertNotNull("null template data", templateData);
		Assert.assertEquals("hello,hello", templateData);
	}

	@Test(expected = HiveTemplateException.class)
	public void testGetSPARQLTemplateAndSubstituteValuesNoFile()
			throws Exception {
		Map<String, String> replacementValues = new HashMap<String, String>();
		replacementValues.put("hello", "hello");
		SPARQLTemplateUtils.getSPARQLTemplateAndSubstituteValues(
				"/test-sparql-template/helloTestxxxx.txt", replacementValues);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSPARQLTemplateAndSubstituteValuesNullFile()
			throws Exception {
		Map<String, String> replacementValues = new HashMap<String, String>();
		replacementValues.put("hello", "hello");
		SPARQLTemplateUtils.getSPARQLTemplateAndSubstituteValues(null,
				replacementValues);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSPARQLTemplateAndSubstituteValuesNullValues()
			throws Exception {
		Map<String, String> replacementValues = new HashMap<String, String>();
		replacementValues.put("hello", "hello");
		SPARQLTemplateUtils.getSPARQLTemplateAndSubstituteValues("hi", null);

	}

	@Test
	public void testGetRawSPARQLTemplate() throws Exception {
		String templateData = SPARQLTemplateUtils
				.getRawSPARQLTemplate("/sparql-template/queryAllForTerm.txt");
		Assert.assertNotNull("null template data", templateData);
		Assert.assertFalse("template data is empty", templateData.isEmpty());
	}

	@Test(expected = HiveTemplateException.class)
	public void testGetRawSPARQLTemplateNoFile() throws Exception {
		SPARQLTemplateUtils
				.getRawSPARQLTemplate("/sparql-template/queryAllForTermxxxx.txt");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetRawSPARQLTemplateNull() throws Exception {
		SPARQLTemplateUtils.getRawSPARQLTemplate(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetRawSPARQLTemplateBlank() throws Exception {
		SPARQLTemplateUtils.getRawSPARQLTemplate("");
	}

}
