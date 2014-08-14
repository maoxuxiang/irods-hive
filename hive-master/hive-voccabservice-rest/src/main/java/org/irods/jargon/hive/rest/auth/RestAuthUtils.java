/**
 * 
 */
package org.irods.jargon.hive.rest.auth;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.irods.jargon.hive.rest.vocabservice.utils.RestTestingProperties;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.TestingUtilsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class RestAuthUtils {

	private static Logger log = LoggerFactory.getLogger(RestAuthUtils.class);

	/**
	 * Return boilerplate http client for testing
	 * 
	 * @param testingProperties
	 * @return
	 * @throws TestingUtilsException
	 */
	public static DefaultHttpClientAndContext httpClientSetup(
			final Properties testingProperties) throws TestingUtilsException {

		if (testingProperties == null) {
			throw new IllegalArgumentException("null testingProperties");
		}

		TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
		HttpHost targetHost = new HttpHost("localhost",
				testingPropertiesHelper.getPropertyValueAsInt(
						testingProperties,
						RestTestingProperties.REST_PORT_PROPERTY), "http");

		DefaultHttpClient httpclient = new DefaultHttpClient();

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		DefaultHttpClientAndContext clientAndContext = new DefaultHttpClientAndContext();
		clientAndContext.setHost(targetHost.getHostName());
		clientAndContext.setHttpClient(httpclient);
		clientAndContext.setHttpContext(localcontext);
		return clientAndContext;
	}
}
