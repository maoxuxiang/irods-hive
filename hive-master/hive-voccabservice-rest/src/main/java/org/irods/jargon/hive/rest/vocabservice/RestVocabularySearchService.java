package org.irods.jargon.hive.rest.vocabservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.irods.jargon.hive.service.VocabularyService;
import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.unc.hive.client.ConceptProxy;

/**
 * Service for searching for vocabulary terms
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
@Named
@Path("/search")
public class RestVocabularySearchService {

	/**
	 * Injected dependency on the HIVE vocabulary service
	 */
	private VocabularyService vocabularyService;

	public static final Logger log = LoggerFactory
			.getLogger(RestVocabularySearchService.class);

	/**
	 * @return the vocabularyService
	 */
	public VocabularyService getVocabularyService() {
		return vocabularyService;
	}

	/**
	 * @param vocabularyService
	 *            the vocabularyService to set
	 */
	@Autowired
	public void setVocabularyService(final VocabularyService vocabularyService) {
		this.vocabularyService = vocabularyService;
	}

	/*
	 * this will be a GET under /search with 2 parametsrs,
	 * ?searchTerm=xxxxx&vocabs=uat,agrovoc,blah
	 * 
	 * Add a method take param = search term (like giant or star in uat) this is
	 * actually the label that was indexed in lucene take param = vocabs like
	 * uat,agrovoc
	 * 
	 * look at String.split() in Java API
	 * 
	 * call vocab service with
	 * 
	 * 
	 * Set<ConceptProxy> searchConcept(String keyword, List<String>
	 * openedVocabularies);
	 * 
	 * 
	 * key word is search term openedVocabularies is the vocabas that are turned
	 * from vocab,vocab to a list<String>
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public Set<ConceptProxy> searchConceptByTermAndVocabs(
			@QueryParam("searchTerm") final String searchTerm,
			@QueryParam("vocabs") final String vocabs) {
		// log.info("getVocabularySearch()");

		String keyword = searchTerm;
		String[] temp = vocabs.split(",");

		List<String> vocab = new ArrayList<String>();
		for (String s : temp) {
			vocab.add(s);
		}
		// System.out.println(vocab);
		Set<ConceptProxy> result = vocabularyService.searchConcept(keyword,
				vocab);

		return result;

	}

}
