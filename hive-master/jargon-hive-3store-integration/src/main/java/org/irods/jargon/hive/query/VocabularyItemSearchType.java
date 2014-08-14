/**
 * 
 */
package org.irods.jargon.hive.query;

import java.io.Serializable;

/**
 * Represents the type of query for the item (directly applied, related, broader, etc)
 * @author Le
 *
 */
public class VocabularyItemSearchType implements Serializable {
	
	public enum SearchType { EXACT, RELATED, BROADER, NARROWER}
	
	private SearchType searchType = SearchType.EXACT;

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

}
