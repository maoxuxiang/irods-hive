/**
 * 
 */
package org.irods.jargon.hive.container;

/**
 * Configuration used to start up HIVE services
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveConfiguration {

	/**
	 * Path to hive configuration file (hive.properties)
	 */
	private String hiveConfigLocation = "";

	public String getHiveConfigLocation() {
		return hiveConfigLocation;
	}

	public void setHiveConfigLocation(final String hiveConfigLocation) {
		this.hiveConfigLocation = hiveConfigLocation;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("hiveConfiguration");
		sb.append("\n\t hiveConfigLocation:");
		sb.append(hiveConfigLocation);
		return sb.toString();
	}

}
