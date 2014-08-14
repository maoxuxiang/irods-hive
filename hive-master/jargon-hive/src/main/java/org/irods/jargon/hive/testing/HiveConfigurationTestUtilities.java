/**
 * 
 */
package org.irods.jargon.hive.testing;

import java.util.Properties;

import org.irods.jargon.hive.container.HiveConfiguration;

import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * Utilities for configuring HIVE tests
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveConfigurationTestUtilities {

	private Properties hiveProperties;

	public Properties getHiveProperties() {
		return hiveProperties;
	}

	public void setHiveProperties(final Properties hiveProperties) {
		this.hiveProperties = hiveProperties;
	}

	public HiveConfigurationTestUtilities(final Properties hiveProperties) {

		if (hiveProperties == null) {
			throw new IllegalArgumentException("null hiveProperties");
		}

		this.hiveProperties = hiveProperties;

	}

	@SuppressWarnings("unused")
	private HiveConfigurationTestUtilities() {
	}

	/**
	 * Return a {@link HiveConfiguration} that matches the given properties
	 * 
	 * @return
	 */
	public HiveConfiguration buildHiveConfiguration() {

		HiveConfiguration hiveConfiguration = new HiveConfiguration();

		StringBuilder sb = new StringBuilder();
		sb.append(hiveProperties
				.get(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR));
		sb.append("/");
		sb.append("hive.properties");

		hiveConfiguration.setHiveConfigLocation(sb.toString());
		return hiveConfiguration;
	}

}
