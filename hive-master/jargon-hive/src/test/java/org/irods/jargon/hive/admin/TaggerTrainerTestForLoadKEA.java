package org.irods.jargon.hive.admin;

import java.util.Properties;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.testing.HiveConfigurationTestUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class TaggerTrainerTestForLoadKEA {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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

	/**
	 * Test method for
	 * {@link edu.unc.ils.mrc.hive.admin.TaggerTrainer#trainKEAAutomaticIndexingModule()}
	 * .
	 */
	@Test
	public void testTagUATWithKEA() throws Exception {

		String hivePath = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
		SKOSScheme schema = new SKOSSchemeImpl(hivePath, "uat", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, true, true);

		HiveConfiguration hiveConfiguration;
		HiveContainer hiveContainer = new HiveContainerImpl();

		hiveConfiguration = new HiveConfigurationTestUtilities(
				testingProperties).buildHiveConfiguration();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();

	}

	@Test
	public void testTagAgrovocWithKEA() throws Exception {

		String hivePath = testingProperties
				.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
		SKOSScheme schema = new SKOSSchemeImpl(hivePath, "agrovoc", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, true, true);

		// test some stuff to see if it worked

	}

}
