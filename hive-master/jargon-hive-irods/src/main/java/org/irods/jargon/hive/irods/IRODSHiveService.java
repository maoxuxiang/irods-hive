package org.irods.jargon.hive.irods;

import java.util.List;

import org.irods.jargon.core.exception.FileNotFoundException;
import org.irods.jargon.hive.irods.exception.IRODSHiveException;

/**
 * Service interface to access and update HIVE vocabulary terms as iRODS AVUs.
 * <p/>
 * Note that HIVE vocabulary terms are serialized using a standard AVU format
 * with a special unit of iRODSUserTagging:HIVE:VocabularyTerm. This identifies
 * HIVE terms that have been applied to a data object, collection, or other iCAT
 * entity.
 * <p/>
 * The AVU attribute will hold the RDF URI that points to the term. The value of
 * the AVU will hold a | delim set of data in the form:
 * vocabulary=XXXX|preferredLabel=XXXXXX|comment=XXXXX
 * <p/>
 * This format allows queries for things like finding applied terms by
 * vocabulary. One could say 'find avus with a value like %vocabulary=XXX%'
 * While not ideal, this does give a bit more flexibility.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface IRODSHiveService {

	public static final String VOCABULARY_AVU_UNIT = "iRODSUserTagging:HIVE:VocabularyTerm";
	public static final String DELIM = "|";
	public static final String VOCABULARY_DELIM = "vocabulary=";
	public static final String LABEL_DELIM = "preferredLabel=";
	public static final String COMMENT_DELIM = "comment=";

	/**
	 * Save or modify a vocabulary term applied to this file or collection.
	 * 
	 * @param hiveVocabularyEntry
	 *            {@link HiveVocabularyEntry} reflecting the term to be applied
	 * @throws FileNotFoundException
	 *             if the iRODS file or collection is missing
	 * @throws IRODSHiveException
	 */
	void saveOrUpdateVocabularyTerm(HiveVocabularyEntry hiveVocabularyEntry)
			throws FileNotFoundException, IRODSHiveException;

	/**
	 * Check the contents of the <code>HiveVocabularyEntry</code> for
	 * completeness (presence of term, URI, vocabulary, etc)
	 * 
	 * @param hiveVocabularyEntry
	 *            {@link HiveVocabularyEntry} to be validated
	 * @throws IRODSHiveException
	 *             if there are validation errors
	 */
	void validateHiveVocabularyEntry(HiveVocabularyEntry hiveVocabularyEntry)
			throws IRODSHiveException;

	/**
	 * Given an absolute path to an iRODS file or collecion, list the applied
	 * vocabulary terms
	 * 
	 * @param irodsAbsolutePath
	 *            <code>String</code> with an absolute path to an iRODS file or
	 *            collection
	 * @return <code>List</code> of {@link HiveVocabularyEntry} for the iRODS
	 *         absolute path. This will be an empty list if no terms are applied
	 * @throws FileNotFoundException
	 *             if the iRODS file or collection does not exist
	 * @throws IRODSHiveException
	 */
	List<HiveVocabularyEntry> listVocabularyTermsForIRODSAbsolutePath(
			String irodsAbsolutePath) throws FileNotFoundException,
			IRODSHiveException;

	/**
	 * Given an iRODS absolute path to a file or collection, and a URI that
	 * represents a term in a SKOS vocabulary, find the stored vocabulary term.
	 * This will return <code>null</code> if no term is available
	 * 
	 * @param irodsAbsolutePath
	 *            <code>String</code> with an absolute path to an iRODS file or
	 *            collection
	 * @param vocabularyTermURI
	 *            <code>String</code> with a URI that represents the SKOS term
	 * @return {@link HiveVocabularyTerm} applied in iRODS, or <code>null</code>
	 * @throws FileNotFoundException
	 * @throws IRODSHiveException
	 */
	HiveVocabularyEntry findHiveVocabularyEntryForPathAndURI(
			final String irodsAbsolutePath, final String vocabularyTermURI)
			throws FileNotFoundException, IRODSHiveException;

	/**
	 * Remove a HIVE term from an iRODS file or collection. Note that if the
	 * term is not applied, this will be silently ignored
	 * 
	 * @param irodsAbsolutePath
	 *            h <code>String</code> with an absolute path to an iRODS file
	 *            or collection
	 * @param vocabularyTermURI
	 *            <code>String</code> with a URI that represents the SKOS term
	 *            to be deleted
	 * @throws FileNotFoundException
	 *             thrown if the iRODS file or collection is not found
	 * @throws IRODSHiveException
	 */
	void deleteVocabularyEntryForPathAndURI(String irodsAbsolutePath,
			String vocabularyTermURI) throws FileNotFoundException,
			IRODSHiveException;

}