package org.irods.jargon.hive.irods;

import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;

/**
 * Represents a HIVE vocabulary term as applied to an iRODS collection or data
 * object. This contains the information necessary to create an RDF statement
 * about the
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveVocabularyEntry {

	/**
	 * Indicates which type of metadata this term applies to (e.g. collection,
	 * data object)
	 */
	private MetadataDomain metadataDomain;

	/**
	 * Unique logical name for the iRODS object that this annotation applies
	 * to. For colletions and data objects this is this iRODS absoulute path.
	 */
	private String domainObjectUniqueName = "";

	/**
	 * The HIVE name for the vocabulary
	 */
	private String vocabularyName = "";

	/**
	 * The human-readable label for the term
	 */
	private String preferredLabel = "";

	/**
	 * The rdf URI for the term
	 */
	private String termURI = "";

	/**
	 * User supplied annotation on the term
	 */
	private String comment = "";

	/**
	 * An optional (may not be set by all methods, may be null) cache of the
	 * underlying AVU that represets this vocabulary entry
	 */
	private MetaDataAndDomainData metadataAndDomainData = null;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HiveVocabularyEntry:");
		sb.append("\n\t metadataDomain:");
		sb.append(metadataDomain);
		sb.append("\n\t domainObjectUniqueName:");
		sb.append(domainObjectUniqueName);
		sb.append("\n\t vocabularyName:");
		sb.append(vocabularyName);
		sb.append("\n\t preferredLabel:");
		sb.append(preferredLabel);
		sb.append("\n\t termURI:");
		sb.append(termURI);
		sb.append("\n\t comment:");
		sb.append(comment);
		return sb.toString();
	}

	/**
	 * 
	 */
	public HiveVocabularyEntry() {
	}

	public MetadataDomain getMetadataDomain() {
		return metadataDomain;
	}

	public void setMetadataDomain(final MetadataDomain metadataDomain) {
		this.metadataDomain = metadataDomain;
	}

	public String getDomainObjectUniqueName() {
		return domainObjectUniqueName;
	}

	public void setDomainObjectUniqueName(final String domainObjectUniqueName) {
		this.domainObjectUniqueName = domainObjectUniqueName;
	}

	public String getVocabularyName() {
		return vocabularyName;
	}

	public void setVocabularyName(final String vocabularyName) {
		this.vocabularyName = vocabularyName;
	}

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(final String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public String getTermURI() {
		return termURI;
	}

	public void setTermURI(final String termURI) {
		this.termURI = termURI;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public MetaDataAndDomainData getMetadataAndDomainData() {
		return metadataAndDomainData;
	}

	public void setMetadataAndDomainData(
			final MetaDataAndDomainData metadataAndDomainData) {
		this.metadataAndDomainData = metadataAndDomainData;
	}

}
