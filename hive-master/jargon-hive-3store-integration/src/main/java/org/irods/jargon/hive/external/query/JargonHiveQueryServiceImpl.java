package org.irods.jargon.hive.external.query;

import java.util.HashMap;
import java.util.Map;

import org.irods.jargon.hive.external.sparql.HiveQueryException;
import org.irods.jargon.hive.external.sparql.JenaHiveSPARQLService;
import org.irods.jargon.hive.external.sparql.JenaHiveSPARQLServiceImpl;
import org.irods.jargon.hive.external.utils.HiveException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.template.HiveTemplateException;
import org.irods.jargon.hive.external.utils.template.SPARQLTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support basic queries for HIVE-annotated data, this wraps the
 * {@link JenaHiveSPARQLService}, which allows direct execution of arbitrary
 * SPARQL queries.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JargonHiveQueryServiceImpl implements JargonHiveQueryService {

	public static final Logger log = LoggerFactory
			.getLogger(JargonHiveQueryServiceImpl.class);
	private final JenaHiveConfiguration jenaHiveConfiguration;
	private final JenaHiveSPARQLService jenaHiveSPARQLService;

	public static final String SPARQL_ALL_FOR_TERM = "/sparql-template/queryAllForTerm.txt";
	public static final String SPARQL_ALL_FOR_RELATED_TERM = "/sparql-template/queryAllForRelatedTerm.txt";

	public static final String TERM = "term";

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 * @param jenaHiveConfiguration
	 */
	public JargonHiveQueryServiceImpl(
			final JenaHiveConfiguration jenaHiveConfiguration)
			throws HiveException {

		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}

		this.jenaHiveConfiguration = jenaHiveConfiguration;
		jenaHiveSPARQLService = new JenaHiveSPARQLServiceImpl(
				jenaHiveConfiguration);
		jenaHiveSPARQLService.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.external.query.JargonHiveQueryService#queryForUri
	 * (java.lang.String)
	 */
	@Override
	public String queryForUri(final String vocabularyUri)
			throws HiveQueryException {

		log.info("queryForUri(final String vocabularyUri)");

		if (vocabularyUri == null || vocabularyUri.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabularyUri");
		}

		log.info("vocabularyUri:{}", vocabularyUri);
		String sparqlTemplate = SPARQL_ALL_FOR_TERM;
		return queryGivenTemplateAndMapReturnJSON(vocabularyUri, sparqlTemplate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.external.query.JargonHiveQueryService#
	 * queryForUriRelated(java.lang.String)
	 */
	@Override
	public String queryForUriRelated(final String vocabularyUri)
			throws HiveQueryException {

		log.info("queryForUriRelated(final String vocabularyUri)");

		if (vocabularyUri == null || vocabularyUri.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabularyUri");
		}

		log.info("vocabularyUri:{}", vocabularyUri);
		String sparqlTemplate = SPARQL_ALL_FOR_RELATED_TERM;
		return queryGivenTemplateAndMapReturnJSON(vocabularyUri, sparqlTemplate);
	}

	/**
	 * Do a query given a vocabulary URI and the appropriate sparql template
	 * expecting to substitute the term value
	 * 
	 * @param vocabularyUri
	 * @param sparqlTemplate
	 * @return
	 * @throws HiveQueryException
	 */
	private String queryGivenTemplateAndMapReturnJSON(
			final String vocabularyUri, final String sparqlTemplate)
			throws HiveQueryException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(TERM, vocabularyUri);
		try {
			String query = SPARQLTemplateUtils
					.getSPARQLTemplateAndSubstituteValues(sparqlTemplate,
							params);
			log.info("built query:{}", query);
			String json = jenaHiveSPARQLService
					.queryAndReturnJSONAsString(query);
			log.info("json data to reutnr:{}", json);
			return json;
		} catch (HiveTemplateException e) {
			throw new HiveQueryException(
					"error making query from sparql template", e);
		}
	}

	/**
	 * @return the jenaHiveConfiguration
	 */
	public JenaHiveConfiguration getJenaHiveConfiguration() {
		return jenaHiveConfiguration;
	}

}
