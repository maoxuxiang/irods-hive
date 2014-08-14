package org.irods.jargon.hive.testing;

import java.util.Properties;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;

public class VocabularyImportMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		// TODO Auto-generated method stub

		Properties testingProperties = new Properties();
		// final HiveTestingPropertiesHelper testingPropertiesHelper = new
		// HiveTestingPropertiesHelper();

		// testingProperties = testingPropertiesHelper.getTestProperties();

		// String hivePath = "/usr/local/hive/hive-data";
		String hivePath = "/Users/zhangle/temp/hive-data";

		// String hivePath = testingProperties
		// .getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
		SKOSScheme schema = new SKOSSchemeImpl(hivePath, "uat", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, true, true);

		// test some stuff to see if it worked

		HiveConfiguration hiveConfiguration;
		HiveContainer hiveContainer = new HiveContainerImpl();

		hiveConfiguration = new HiveConfiguration();
		hiveConfiguration.setHiveConfigLocation(hivePath + "/hive.properties");
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();

	}

}
