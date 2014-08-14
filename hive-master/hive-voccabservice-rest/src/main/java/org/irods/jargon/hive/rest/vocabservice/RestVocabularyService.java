/**
 * 
 */
package org.irods.jargon.hive.rest.vocabservice;

import java.util.List;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.irods.jargon.hive.exception.JargonHiveException;
import org.irods.jargon.hive.service.VocabularyService;
import org.irods.jargon.hive.service.domain.VocabularyInfo;
import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.unc.ils.mrc.hive.api.SKOSScheme;

/**
 * REST wrapper for vocabulary service allowing query of HIVE terms and
 * navigation of stored vocabularies
 * 
 * @author Mike Conway
 * 
 */
@Named
@Path("/vocabulary")
public class RestVocabularyService {
	/**
	 * Injected dependency on the HIVE vocabulary service
	 */
	private VocabularyService vocabularyService;

	public static final Logger log = LoggerFactory
			.getLogger(RestVocabularyService.class);

	public VocabularyService getVocabularyService() {
		return vocabularyService;
	}

	@Autowired
	public void setVocabularyService(final VocabularyService vocabularyService) {
		this.vocabularyService = vocabularyService;
	}

	/**
	 * respond to a get request and return a list of vocabularies
	 * 
	 * @return <code>List<String></code> with vocabulary names in the HIVE
	 * @throws JargonHiveException
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<VocabularyInfo> getVocabularies() throws JargonHiveException {
		log.info("getVocabularies()");
		return vocabularyService.getAllVocabularies();

	}
	
	// localhost:8080/vocabservice/vocabulary/agrovoc

	/**
	 * create a method that gives vocabularyInfo for a specific vocabulary
	 * 
	 * 1) set up the annotations so that it responds to a GET to
	 * /vocablary/vocabnamehere
	 * 
	 * 
	 * 2) call vocabularyService (SKOSScheme getVocabularyByName(String
	 * vocabularyName) throws VocabularyNotFoundException;)
	 * 
	 * 
	 * 3) build a VocabularyInfo from the data in SkosScheme
	 * 
	 * 4) write a unit test for this
	 * 
	 * 
	 * 
	 * @param vocabularyName
	 * @return
	 * @throws JargonHiveException
	 */

	@GET
	// @Path something - what's the mapping for a variable in the path? Check
	// here
	// http://docs.jboss.org/resteasy/docs/3.0.7.Final/userguide/html_single/index.html#_PathParam
	@Path("/{vocabularyName}")
	public VocabularyInfo getVocabulary(@PathParam("vocabularyName") final String vocabularyName) // @PathParam?
			throws JargonHiveException {

		// get the skosscheme from vocab service
		// create VocabularyInfo object from data in skosscheme and return
		
		SKOSScheme vocabularySKOSS  = vocabularyService.getVocabularyByName(vocabularyName);
		VocabularyInfo vocabularyInfo = new VocabularyInfo();
		
		vocabularyInfo.setVocabularyName(vocabularySKOSS.getName());
		vocabularyInfo.setNumberOfConcepts(vocabularySKOSS.getNumberOfConcepts());
		vocabularyInfo.setNumberOfRelations(vocabularySKOSS.getNumberOfRelations());
		vocabularyInfo.setLastUpdated(vocabularySKOSS.getLastUpdateDate());
		
		return vocabularyInfo; 

	}

	
	



}
