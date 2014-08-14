/**
 * 
 */
package org.irods.jargon.hive.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item in the query
 * 
 * @author Le Zhang
 *
 */
public class HiveQueryVocabularyItem implements Serializable {
	
	private String vocabularyName = "";
	private String vocabularyTermURI = "";
	private String preferredLabel = "";
	
	
    public enum ConnectorTypeEnum {AND,OR}
	
	private ConnectorTypeEnum connectorType = ConnectorTypeEnum.AND;
	
	private List<VocabularyItemSearchType> searchTypes = new ArrayList<VocabularyItemSearchType>();
	
//	public HiveQueryVocabularyItem(String vocabName, String termUri, String preLabel) {
//		this.vocabularyName = vocabName;
//		this.vocaublaryTermURI = termUri;
//		this.preferredLabel = preLabel;
//	}
	
	public HiveQueryVocabularyItem() {
		
	}
	
	public String getVocabularyName() {
		return vocabularyName;
	}

	public void setVocabularyName(String vocabularyName) {
		this.vocabularyName = vocabularyName;
	}

	public String getVocabularyTermURI() {
		return vocabularyTermURI;
	}

	public void setVocabularyTermURI(String vocabularyTermURI) {
		this.vocabularyTermURI = vocabularyTermURI;
	}

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public ConnectorTypeEnum getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(ConnectorTypeEnum connectorType) {
		this.connectorType = connectorType;
	}

	public List<VocabularyItemSearchType> getSearchTypes() {
		return searchTypes;
	}

	public void setSearchTypes(List<VocabularyItemSearchType> searchTypes) {
		this.searchTypes = searchTypes;
	}



	

}
