package org.irods.jargon.hive.container;

import org.irods.jargon.hive.exception.JargonHiveException;
import org.irods.jargon.hive.service.VocabularyService;

import edu.unc.ils.mrc.hive.api.SKOSServer;

/**
 * Interface for the core Jargon/HIVE service. This is the container for the
 * HIVE services and provides access to basic services needed by Jargon.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface HiveContainer {

	/**
	 * Get the path to hive configuration file
	 * 
	 * @return the hiveConfiguration
	 */
	HiveConfiguration getHiveConfiguration();

	/**
	 * @param hiveConfiguration
	 *            the hiveConfiguration to set
	 */
	void setHiveConfiguration(HiveConfiguration hiveConfiguration);

	/**
	 * Method called upon startup to initialize the <code>SKOSServer</code>
	 * components. Setting the flag "start" to be true If hiveConfiguration is
	 * not setup or the SKOSServer has been started up, an exception arises
	 * 
	 * @throws JargonHiveException
	 */
	void init() throws JargonHiveException;

	/**
	 * Get the <code>SKOSServer</code> that contains the HIVE vocabularies
	 * 
	 * @return an SKOSServer instance
	 */
	SKOSServer getSkosServer();

	/**
	 * Method called to close the SkosServer if it has been started
	 */
	public abstract void shutdown();

	/**
	 * Get an instance of the
	 * <code>VocabularyService<code> based on HIVE configuration info if SKOSServer has been started
	 * 
	 * @return an instance of VocabularyService
	 * @throws JargonHiveException
	 */
	VocabularyService instanceVocabularyService() throws JargonHiveException;

}