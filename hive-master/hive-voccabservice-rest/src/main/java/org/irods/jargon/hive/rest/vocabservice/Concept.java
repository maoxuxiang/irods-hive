package org.irods.jargon.hive.rest.vocabservice;

import java.util.ArrayList;
import java.util.List;

/**
 * value holder that contains the label, uri, vacb name, list from conceptproxy
 * @author Mao
 *
 */

public class Concept {
	
	private String vocabName = "";
	private String uri = "";
	private String label = "";
	private List<String> altLabel = new ArrayList<String>();
	private List<ConceptListEntry> narrower = new ArrayList<ConceptListEntry>();
	private List<ConceptListEntry> broader = new ArrayList<ConceptListEntry>();
	private List<ConceptListEntry> related = new ArrayList<ConceptListEntry>();
	
	public String getVocabName() {
		return vocabName;
	}
	public void setVocabName(String vocabName) {
		this.vocabName = vocabName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getAltLabel() {
		return altLabel;
	}
	public void setAltLabel(List<String> altLabel) {
		this.altLabel = altLabel;
	}
	public List<ConceptListEntry> getNarrower() {
		return narrower;
	}
	public void setNarrower(List<ConceptListEntry> narrower) {
		this.narrower = narrower;
	}
	public List<ConceptListEntry> getBroader() {
		return broader;
	}
	public void setBroader(List<ConceptListEntry> broader) {
		this.broader = broader;
	}
	public List<ConceptListEntry> getRelated() {
		return related;
	}
	public void setRelated(List<ConceptListEntry> related) {
		this.related = related;
	}

}
