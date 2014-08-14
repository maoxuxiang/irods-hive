package org.irods.jargon.hive.query;

import java.util.List;

public class HiveQueryService {
	
	
	/*
	 * For sparql1.txt
	 */
	
	/*
	 * StringBuilder sparqlQuery = new StringBuilder();
	 * 
	 * sparqlQuery.append("PREFIX irods: <http://www.irods.org/ontologies/2013/2/iRODS.owl#>");
	 * sparqlQuery.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>");
	 * 
	 * sparqlQuery.append("SELECT ?x ?y ?webLink ?infoLink");
	 * 
	 * sparqlQuery.append("WHERE {");
	 * sparqlQuery.append("?x  irods:correspondingConcept ?y .");
	 * sparqlQuery.append("?y skos:related <http://www.fao.org/aos/agrovoc#c_28638> .");
	 * sparqlQuery.append("?x irods:hasDownloadLocation ?weblink .");
	 * sparqlQuery.append("?x irods:hasWebInformationLink ?infoLink}");
	 * 
	 * return sparqlQuery.toString();
	 * 
	 */
	
	public String buildSparql(HiveQuery hq) {
		StringBuilder sparqlQuery = new StringBuilder();
		List<HiveQueryVocabularyItem> queryVocabs = hq.getHiveQueryVocabularyItems();
		
		sparqlQuery.append("PREFIX irods: <http://www.irods.org/ontologies/2013/2/iRODS.owl#>");
		sparqlQuery.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>");
		
		sparqlQuery.append("SELECT ?x ?y ?webLink ?infoLink");
		sparqlQuery.append("WHERE {");
		sparqlQuery.append("?x  irods:correspondingConcept ?y .");
		
		for (HiveQueryVocabularyItem hqv: queryVocabs) {
			for (VocabularyItemSearchType st: hqv.getSearchTypes()) {
				sparqlQuery.append("?y skos:" + st + " " + hqv.getVocabularyTermURI());
			}
		}
		
		sparqlQuery.append("?x irods:hasDownloadLocation ?weblink .");
		sparqlQuery.append("?x irods:hasWebInformationLink ?infoLink}");
		
		return sparqlQuery.toString();
		
	}

}
