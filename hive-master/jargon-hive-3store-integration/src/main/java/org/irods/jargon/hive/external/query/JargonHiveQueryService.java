package org.irods.jargon.hive.external.query;

import org.irods.jargon.hive.external.sparql.HiveQueryException;
import org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService;

/**
 * Service to provide useful general queries of hive/iRODS data. Note that the
 * {@link JenaHiveSPARQLService} provides direct SPARQL support, this wraps that
 * support with convenience methods based on SPARQL templates.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface JargonHiveQueryService {

	/**
	 * Given a vocabulary term in URI format, return a <code>String</code> in
	 * JSON format with the SPARQL results
	 * 
	 * @param vocabularyUri
	 *            <code>String</code> in URI format that is a vocabulary term
	 * @return <code>String</code> of JSON with the query results
	 * @throws HiveQueryException
	 */
	String queryForUri(String vocabularyUri) throws HiveQueryException;

	/**
	 * Given a vocabulary term in URI format, return a <code>String</code> in
	 * JSON format with the SPARQL results for terms that are realted to this
	 * term
	 * 
	 * @param vocabularyUri
	 *            <code>String</code> in URI format that is a vocabulary term
	 * @return <code>String</code> of JSON with the query results
	 * @throws HiveQueryException
	 */
	String queryForUriRelated(String vocabularyUri) throws HiveQueryException;

}