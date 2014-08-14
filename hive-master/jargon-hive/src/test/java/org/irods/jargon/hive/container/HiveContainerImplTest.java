package org.irods.jargon.hive.container;

import org.irods.jargon.hive.service.VocabularyService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HiveContainerImplTest {

	static final HiveConfiguration hiveConfiguration = new HiveConfiguration();
	static HiveContainer hiveContainer = new HiveContainerImpl();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		// hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");

		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
		hiveContainer = null;
		Thread.sleep(10000);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInit() throws Exception {

		// hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");

		Assert.assertNotNull("did not start skos server",
				hiveContainer.getSkosServer());

	}

	@Test
	public void testInstanceVocabularyServer() throws Exception {

		VocabularyService vocabularyService = hiveContainer
				.instanceVocabularyService();
		Assert.assertNotNull("did not get vocab service", vocabularyService);
	}

}
