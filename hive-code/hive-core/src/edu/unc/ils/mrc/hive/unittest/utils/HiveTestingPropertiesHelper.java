package edu.unc.ils.mrc.hive.unittest.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.TestingUtilsException;

/**
 * Helper classes for testing
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveTestingPropertiesHelper {

	/**
	 * Dir for the preconfigured 'functional' testing hive built using the
	 * HiveTestInstanceSetup utility
	 */
	public static final String TEST_HIVE_PARENT_DIR = "test.hive.parent.dir";

	/**
	 * Location of a source HIVE set of RDF and template config properties
	 */
	public static final String TEST_HIVE_SOURCE_DIR = "test.hive.source.dir";

	/**
	 * Location of a temp scratch directory under which any number of test HIVEs
	 * can be built in different unit and funtional tests
	 */
	public static final String TEST_HIVE_SCRATCH_DIR = "test.hive.scratch.dir";

	/**
	 * 
	 */
	public HiveTestingPropertiesHelper() {
	}

	public boolean checkTestHiveFuntionalSetup() throws TestingUtilsException {
		Properties hiveTestingProperties = getTestProperties();
		String hiveFunctionalSetupDir = hiveTestingProperties
				.getProperty(TEST_HIVE_PARENT_DIR);

		if (hiveFunctionalSetupDir == null || hiveFunctionalSetupDir.isEmpty()) {
			throw new TestingUtilsException(
					"no property set for test.hive.parent.dir");
		}

		File parentDirFile = new File(hiveFunctionalSetupDir);

		if (!parentDirFile.exists()) {
			return false;
		}

		if (!parentDirFile.isDirectory()) {
			return false;
		}

		return true;

	}

	/**
	 * Load the properties that control various tests from the
	 * testing.properties file on the code path
	 * 
	 * @return <code>Properties</code> class with the test values
	 * @throws TestingUtilsException
	 */
	public Properties getTestProperties() throws TestingUtilsException {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream("hive.testing.properties");
		Properties properties = new Properties();

		try {
			properties.load(in);
		} catch (IOException ioe) {
			throw new TestingUtilsException("error loading test properties",
					ioe);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
		}

		return properties;
	}

	/**
	 * Return the given property (by key) as an int
	 * 
	 * @param testingProperties
	 * @param key
	 * @return
	 * @throws TestingUtilsException
	 */
	public int getPropertyValueAsInt(final Properties testingProperties,
			final String key) throws TestingUtilsException {
		String propVal = (String) testingProperties.get(key);

		if (propVal == null || propVal.length() == 0) {
			throw new TestingUtilsException(
					"missing or invalid value in testing.properties");
		}

		int retVal = 0;

		try {
			retVal = Integer.parseInt(propVal);
		} catch (NumberFormatException nfe) {
			throw new TestingUtilsException(
					"port is in valid format to convert to int:" + propVal, nfe);
		}

		return retVal;
	}

	/**
	 * Get the standard iRODS test server port from the testing properties
	 * 
	 * @param testingProperties
	 * @return
	 * @throws TestingUtilsException
	 */
	public int getPortAsInt(final Properties testingProperties)
			throws TestingUtilsException {
		return getPropertyValueAsInt(testingProperties,
				TestingPropertiesHelper.IRODS_PORT_KEY);
	}

}
