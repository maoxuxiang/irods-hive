/**
 * 
 */
package org.irods.jargon.hive.container;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.irods.jargon.hive.exception.JargonHiveException;
import org.irods.jargon.hive.service.VocabularyService;
import org.irods.jargon.hive.service.VocabularyServiceImpl;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.SKOSServer;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSServerImpl;

/**
 * Main container for integrating iRODS and HIVE, contains the core HIVE
 * services and allows configuration and access to vocabularies and other
 * information.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 */
public class HiveContainerImpl implements HiveContainer {

	/**
	 * Flag that can check if startup was done
	 */
	private volatile boolean started = false;

	/**
	 * HIVE core service that picks up configured vocabularies and indexers.
	 * This is started up by this container class using the given config.
	 */
	private SKOSServer skosServer = null;

	/**
	 * Configuration properties used to initialize HIVE services
	 */
	private HiveConfiguration hiveConfiguration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.HiveContainer#getHiveConfiguration()
	 */
	@Override
	public HiveConfiguration getHiveConfiguration() {
		return hiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.container.HiveContainer#instanceVocabularyService()
	 */
	@Override
	public VocabularyService instanceVocabularyService()
			throws JargonHiveException {
		if (!started) {
			throw new JargonHiveException(
					"skosServer not started, please call init()");
		}
		return new VocabularyServiceImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.HiveContainer#setHiveConfiguration(org.irods.jargon
	 * .hive.HiveConfiguration)
	 */
	@Override
	public void setHiveConfiguration(final HiveConfiguration hiveConfiguration) {
		this.hiveConfiguration = hiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.HiveContainer#init()
	 */
	@Override
	public void init() throws JargonHiveException {

		if (hiveConfiguration == null) {
			throw new JargonHiveException("hiveConfiguration not provided");
		}

		if (started) {
			throw new IllegalArgumentException("hive already staretd");
		}

		startupSkosServer();
		started = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.container.HiveContainer#shutdown()
	 */
	@Override
	public void shutdown() {
		if (!started) {
			return;
		}

		skosServer.close();

	}

	/**
	 * Method called to startup an SkosServer by providing HIVE configuration
	 * path. Printing out the SKOS schemas of HIVE vocabularies
	 * 
	 * @throws JargonHiveException
	 */
	private void startupSkosServer() throws JargonHiveException {

		// logger.debug("starting SKOSServerImpl");
		// Levanto el servidor de vocabularios
		skosServer = new SKOSServerImpl(
				hiveConfiguration.getHiveConfigLocation());

		if (skosServer == null) {
			throw new JargonHiveException("unable to start HIVE server");
		}

		// TreeMap<String, SKOSScheme> schemaMap = server.getSKOSSchemas();
		// //added by Mike
		// logger.info("schema as tree map:" + schemaMap); //added by Mike

		skosServer.getSKOSSearcher();

		// server.getSKOSSchemas()
		/**
		 * Statistics test
		 */

		TreeMap<String, SKOSScheme> vocabularies = skosServer.getSKOSSchemas();
		Set<String> keys = vocabularies.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			SKOSScheme voc = vocabularies.get(it.next());
			System.out.println("NAME: " + voc.getName());
			System.out.println("\t LONG NAME: " + voc.getLongName());
			System.out.println("\t NUMBER OF CONCEPTS: "
					+ voc.getNumberOfConcepts());
			System.out.println("\t NUMBER OF RELATIONS: "
					+ voc.getNumberOfRelations());
			System.out.println("\t DATE: " + voc.getLastDate());
			System.out.println();
			System.out.println("\t SIZE: " + voc.getSubAlphaIndex("a").size());
			System.out.println();
			// System.out.println("\t TOP CONCEPTS: " +
			// voc.getNumberOfTopConcepts());
		}
	}

	@Override
	public SKOSServer getSkosServer() {
		return skosServer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HiveContainerImpl");
		sb.append("\n\t hiveConfiguration:");
		sb.append(hiveConfiguration);
		return sb.toString();
	}

}
