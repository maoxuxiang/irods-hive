package org.irods.jargon.hive.irods;

import java.util.ArrayList;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.DataNotFoundException;
import org.irods.jargon.core.exception.DuplicateDataException;
import org.irods.jargon.core.exception.FileNotFoundException;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.CollectionAndDataObjectListAndSearchAO;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.domain.AvuData;
import org.irods.jargon.core.pub.domain.ObjStat;
import org.irods.jargon.core.query.AVUQueryElement;
import org.irods.jargon.core.query.AVUQueryElement.AVUQueryPart;
import org.irods.jargon.core.query.AVUQueryOperatorEnum;
import org.irods.jargon.core.query.JargonQueryException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.hive.irods.exception.IRODSHiveException;
import org.irods.jargon.usertagging.AbstractIRODSTaggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Services to acces and update HIVE vocabulary terms as iRODS AVUs.
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
public class IRODSHiveServiceImpl extends AbstractIRODSTaggingService implements
		IRODSHiveService {

	public static final Logger log = LoggerFactory
			.getLogger(IRODSHiveServiceImpl.class);

	private CollectionAndDataObjectListAndSearchAO collectionAndDataObjectListAndSearchAO = null;
	private CollectionAO collectionAO = null;
	private DataObjectAO dataObjectAO = null;

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 * @throws JargonException
	 */
	public IRODSHiveServiceImpl(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount) throws IRODSHiveException {
		super(irodsAccessObjectFactory, irodsAccount);

		try {
			collectionAndDataObjectListAndSearchAO = irodsAccessObjectFactory
					.getCollectionAndDataObjectListAndSearchAO(irodsAccount);
			collectionAO = irodsAccessObjectFactory
					.getCollectionAO(irodsAccount);
			dataObjectAO = irodsAccessObjectFactory
					.getDataObjectAO(irodsAccount);
		} catch (JargonException e) {
			log.error("exception getting necessary access objects", e);
			throw new IRODSHiveException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.irods.IRODSHiveService#
	 * deleteVocbularyEntryForPathAndURI(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteVocabularyEntryForPathAndURI(
			final String irodsAbsolutePath, final String vocabularyTermURI)
			throws FileNotFoundException, IRODSHiveException {

		log.info("deleteVocabularyEntryForPathAndURI()");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException(
					"null or emtpy irodsAbsolutePath");
		}

		if (vocabularyTermURI == null || vocabularyTermURI.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty vocabularyTermURI");
		}

		log.info("irodsAbsolutePath:{}", irodsAbsolutePath);
		log.info("vocabularyTermURI:{}", vocabularyTermURI);
		HiveVocabularyEntry entry = findHiveVocabularyEntryForPathAndURI(
				irodsAbsolutePath, vocabularyTermURI);
		if (entry == null) {
			log.info("no data to delete, silently ignore");
			return;
		}

		log.info("found term to delete...delete the avu...");

		AvuData avuData = translateHiveEntryIntoAvu(entry);

		try {

			ObjStat objStat = collectionAndDataObjectListAndSearchAO
					.retrieveObjectStatForPath(irodsAbsolutePath);

			if (objStat.isSomeTypeOfCollection()) {
				log.info("is a collection");
				collectionAO.deleteAVUMetadata(irodsAbsolutePath, avuData);
			} else {
				log.info("is a data object");
				dataObjectAO.deleteAVUMetadata(irodsAbsolutePath, avuData);
			}

			log.info("metadata deleted");

		} catch (FileNotFoundException fnf) {
			log.error("file not found for:{}", irodsAbsolutePath);
			throw fnf;
		} catch (JargonException e) {
			log.error("exception getting metadata", e);
			throw new IRODSHiveException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.irods.IRODSHiveService#saveOrUpdateVocabularyTerm
	 * (org.irods.jargon.hive.irods.HiveVocabularyEntry)
	 */
	@Override
	public void saveOrUpdateVocabularyTerm(
			final HiveVocabularyEntry hiveVocabularyEntry)
			throws FileNotFoundException, IRODSHiveException {
		log.info("saveOrUpdateVocabularyTerm()");

		if (hiveVocabularyEntry == null) {
			throw new IllegalArgumentException(
					"null or empty hiveVocabularyEntry");
		}

		log.info("validating entry...");
		validateHiveVocabularyEntry(hiveVocabularyEntry);
		log.info("...validated, look up metdata if it is already there");
		HiveVocabularyEntry currentEntry = findHiveVocabularyEntryForPathAndURI(
				hiveVocabularyEntry.getDomainObjectUniqueName(),
				hiveVocabularyEntry.getTermURI());

		try {

			if (currentEntry == null) {
				addMetadataAsNew(hiveVocabularyEntry);
			} else {
				modifyMetadataAsUpdate(hiveVocabularyEntry, currentEntry);
			}
		} catch (DataNotFoundException dnf) {
			log.info(
					"data not found exception translated to FileNotFoundException for consistency",
					dnf);
			throw new FileNotFoundException(
					"did not find file while saving metadata", dnf);
		} catch (JargonException je) {
			log.error("JargonException adding avu metadata", je);
			throw new IRODSHiveException(je);
		}

	}

	private void modifyMetadataAsUpdate(
			final HiveVocabularyEntry hiveVocabularyEntry,
			final HiveVocabularyEntry currentEntry) throws IRODSHiveException,
			DataNotFoundException, JargonException {
		log.info("current entry, so it's an update");
		AvuData hiveAvu = translateHiveEntryIntoAvu(hiveVocabularyEntry);
		AvuData currentAvu = translateHiveEntryIntoAvu(currentEntry);

		if (hiveVocabularyEntry.getMetadataDomain() == MetadataDomain.COLLECTION) {
			log.info("updating collection metadata");
			collectionAO.modifyAVUMetadata(
					hiveVocabularyEntry.getDomainObjectUniqueName(),
					currentAvu, hiveAvu);
			log.info("collection metadata updated");
		} else {
			log.info("updating data object metadata");
			dataObjectAO.modifyAVUMetadata(
					hiveVocabularyEntry.getDomainObjectUniqueName(),
					currentAvu, hiveAvu);
			log.info("data object metadata modified");
		}
	}

	private void addMetadataAsNew(final HiveVocabularyEntry hiveVocabularyEntry)
			throws IRODSHiveException, DataNotFoundException,
			DuplicateDataException, JargonException {
		log.info("no current entry, so it's an add");
		AvuData hiveAvu = translateHiveEntryIntoAvu(hiveVocabularyEntry);
		// I am trusting the metadata type and path in the entry
		if (hiveVocabularyEntry.getMetadataDomain() == MetadataDomain.COLLECTION) {
			log.info("add avu to a collection");
			collectionAO.addAVUMetadata(
					hiveVocabularyEntry.getDomainObjectUniqueName(), hiveAvu);
		} else if (hiveVocabularyEntry.getMetadataDomain() == MetadataDomain.DATA) {
			log.info("add avu to a data object");
			dataObjectAO.addAVUMetadata(
					hiveVocabularyEntry.getDomainObjectUniqueName(), hiveAvu);
		} else {
			log.error("unsupported metadata domain:{}",
					hiveVocabularyEntry.getMetadataDomain());
			throw new IRODSHiveException("unsupported metadata domain");
		}
		log.info("metadata added");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.irods.IRODSHiveService#
	 * findHiveVocabularyEntryForPathAndURI(java.lang.String, java.lang.String)
	 */
	@Override
	public HiveVocabularyEntry findHiveVocabularyEntryForPathAndURI(
			final String irodsAbsolutePath, final String vocabularyTermURI)
			throws FileNotFoundException, IRODSHiveException {

		log.info("findEntryForPathAndURI()");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException(
					"null or emtpy irodsAbsolutePath");
		}

		if (vocabularyTermURI == null || vocabularyTermURI.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty vocabularyTermURI");
		}

		log.info("irodsAbsolutePath:{}", irodsAbsolutePath);
		log.info("vocabularyTermURI:{}", vocabularyTermURI);
		List<MetaDataAndDomainData> metadata = null;

		List<AVUQueryElement> avuQueryElements = new ArrayList<AVUQueryElement>(
				2);

		try {
			avuQueryElements.add(AVUQueryElement.instanceForValueQuery(
					AVUQueryPart.UNITS, AVUQueryOperatorEnum.EQUAL,
					VOCABULARY_AVU_UNIT));
			avuQueryElements.add(AVUQueryElement.instanceForValueQuery(
					AVUQueryPart.ATTRIBUTE, AVUQueryOperatorEnum.EQUAL,
					vocabularyTermURI));
			ObjStat objStat = collectionAndDataObjectListAndSearchAO
					.retrieveObjectStatForPath(irodsAbsolutePath);

			if (objStat.isSomeTypeOfCollection()) {
				log.info("is a collection");
				metadata = collectionAO
						.findMetadataValuesByMetadataQueryForCollection(
								avuQueryElements, irodsAbsolutePath);
			} else {
				log.info("is a data object");
				metadata = dataObjectAO
						.findMetadataValuesForDataObjectUsingAVUQuery(
								avuQueryElements, irodsAbsolutePath);
			}

			if (metadata.isEmpty()) {
				log.info("no metadata available, returning null");
				return null;
			}

			// will use the first entry (should only be one)..
			MetaDataAndDomainData domainData = metadata.get(0);

			log.info("found metadata:{}", domainData);
			return translateMetadataEntryIntoVocabularyEntry(domainData);

		} catch (FileNotFoundException fnf) {
			log.error("file not found for:{}", irodsAbsolutePath);
			throw fnf;
		} catch (JargonException e) {
			log.error("exception getting metadata", e);
			throw new IRODSHiveException(e);
		} catch (JargonQueryException e) {
			log.error("query exception getting metadata", e);
			throw new IRODSHiveException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.irods.IRODSHiveService#validateHiveVocabularyEntry
	 * (org.irods.jargon.hive.irods.HiveVocabularyEntry)
	 */
	@Override
	public void validateHiveVocabularyEntry(
			final HiveVocabularyEntry hiveVocabularyEntry)
			throws IRODSHiveException {

		log.info("validateHiveVocabularyEntry()");

		if (hiveVocabularyEntry == null) {
			throw new IllegalArgumentException("null hiveVocabularyEntry");
		}

		log.info("hiveVocabularyEntry:{}", hiveVocabularyEntry);

		if (hiveVocabularyEntry.getMetadataDomain() == null) {
			log.error("no metadataDomain for vocabulary entry");
			throw new IRODSHiveException(
					"no metadata domain for given vocbulary entry");
		}

		if (hiveVocabularyEntry.getMetadataDomain() == MetadataDomain.COLLECTION
				|| hiveVocabularyEntry.getMetadataDomain() == MetadataDomain.DATA) {
			// ok
		} else {
			log.error(
					"only supports metadata for collections and data objects, not {}",
					hiveVocabularyEntry.getMetadataDomain());
			throw new IRODSHiveException(
					"only supports metadata for collections and data objects");
		}

		if (hiveVocabularyEntry.getDomainObjectUniqueName() == null
				|| hiveVocabularyEntry.getDomainObjectUniqueName().isEmpty()) {
			log.error("no domain object unique name for vocabulary entry");
			throw new IRODSHiveException(
					"no metadata domain for given vocbulary entry");
		}

		if (hiveVocabularyEntry.getVocabularyName() == null
				|| hiveVocabularyEntry.getVocabularyName().isEmpty()) {
			log.error("no vocabularyName for entry");
			throw new IRODSHiveException("no vocabularyName specified");
		}

		if (hiveVocabularyEntry.getPreferredLabel() == null
				|| hiveVocabularyEntry.getPreferredLabel().isEmpty()) {
			log.error("no preferred label for entry");
			throw new IRODSHiveException("no preferred label specified");
		}

		if (hiveVocabularyEntry.getTermURI() == null
				|| hiveVocabularyEntry.getTermURI().isEmpty()) {
			log.error("no URI for entry");
			throw new IRODSHiveException("no URI specified");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.irods.IRODSHiveService#
	 * listVocabularyTermsForIRODSAbsolutePath(java.lang.String)
	 */
	@Override
	public List<HiveVocabularyEntry> listVocabularyTermsForIRODSAbsolutePath(
			final String irodsAbsolutePath) throws FileNotFoundException,
			IRODSHiveException {

		log.info("listVocabulariesMarkedForIRODSAbsolutePath()");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty irodsAbsolutePath");
		}

		log.info("listing vocabularies for absPath:{}", irodsAbsolutePath);

		try {
			CollectionAndDataObjectListAndSearchAO collectionAndDataObjectListAndSearchAO = getIrodsAccessObjectFactory()
					.getCollectionAndDataObjectListAndSearchAO(
							getIrodsAccount());
			ObjStat objStat = collectionAndDataObjectListAndSearchAO
					.retrieveObjectStatForPath(irodsAbsolutePath);
			log.info("got objStat:{}", objStat);
			List<MetaDataAndDomainData> metadata;

			metadata = listMetadataBasedOnObjStat(objStat);
			return translateMetadataListToVocabularEntries(metadata);

		} catch (JargonException e) {
			log.error("error getting access object", e);
			throw new IRODSHiveException(e);
		} catch (JargonQueryException e) {
			log.error("query exception getting vocabularies", e);
			throw new IRODSHiveException(e);
		}

	}

	/**
	 * Given a list of metadata attributes, create a list of parsed HIVE
	 * vocabulary terms
	 * 
	 * @param metadata
	 *            {@link MetaDataAndDomainData} with HIVE vocabulary entries
	 * @return <code>List</code> of {@link HiveVocabularyEntry} correesponding
	 *         to the raw iRODS AVU metadata provided
	 * @throws IRODSHiveException
	 */
	private List<HiveVocabularyEntry> translateMetadataListToVocabularEntries(
			final List<MetaDataAndDomainData> metadata)
			throws IRODSHiveException {

		log.info("translateMetadataListToVocabularEntries()");

		if (metadata == null) {
			throw new IllegalArgumentException("null metadata");
		}

		List<HiveVocabularyEntry> entries = new ArrayList<HiveVocabularyEntry>();

		for (MetaDataAndDomainData metadataEntry : metadata) {
			entries.add(translateMetadataEntryIntoVocabularyEntry(metadataEntry));
		}

		return entries;

	}

	/**
	 * Format an AVU that reflects a <code>HiveVocabularyEntry</code>
	 * 
	 * @param hiveVocabularyEntry
	 * @return
	 * @throws IRODSHiveException
	 */
	private AvuData translateHiveEntryIntoAvu(
			final HiveVocabularyEntry hiveVocabularyEntry)
			throws IRODSHiveException {

		log.info("translateHiveEntryIntoAvu()");

		if (hiveVocabularyEntry == null) {
			throw new IllegalArgumentException("null hiveVocabularyEntry");
		}

		log.info("hiveVocabularyEntry:{}", hiveVocabularyEntry);

		// build up the delimited data used in the AVU value

		// vocabulary=XXXX|preferredLabel=XXXXXX|comment=XXXXX

		StringBuilder sb = new StringBuilder();
		sb.append(VOCABULARY_DELIM);
		sb.append(hiveVocabularyEntry.getVocabularyName());
		sb.append(DELIM);
		sb.append(LABEL_DELIM);
		sb.append(hiveVocabularyEntry.getPreferredLabel());
		sb.append(DELIM);
		sb.append(COMMENT_DELIM);
		sb.append(hiveVocabularyEntry.getComment());

		try {
			return AvuData.instance(hiveVocabularyEntry.getTermURI(),
					sb.toString(), VOCABULARY_AVU_UNIT);
		} catch (JargonException e) {
			log.error("error creating AVU", e);
			throw new IRODSHiveException(e);
		}

	}

	/**
	 * Parse a particular raw AVU into a <code>HiveVocabularyEntry</code>,
	 * checking for proper formatting
	 * 
	 * @param metadataEntry
	 * @return
	 * @throws IRODSHiveException
	 */
	private HiveVocabularyEntry translateMetadataEntryIntoVocabularyEntry(
			final MetaDataAndDomainData metadataEntry)
			throws IRODSHiveException {

		if (metadataEntry == null) {
			throw new IllegalArgumentException("null metadataEntry");
		}

		if (!metadataEntry.getAvuUnit().equals(VOCABULARY_AVU_UNIT)) {
			throw new IllegalArgumentException(
					"metadata entry is not a HIVE vocabulary term");
		}

		log.info("have raw vocabulary term:{}", metadataEntry);

		HiveVocabularyEntry entry = new HiveVocabularyEntry();
		entry.setMetadataDomain(metadataEntry.getMetadataDomain());
		entry.setDomainObjectUniqueName(metadataEntry
				.getDomainObjectUniqueName());
		entry.setTermURI(metadataEntry.getAvuAttribute());
		entry.setMetadataAndDomainData(metadataEntry);

		/*
		 * parse the value, see top level comment for explanation of format of
		 * AVU data vocabulary=XXXX|preferredLabel=XXXXXX|comment=XXXXX
		 */

		String[] valueParts = metadataEntry.getAvuValue().split("\\|");
		if (valueParts.length != 3) {
			log.error("did not split value into 3 parts:{}",
					metadataEntry.getAvuValue());
			throw new IRODSHiveException(
					"unable to parse format of AVU to get information in value, should be 3 | delim parts");
		}

		// part1 vocabulary

		int idx = 0;

		idx = valueParts[0].indexOf(VOCABULARY_DELIM);
		if (idx == -1) {
			log.error("no vocabulary part in:{}", valueParts[0]);
			throw new IRODSHiveException("missing vocabulary part");
		}

		entry.setVocabularyName(valueParts[0].substring(VOCABULARY_DELIM
				.length()));

		// part2 preferred label
		idx = valueParts[1].indexOf(LABEL_DELIM);
		if (idx == -1) {
			log.error("no label part in:{}", valueParts[1]);
			throw new IRODSHiveException("missing label part");
		}

		entry.setPreferredLabel(valueParts[1].substring(LABEL_DELIM.length()));

		// part3 comment
		idx = valueParts[2].indexOf(COMMENT_DELIM);
		if (idx == -1) {
			log.error("no comment part in:{}", valueParts[2]);
			throw new IRODSHiveException("missing comment part");
		}

		entry.setComment(valueParts[2].substring(COMMENT_DELIM.length()));

		return entry;

	}

	/**
	 * Retrieve the raw metadata given an <code>ObjStat</code> that describes
	 * the target collection or data object
	 * 
	 * @param objStat
	 * @return
	 * @throws JargonException
	 * @throws JargonQueryException
	 */
	private List<MetaDataAndDomainData> listMetadataBasedOnObjStat(
			final ObjStat objStat) throws JargonException, JargonQueryException {

		log.info("listMetadataBasedOnObjStat()");

		if (objStat == null) {
			throw new IllegalArgumentException("null objstat");
		}

		List<AVUQueryElement> avuQueryElements = buildQueryToFindHiveMetadata();

		List<MetaDataAndDomainData> metadata;
		if (objStat.isSomeTypeOfCollection()) {
			log.info("is a collection...");
			CollectionAO collectionAO = getIrodsAccessObjectFactory()
					.getCollectionAO(getIrodsAccount());
			metadata = collectionAO
					.findMetadataValuesByMetadataQueryForCollection(
							avuQueryElements, objStat.getAbsolutePath(), 0);

		} else {
			log.info("is a dataObject...");
			DataObjectAO dataObjectAO = getIrodsAccessObjectFactory()
					.getDataObjectAO(getIrodsAccount());
			metadata = dataObjectAO
					.findMetadataValuesForDataObjectUsingAVUQuery(
							avuQueryElements, objStat.getAbsolutePath());

		}
		return metadata;
	}

	/**
	 * Handy method to build the metadata query used to find HIVE AVUs
	 * 
	 * @return <code>List</code> of {@link AVUQueryElements} that constitute the
	 *         appropriate metadata query
	 * @throws JargonQueryException
	 */
	public static List<AVUQueryElement> buildQueryToFindHiveMetadata()
			throws JargonQueryException {
		List<AVUQueryElement> avuQueryElements = new ArrayList<AVUQueryElement>(
				1);
		avuQueryElements.add(AVUQueryElement.instanceForValueQuery(
				AVUQueryPart.UNITS, AVUQueryOperatorEnum.EQUAL,
				VOCABULARY_AVU_UNIT));
		return avuQueryElements;
	}

}
