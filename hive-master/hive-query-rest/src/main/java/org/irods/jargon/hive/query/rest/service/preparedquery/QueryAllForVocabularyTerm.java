/**
 * 
 */
package org.irods.jargon.hive.query.rest.service.preparedquery;

import org.irods.jargon.hive.external.query.JargonHiveQueryService;
import org.irods.jargon.hive.external.sparql.HiveQueryException;
import org.irods.jargon.hive.query.rest.service.HiveQueryServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for query of all by vocabulary term, queries all files and
 * collections that have the associated term
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
@Controller
public class QueryAllForVocabularyTerm {

	private JargonHiveQueryService jargonHiveQueryService;

	public static final Logger log = LoggerFactory
			.getLogger(QueryAllForVocabularyTerm.class);

	/**
	 * @param jargonHiveQueryService
	 *            the jargonHiveQueryService to set
	 */
	@Autowired
	@Required
	public void setJargonHiveQueryService(
			JargonHiveQueryService jargonHiveQueryService) {
		this.jargonHiveQueryService = jargonHiveQueryService;
	}

	/**
	 * Use the canned query all for vocabulary term to query all files and
	 * collections, returning JSON data from Jena
	 * 
	 * example: http://localhost:8080/hive-query-rest/preparedQuery/
	 * allForVocabularyTerm
	 * ?vocabUri=http://www.fao.org/aos/agrovoc&termId=c_1669
	 * 
	 * @param vocabularyUri
	 *            <code>String</code> with the URI that describes the
	 *            vocabulary, this will be the part up the the # anchor, so in
	 *            the example it would be http://ww.fao.org/aos/agrovoc
	 * @param termId
	 *            <code>String</code> with the 'anchor' part of the term, so in
	 *            the example this would be c_1669
	 * @return <code>String</code> containing JSON data from the triple store
	 * @throws HiveQueryServiceException
	 * 
	 * 
	 * 
	 * 
	 */
	@RequestMapping(value = "/preparedQuery/allForVocabularyTerm", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String queryForAllByVocabularyTerm(
			@RequestParam("vocabUri") final String vocabUri,
			@RequestParam("termId") final String termId)
			throws HiveQueryServiceException {

		log.info("queryForAllByVocabularyTerm");

		if (vocabUri == null || vocabUri.isEmpty()) {
			throw new HiveQueryServiceException("null or empty vocabUri ");
		}

		if (termId == null || termId.isEmpty()) {
			throw new HiveQueryServiceException("null or empty termId ");
		}

		log.info("vocabUri:{}", vocabUri);
		log.info("termId:{}", termId);

		StringBuilder sb = new StringBuilder();
		sb.append(vocabUri);
		sb.append('#');
		sb.append(termId);

		try {
			return jargonHiveQueryService.queryForUri(sb.toString());
		} catch (HiveQueryException e) {
			log.error("HiveQueryException querying for term", e);
			throw new HiveQueryServiceException(
					"exception querying for Hive vocabulary term", e);
		}

	}

}
