package org.irods.jargon.hive.external.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.FeatureSet;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.util.StoreUtils;

/**
 * Manages accessing underlying Jena model based on configuration information.
 * This is a bit of a prototype, it may end up being a full-blown service class
 * with an init method, etc
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaModelManager {

	public static final Logger log = LoggerFactory
			.getLogger(JenaModelManager.class);

	private Store store = null;

	/**
	 * 
	 */
	public JenaModelManager() {

	}

	/**
	 * Connect and build a Jena SDB (database) model based on the provided
	 * configuration. This will do creates as necessary
	 * 
	 * @return
	 * @throws HiveIndexerException
	 */
	public OntModel buildJenaDatabaseModel(
			final JenaHiveConfiguration jenaHiveConfiguration)
			throws HiveIndexerException {
		log.info("buildJenaDatabaseModel()");
		Connection conn = getJdbcConnectionBasedOnHiveConfig(jenaHiveConfiguration);
		log.info("have connection, creating jena model via sdb");
		FeatureSet featureSet = new FeatureSet();
		StoreDesc storeDesc = new com.hp.hpl.jena.sdb.StoreDesc(
				LayoutType.LayoutTripleNodesHash, DatabaseType.Derby,
				featureSet);
		log.info("storeDesc:{}", storeDesc);
		log.info("sdb connection created..getting store...");
		store = SDBFactory.connectStore(conn, storeDesc);
		log.info("checking is store is formatted...");
		try {
			boolean formatted = StoreUtils.isFormatted(store);
			if (!formatted) {
				log.info("not formatted, format it...");
				store.getTableFormatter().create();
				log.info("store is created");
			}

		} catch (SQLException e) {
			log.error("sql error checking if store is formatted", e);
			throw new HiveIndexerException(
					"sql exception inititalizing store and checking if formatted",
					e);
		}

		log.info("creating jenaModel from store...");
		Model defaultModel = SDBFactory.connectDefaultModel(store);
		log.info("create ont model from this...");

		OntModel ontModel = ModelFactory.createOntologyModel(
				OntModelSpec.OWL_DL_MEM, defaultModel);
		log.info("jena model created, default");

		return ontModel;

	}

	/**
	 * Check if the {@link JenaHiveConfiguration} has rational settings for a db
	 * model and throw an exception if it doesnt
	 * 
	 * @throws HiveIndexerException
	 */
	private void validateDbConfiguration(
			final JenaHiveConfiguration jenaHiveConfiguration)
			throws HiveIndexerException {
		if (jenaHiveConfiguration.getJenaDbDriverClass() == null
				|| jenaHiveConfiguration.getJenaDbDriverClass().isEmpty()) {
			throw new HiveIndexerException(
					"null or empty database driver in hive config class");
		}

		if (jenaHiveConfiguration.getJenaDbUri() == null
				|| jenaHiveConfiguration.getJenaDbUri().isEmpty()) {
			throw new HiveIndexerException("null or empty jena db uri");
		}

		if (jenaHiveConfiguration.getIdropContext() == null) {
			log.error("null idropContext for jena:{}", jenaHiveConfiguration);
			throw new HiveIndexerException("null idrop context");
		}

		if (jenaHiveConfiguration.getJenaDbType().equals(
				JenaHiveConfiguration.JENA_DERBY_DB_TYPE)) {
			// ok
			log.info("will be derby database type");
		} else {
			log.error("unknown database type for jena:{}",
					jenaHiveConfiguration);
			throw new HiveIndexerException("unknown database type");
		}

		// other things cannot be null but could be optional depending on the db

		if (jenaHiveConfiguration.getJenaDbPassword() == null) {
			throw new HiveIndexerException(
					"null jena db password, set to blank if not used");
		}

		if (jenaHiveConfiguration.getJenaDbUser() == null) {
			throw new HiveIndexerException(
					"null jena db user, set to blank if not used");
		}
	}

	/**
	 * Connect to a database via JDBC given the parameters in the
	 * {@link JenaHiveConfiguration}
	 * 
	 * @return {@link Connection} for use in Jena
	 * @throws HiveIndexerException
	 */
	private Connection getJdbcConnectionBasedOnHiveConfig(
			final JenaHiveConfiguration jenaHiveConfiguration)
			throws HiveIndexerException {

		log.info("getJdbcConnectionBasedOnHiveConfig()");
		validateDbConfiguration(jenaHiveConfiguration);
		
		log.info("attempting to load driver for:{}", jenaHiveConfiguration.getJenaDbDriverClass());
		
		 try {
			Class.forName(jenaHiveConfiguration.getJenaDbDriverClass()).newInstance();
		} catch (InstantiationException e1) {
			log.error("instantiation exception with given database driver", e1);
			throw new HiveIndexerException(e1);
		} catch (IllegalAccessException e1) {
			log.error("illegal access exception with given database driver", e1);
			throw new HiveIndexerException(e1);
		} catch (ClassNotFoundException e1) {
			log.error("class not found exception with given database driver", e1);
			throw new HiveIndexerException(e1);
		}

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", jenaHiveConfiguration.getJenaDbUser());
		connectionProps.put("password",
				jenaHiveConfiguration.getJenaDbPassword());

		try {
			conn = DriverManager.getConnection(
					jenaHiveConfiguration.getJenaDbUri(), connectionProps);
		} catch (SQLException e) {
			log.error(
					"sql exception connecting to database using jena config:{}",
					jenaHiveConfiguration);
			throw new HiveIndexerException(
					"sql exception connecting to specified Jena model db", e);
		}

		return conn;
	}

	public void close() {
		store.close();
	}

}
