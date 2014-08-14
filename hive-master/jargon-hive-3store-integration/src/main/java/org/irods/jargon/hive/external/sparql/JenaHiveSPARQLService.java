package org.irods.jargon.hive.external.sparql;

import org.irods.jargon.hive.external.utils.HiveException;
import org.irods.jargon.hive.external.utils.JenaModelManager;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Interface for a service to query the triple store associated with HIVE/iRODS
 * data, using plain SPARQL and returning the resulting Jena model.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface JenaHiveSPARQLService {

	/**
	 * Init method that must be called before use to get a
	 * {@link JenaModelManager} that will support subsequent queries
	 * 
	 * @throws HiveException
	 */
	public abstract void init() throws HiveException;

	/**
	 * Execute a query and return a jena <code>ResultSet</code> that is the
	 * result
	 * 
	 * @param sparqlString
	 *            <code>String</code> with valid SPARQL query
	 * @return
	 * @throws HiveQueryException
	 */
	ResultSet query(String sparqlString) throws HiveQueryException;

	/**
	 * Query and build a JSON representation of the results
	 * 
	 * @param sparqlString
	 *            <code>String</code> with valid SPARQL query
	 * @return <code>String</code> containing JSON data representing the result
	 *         set
	 * @throws HiveQueryException
	 */
	String queryAndReturnJSONAsString(final String sparqlString)
			throws HiveQueryException;

}