/**
 * 
 */
package org.irods.jargon.hive.rest.vocabservice;

/**
 * value holder that contains the label and uri from conceptproxy
 * @author Mao
 *
 */
public class ConceptListEntry {
	
	private String label = "";
	private String uri = "";
	
	public ConceptListEntry(String uri, String label) {
		this.label = label;
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

}
