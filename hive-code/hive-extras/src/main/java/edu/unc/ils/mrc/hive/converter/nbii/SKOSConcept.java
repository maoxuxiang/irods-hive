package edu.unc.ils.mrc.hive.converter.nbii;

import java.util.ArrayList;
import java.util.List;

public class SKOSConcept implements Concept {

	private String uri;
	private String prefLabel;
	private String scopeNote;
	private List<String> altLabel;
	private List<String> hiddenLabel;
	private List<String> narrower;
	private List<String> narrowerURI;
	private List<String> broader;
	private List<String> broaderURI;
	private List<String> related;
	private List<String> realtedURI;

	public SKOSConcept(final String uri) {
		this.uri = uri;
		narrower = new ArrayList<String>();
		narrowerURI = new ArrayList<String>();
		broader = new ArrayList<String>();
		broaderURI = new ArrayList<String>();
		altLabel = new ArrayList<String>();
		hiddenLabel = new ArrayList<String>();
		related = new ArrayList<String>();
		realtedURI = new ArrayList<String>();
		scopeNote = "";
	}

	@Override
	public void setUri(final String uri) {
		this.uri = uri;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getPrefLabel() {
		return prefLabel;
	}

	@Override
	public void setPrefLabel(final String prefLabel) {
		this.prefLabel = prefLabel;
	}

	@Override
	public String getScopeNote() {
		return scopeNote;
	}

	@Override
	public void setScopeNote(final String scopeNote) {
		this.scopeNote = this.scopeNote.concat(" " + scopeNote);
	}

	@Override
	public List<String> getAltLabel() {
		return altLabel;
	}

	@Override
	public void setAltLabel(final String altLabel) {
		this.altLabel.add(altLabel);
	}

	@Override
	public List<String> getHiddenLabel() {
		return hiddenLabel;
	}

	@Override
	public void setHiddenLabel(final String hiddenLabel) {
		this.hiddenLabel.add(hiddenLabel);
	}

	@Override
	public List<String> getNarrower() {
		return narrower;
	}

	@Override
	public List<String> getNarrowerURI() {
		return narrowerURI;
	}

	@Override
	public void setNarrower(final String narrower) {
		this.narrower.add(narrower);
	}

	@Override
	public void setNarrowerURI(final String narrowerURI) {
		this.narrowerURI.add(narrowerURI);
	}

	@Override
	public List<String> getBroader() {
		return broader;
	}

	@Override
	public List<String> getBroaderURI() {
		return broaderURI;
	}

	@Override
	public void setBroader(final String broader) {
		this.broader.add(broader);
	}

	@Override
	public void setBroaderURI(final String broaderURI) {
		this.broaderURI.add(broaderURI);
	}

	@Override
	public List<String> getRelated() {
		return related;
	}

	@Override
	public List<String> getRelatedURI() {
		return realtedURI;
	}

	@Override
	public void setRelated(final String related) {
		this.related.add(related);
	}

	@Override
	public void setRelatedURI(final String relatedURI) {
		realtedURI.add(relatedURI);
	}

}
