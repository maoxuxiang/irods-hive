package edu.unc.ils.mrc.hive.converter.embne;

import java.util.HashSet;
import java.util.Set;

public class SKOSConcept {

	String descriptorId = "";
	Set<String> related = new HashSet<String>();
	Set<String> broader = new HashSet<String>();
	Set<String> narrower = new HashSet<String>();
	String conceptScheme = "";
	String prefLabel = "";
	Set<String> altLabels = new HashSet<String>();
	String scopeNote = "";

	public void addNarrower(final String value) {
		narrower.add(value);
	}

	public void addBroader(final String value) {
		broader.add(value);
	}

	public void addRelated(final String value) {
		// related.put(descriptorId, "1");
		related.add(value);
	}

	public void addAltLabel(final String term) {
		altLabels.add(term);
	}

	public String getDescriptorId() {
		return descriptorId;
	}

	public void setDescriptorId(final String descriptorId) {
		this.descriptorId = descriptorId;
	}

	public Set<String> getNarrower() {
		return narrower;
	}

	public Set<String> getBroader() {
		return broader;
	}

	public Set<String> getRelated() {
		return related;
	}

	public String getPrefLabel() {
		return prefLabel;
	}

	public String getConceptScheme() {
		return conceptScheme;
	}

	public void setPrefLabel(final String prefLabel) {
		this.prefLabel = prefLabel;
	}

	public void setConceptScheme(final String scheme) {
		conceptScheme = scheme;
	}

	public Set<String> getAltLabels() {
		return altLabels;
	}

	public String getScopeNote() {
		return scopeNote;
	}

	public void setScopeNote(final String scopeNote) {
		this.scopeNote = scopeNote;
	}

}
