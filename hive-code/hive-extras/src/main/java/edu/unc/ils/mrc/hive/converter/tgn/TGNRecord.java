package edu.unc.ils.mrc.hive.converter.tgn;

import java.util.ArrayList;
import java.util.List;

public class TGNRecord {

	private static final String URI_BASE = "http://tgn.getty.edu/tgn#";

	private String uri;
	private String subjectID;
	private String preferredTerm;
	private List<String> altTerm;
	private List<String> broderTerms;
	private List<String> narrowerTerms;
	private List<String> relatedTerms;
	private String scopeNote;

	public TGNRecord(final String subjectID) {
		this.subjectID = subjectID;
		uri = URI_BASE + subjectID;
		altTerm = new ArrayList<String>();
		broderTerms = new ArrayList<String>();
		narrowerTerms = new ArrayList<String>();
		relatedTerms = new ArrayList<String>();
	}

	public String getURIBASE() {
		return URI_BASE;
	}

	public String getSubjectID() {
		return subjectID;
	}

	public String getUri() {
		return uri;
	}

	public String getPreferredTerm() {
		return preferredTerm;
	}

	public void setPreferredTerm(final String preferredTerm) {
		this.preferredTerm = preferredTerm.replaceAll("&", "and");
	}

	public List<String> getAltTerm() {
		return altTerm;
	}

	public void setAltTerm(final String altTerm) {
		this.altTerm.add(altTerm.replaceAll("&", "and"));
	}

	public List<String> getBroderTerms() {
		return broderTerms;
	}

	public void setBroderTerms(final String broderTerm) {
		broderTerms.add(URI_BASE + broderTerm);
	}

	public List<String> getNarrowerTerms() {
		return narrowerTerms;
	}

	public void setNarrowerTerms(final String narrowerTerm) {
		narrowerTerms.add(URI_BASE + narrowerTerm);
	}

	public List<String> getRelatedTerms() {
		return relatedTerms;
	}

	public void setRelatedTerms(final String relatedTerm) {
		relatedTerms.add(URI_BASE + relatedTerm);
	}

	public String getScopeNote() {
		return scopeNote;
	}

	public void setScopeNote(final String scopeNote) {
		this.scopeNote = scopeNote.replaceAll("&", "and");
	}

}
