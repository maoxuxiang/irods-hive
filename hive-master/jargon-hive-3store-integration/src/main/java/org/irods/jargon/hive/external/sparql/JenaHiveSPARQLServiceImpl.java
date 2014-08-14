package org.irods.jargon.hive.external.sparql;

import java.io.ByteArrayOutputStream;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexer;
import org.irods.jargon.hive.external.utils.HiveException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.hive.external.utils.JenaModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

/**
 * Generic service to execute arbitrary SPARQL against the HIVE/iRODS index
 * triple store built by the {@link JenaHiveIndexer} service.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveSPARQLServiceImpl implements JenaHiveSPARQLService {

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveSPARQLServiceImpl.class);
	private final JenaHiveConfiguration jenaHiveConfiguration;
	private JenaModelManager jenaModelManager = null;
	private OntModel jenaModel;

	/**
	 * Initialize the SPARQL service with the Jena configuration
	 * 
	 * @param irodsAccessObjectFactory
	 *            {@link IRODSAccessObjectFactory} used for any iRODS
	 *            connections
	 * @param irodsAccount
	 *            {@link IRODSAccount} with host/port/zone/user information
	 * @param jenaHiveConfiguration
	 *            {@link JenaHiveConfiguration} that points to the triple store
	 *            to be queried
	 */
	public JenaHiveSPARQLServiceImpl(
			final JenaHiveConfiguration jenaHiveConfiguration) {

		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}

		this.jenaHiveConfiguration = jenaHiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService#init()
	 */
	@Override
	public void init() throws HiveException {
		log.info("init()");
		if (jenaHiveConfiguration.getJenaModelType() == JenaModelType.DATABASE_ONT) {
			log.info("building database ont via jenaModelManager...");
			jenaModelManager = new JenaModelManager();
			jenaModel = jenaModelManager
					.buildJenaDatabaseModel(jenaHiveConfiguration);
			log.info("jenaModelManagerInitialized");
		} else {
			throw new HiveQueryException(
					"query only supported for database ont");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService#
	 * queryAndReturnJSONAsString(java.lang.String)
	 */
	@Override
	public String queryAndReturnJSONAsString(final String sparqlString)
			throws HiveQueryException {
		log.info("queryAndReturnJSONAsString()");
		if (sparqlString == null || sparqlString.isEmpty()) {
			throw new IllegalArgumentException("null or empty sparqlString");
		}
		log.info("sparqlString:{}", sparqlString);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		QueryExecution qexec = null;
		try {
			Query query = QueryFactory.create(sparqlString);
			qexec = QueryExecutionFactory.create(query, jenaModel);
			log.info("running query...");
			ResultSet resultSet = qexec.execSelect();
			log.info("outputting as JSON");
			ResultSetFormatter.outputAsJSON(bos, resultSet);
			log.info("json in stream, now output to string");
			return bos.toString("UTF-8");
		} catch (Exception e) {
			log.error("exception processing query", e);
			throw new HiveQueryException(e);
		} finally {

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService#query(java
	 * .lang.String)
	 */
	@Override
	public ResultSet query(final String sparqlString) throws HiveQueryException {
		log.info("query()");
		if (sparqlString == null || sparqlString.isEmpty()) {
			throw new IllegalArgumentException("null or empty sparqlString");
		}
		log.info("sparqlString:{}", sparqlString);

		QueryExecution qexec = null;
		try {
			Query query = QueryFactory.create(sparqlString);
			qexec = QueryExecutionFactory.create(query, jenaModel);
			log.info("running query...");
			ResultSet resultSet = qexec.execSelect();
			return resultSet;
		} catch (Exception e) {
			log.error("exception processing query", e);
			throw new HiveQueryException(e);
		} finally {
			if (qexec != null) {
				qexec.close();
			}
		}

	}

}
