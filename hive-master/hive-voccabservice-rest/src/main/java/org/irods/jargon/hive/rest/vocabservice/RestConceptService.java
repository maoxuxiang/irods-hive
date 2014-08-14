/**
 * 
 */
package org.irods.jargon.hive.rest.vocabservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.irods.jargon.hive.exception.JargonHiveException;
import org.irods.jargon.hive.service.VocabularyService;
import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.unc.hive.client.ConceptProxy;

/**
 * REST interface for interacting with concepts
 * 
 * @author Mike Conway - DICE
 * 
 */
@Named
@Path("/concept")
public class RestConceptService {

	/**
	 * Injected dependency on the HIVE vocabulary service
	 */
	private VocabularyService vocabularyService;

	public static final Logger log = LoggerFactory
			.getLogger(RestConceptService.class);

	public VocabularyService getVocabularyService() {
		return vocabularyService;
	}

	@Autowired
	public void setVocabularyService(final VocabularyService vocabularyService) {
		this.vocabularyService = vocabularyService;
	}

	/*
	 * vocaulary/broader?uri=blah get vocab and get the broader, retrun a
	 * List<ConceptListEntry> of the broader terms *vocaulary/narrower?uri=blah
	 * get vocab and get the broader, retrun a List<ConceptListEntry> of the
	 * narrower terms
	 * 
	 * *vocaulary/related?uri=blah get vocab and get the broader, retrun a
	 * List<ConceptListEntry> of the related terms
	 */

	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<Concept> getConcept(

	@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		List<ConceptProxy> concept = vocabularyService.getSubTopConcept(vocabulary, "", true);
		
		List<Concept> result = new ArrayList<Concept>();
		
		for (ConceptProxy c : concept) {
			Concept result1 = new Concept();
			
			result1.setBroader(convert(c.getBroader()));
			result1.setNarrower(convert(c.getNarrower()));
			result1.setRelated(convert(c.getRelated()));
			result1.setAltLabel(c.getAltLabel());
			result1.setLabel(c.getPreLabel());
			result1.setUri(c.getURI());
			result1.setVocabName(vocabulary);
			
			result.add(result1);
		}
		return result;
	}

	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<Concept> getConceptQueries(
			@QueryParam("letter") final String letter,
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		List<ConceptProxy> concept = vocabularyService.getSubTopConcept(vocabulary, letter, true);
		
		
		List<Concept> result = new ArrayList<Concept>();
		
		for (ConceptProxy c : concept) {
			Concept result1 = new Concept();
			
			result1.setBroader(convert(c.getBroader()));
			result1.setNarrower(convert(c.getNarrower()));
			result1.setRelated(convert(c.getRelated()));
			result1.setAltLabel(c.getAltLabel());
			result1.setLabel(c.getPreLabel());
			result1.setUri(c.getURI());
			result1.setVocabName(vocabulary);
			
			result.add(result1);
		}
		return result;
		
	}

	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })

	public Concept findConceptByUri (
			@QueryParam("uri") final String uri,
	
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		URI uriString = null;
		try {
			uriString = new URI(uri);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
			throw new JargonHiveException("error creating URI", e);
		}
		String namespaceURI = uriString.getScheme() + "://"
				+ uriString.getHost() + uriString.getPath();
		String localPart = "#" + uriString.getFragment();
		
		ConceptProxy concept = vocabularyService.getConceptByURI(namespaceURI, localPart);
		Concept result = new Concept();
		
		result.setBroader(findConceptBroaderByUri(uri, vocabulary));
		result.setNarrower(findConceptNarrowerByUri(uri, vocabulary));
		result.setRelated(findConceptRelatedByUri(uri, vocabulary));
		result.setAltLabel(concept.getAltLabel());
		result.setLabel(concept.getPreLabel());
		result.setUri(concept.getURI());
		result.setVocabName(vocabulary);
		
		return result;
		
	}

	@GET
	@Path("{vocabulary}/broader")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptListEntry> findConceptBroaderByUri(
			@QueryParam("uri") final String uri,
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		URI uriString = null;
		try {
			uriString = new URI(uri);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
			throw new JargonHiveException("error creating URI", e);
		}
		String namespaceURI = uriString.getScheme() + "://"
				+ uriString.getHost() + uriString.getPath();

		String localPart = "#" + uriString.getFragment();

		ConceptProxy concept = vocabularyService.getConceptByURI(namespaceURI,
				localPart);
		List<ConceptListEntry> result = new ArrayList<ConceptListEntry>();
		Map<String, String> broader = concept.getBroader();

		Iterator<String> it = broader.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String v = broader.get(key);
			ConceptListEntry listEntry = new ConceptListEntry(key, v);
			result.add(listEntry);
		}

		return result;
	}
	
	private List<ConceptListEntry> convert(Map<String, String> map) {
		
		List<ConceptListEntry> result = new ArrayList<ConceptListEntry>();
		Map<String, String> convert = map;

		Iterator<String> it = convert.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String v = convert.get(key);
			ConceptListEntry listEntry = new ConceptListEntry(key, v);
			result.add(listEntry);
		}

		return result;
	}
	


	@GET
	@Path("{vocabulary}/narrower")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptListEntry> findConceptNarrowerByUri(
			@QueryParam("uri") final String uri,
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		URI uriString = null;
		try {
			uriString = new URI(uri);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
			throw new JargonHiveException("error creating URI", e);

		}
		String namespaceURI = uriString.getScheme() + "://"
				+ uriString.getHost() + uriString.getPath();

		String localPart = "#" + uriString.getFragment();

		ConceptProxy concept = vocabularyService.getConceptByURI(namespaceURI,
				localPart);
		List<ConceptListEntry> result = new ArrayList<ConceptListEntry>();
		Map<String, String> broader = concept.getBroader();

		Iterator<String> it = broader.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String v = broader.get(key);
			ConceptListEntry listEntry = new ConceptListEntry(key, v);
			result.add(listEntry);
		}

		return result;
	}

	@GET
	@Path("{vocabulary}/related")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptListEntry> findConceptRelatedByUri(
			@QueryParam("uri") final String uri,
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		URI uriString = null;
		try {
			uriString = new URI(uri);

		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
			throw new JargonHiveException("error creating URI", e);

		}
		String namespaceURI = uriString.getScheme() + "://"
				+ uriString.getHost() + uriString.getPath();

		String localPart = "#" + uriString.getFragment();

		ConceptProxy concept = vocabularyService.getConceptByURI(namespaceURI,
				localPart);
		List<ConceptListEntry> result = new ArrayList<ConceptListEntry>();
		Map<String, String> broader = concept.getBroader();

		Iterator<String> it = broader.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String v = broader.get(key);
			ConceptListEntry listEntry = new ConceptListEntry(key, v);
			result.add(listEntry);
		}

		return result;
	}

}
