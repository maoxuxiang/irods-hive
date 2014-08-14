package org.irods.jargon.hive.rest.vocabservice;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.irods.jargon.hive.rest.auth.DefaultHttpClientAndContext;
import org.irods.jargon.hive.rest.auth.RestAuthUtils;
import org.irods.jargon.hive.rest.vocabservice.utils.RestTestingProperties;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.testframework.HiveScratchAreaCreator;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jargon-beans.xml",
		"classpath:rest-servlet.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class RestVocabularySearchServiceTest implements ApplicationContextAware {

	@SuppressWarnings("unused")
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;
	private static TJWSEmbeddedJaxrsServer server;
	private static ApplicationContext applicationContext;
	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "RestVocabularySearchServiceTest";

	@Override
	public void setApplicationContext(final ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();
		hiveScratchAreaCreator = new HiveScratchAreaCreator(testingProperties);

		if (testingPropertiesHelper.checkTestHiveFuntionalSetup() == false) {
			throw new Exception(
					"test hive not set up, run HiveTestInstanceSetup");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (server != null) {
			server.stop();
		}
	}

	@Before
	public void setUp() throws Exception {

		if (server != null) {
			return;
		}

		int port = testingPropertiesHelper.getPropertyValueAsInt(
				testingProperties, RestTestingProperties.REST_PORT_PROPERTY);
		server = new TJWSEmbeddedJaxrsServer();
		server.setPort(port);
		ResteasyDeployment deployment = server.getDeployment();

		server.start();
		Dispatcher dispatcher = deployment.getDispatcher();
		SpringBeanProcessor processor = new SpringBeanProcessor(dispatcher,
				deployment.getRegistry(), deployment.getProviderFactory());
		((ConfigurableApplicationContext) applicationContext)
				.addBeanFactoryPostProcessor(processor);

		SpringResourceFactory noDefaults = new SpringResourceFactory(
				"restVocabularySearchService", applicationContext,
				RestVocabularySearchService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSearchConceptByTermAndVocabs() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("http://localhost:");
		sb.append(testingPropertiesHelper.getPropertyValueAsInt(
				testingProperties, RestTestingProperties.REST_PORT_PROPERTY));
		sb.append("/search");
		sb.append("?searchTerm=stars&vocabs=uat,agrovoc");
		
		DefaultHttpClientAndContext clientAndContext = RestAuthUtils
				.httpClientSetup(testingProperties);
		
		try {

			HttpGet httpget = new HttpGet(sb.toString());
			httpget.addHeader("accept", "application/json");

			HttpResponse response = clientAndContext.getHttpClient().execute(
					httpget, clientAndContext.getHttpContext());
			HttpEntity entity = response.getEntity();
			Assert.assertEquals(200, response.getStatusLine().getStatusCode());
			Assert.assertNotNull(entity);
			String entityData = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			System.out.println("JSON>>>");
			System.out.println(entityData);
			ObjectMapper objectMapper = new ObjectMapper();
			Set<ConceptProxy> actual = objectMapper.readValue(entityData,
					Set.class);


			Assert.assertNotNull("no list of concepts returned", actual);
			Assert.assertFalse("concept list is empty", actual.isEmpty());

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			clientAndContext.getHttpClient().getConnectionManager().shutdown();
		}
	}

}
