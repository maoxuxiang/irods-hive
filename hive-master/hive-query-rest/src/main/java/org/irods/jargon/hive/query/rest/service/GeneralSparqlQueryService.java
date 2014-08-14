/**
 * 
 */
package org.irods.jargon.hive.query.rest.service;

import org.irods.jargon.hive.external.sparql.HiveQueryException;
import org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
@Controller
public class GeneralSparqlQueryService {

	private JenaHiveSPARQLService jargonHiveSparqlService;

	public static final Logger log = LoggerFactory
			.getLogger(GeneralSparqlQueryService.class);

	/**
	 * Do a straight sparql query and return JSON as it comes from jena
	 * 
	 * @param query
	 *            <code>String</code> containing sparql
	 * @return <code>String</code> with the JSON response from jena
	 * @throws HiveQueryServiceException
	 */
	@RequestMapping(value = "/sparql", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String querySparqlReturnJson(@RequestBody final String query)
			throws HiveQueryServiceException {

		log.info("querySparqlReturnJson");

		if (query == null || query.isEmpty()) {
			throw new HiveQueryServiceException("null or empty query ");
		}

		log.info("query:{}", query);

		try {
			return jargonHiveSparqlService.queryAndReturnJSONAsString(query);
		} catch (HiveQueryException e) {
			log.error("HiveQueryException querying with sparql", e);
			throw new HiveQueryServiceException(
					"exception querying for Hive via sparql", e);
		}

	}

	/**
	 * @return the jargonHiveSparqlService
	 */
	public JenaHiveSPARQLService getJargonHiveSparqlService() {
		return jargonHiveSparqlService;
	}

	/**
	 * @param jargonHiveSparqlService
	 *            the jargonHiveSparqlService to set
	 */
	@Autowired
	@Required
	public void setJargonHiveSparqlService(
			JenaHiveSPARQLService jargonHiveSparqlService) {
		this.jargonHiveSparqlService = jargonHiveSparqlService;
	}

	/**
	 * 
	 */
	public GeneralSparqlQueryService() {
	}

}
