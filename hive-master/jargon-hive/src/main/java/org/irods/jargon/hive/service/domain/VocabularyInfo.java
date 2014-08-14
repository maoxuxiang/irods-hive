/**
 * 
 */
package org.irods.jargon.hive.service.domain;

import java.util.Date;

/**
 * Represents basic info about a vocabulary
 * 
 * @author Mike Conway - DICE
 * 
 */
public class VocabularyInfo {

	private String vocabularyName = "";
	private long numberOfConcepts = 0;
	private long numberOfRelations = 0;
	private Date lastUpdated = null;

	public String getVocabularyName() {
		return vocabularyName;
	}

	public void setVocabularyName(String vocabularyName) {
		this.vocabularyName = vocabularyName;
	}

	public long getNumberOfConcepts() {
		return numberOfConcepts;
	}

	public void setNumberOfConcepts(long numberOfConcepts) {
		this.numberOfConcepts = numberOfConcepts;
	}

	public long getNumberOfRelations() {
		return numberOfRelations;
	}

	public void setNumberOfRelations(long numberOfRelations) {
		this.numberOfRelations = numberOfRelations;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "VocabularyInfo ["
				+ (vocabularyName != null ? "vocabularyName=" + vocabularyName
						+ ", " : "") + "numberOfConcepts=" + numberOfConcepts
				+ ", numberOfRelations=" + numberOfRelations + ", "
				+ (lastUpdated != null ? "lastUpdated=" + lastUpdated : "")
				+ "]";
	}

}
