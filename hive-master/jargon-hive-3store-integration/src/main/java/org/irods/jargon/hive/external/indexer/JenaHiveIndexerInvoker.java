package org.irods.jargon.hive.external.indexer;

import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.query.AVUQueryElement;
import org.irods.jargon.core.query.JargonQueryException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitor;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker;
import org.irods.jargon.datautils.visitor.NoMoreItemsException;
import org.irods.jargon.hive.irods.IRODSHiveServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoker class uses a jargon framework to query for HIVE vocabulary terms in
 * AVUs, and to process each of these terms, adding it to a Jena model.
 * <p/>
 * As a result of this processing, the 'visitor' class holds a refernece to the
 * just built Jena model
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveIndexerInvoker extends
		AbstractIRODSVisitorInvoker<MetaDataAndDomainData> {

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveIndexerInvoker.class);

	private DataObjectAO dataObjectAO;
	private CollectionAO collectionAO;
	private List<MetaDataAndDomainData> metadataList;
	private List<AVUQueryElement> query;

	private boolean collectionsDone = false;
	private int currentIdx = -1;

	/**
	 * Control count of collections processed
	 */
	private int collectionsProcessed = 0;

	/**
	 * Control count of data objects processed
	 */
	private int dataObjectsProcessed = 0;

	public JenaHiveIndexerInvoker(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount,
			final AbstractIRODSVisitor<MetaDataAndDomainData> visitor)
			throws JargonException {
		super(irodsAccessObjectFactory, irodsAccount, visitor);

	}

	@Override
	public void close() throws JargonException {
		log.info("close()...closing iRODS connection...");
		getIrodsAccessObjectFactory().closeSessionAndEatExceptions();
	}

	/**
	 * Check to see if more data, this is done via checking for more
	 * collections, or at end of collections by looking at data objects. This
	 * also triggers any paging that needs to happen within collectons or data
	 * objects. This is called by the invoker framework as part of its normal
	 * iteration.
	 */
	@Override
	protected boolean hasMore() throws JargonException {

		log.info("hasMore()");
		if (metadataList.isEmpty() && collectionsDone) {
			log.info("collections are done, there were no data objects either...done");
			return false;
		}

		log.info("I had gotten some data, see if I am at the end (will be looking at the next item");

		if (currentIdx + 1 >= metadataList.size()) {
			log.info("next index exceeds current list, see what needs to be done next");
			return processEndOfCurrentListAndSeeIfMoreToReturn();
		} else {
			log.info("have more items in the current list, can do next index");
			return true;
		}
	}

	/**
	 * I am at the end of collections or data objects, or may need to page.
	 * Assess this and reset the index and listing as appropriate
	 * 
	 * @return
	 */
	private boolean processEndOfCurrentListAndSeeIfMoreToReturn()
			throws JargonException {
		log.info("processEndOfCurrentListAndSeeIfMoreToReturn()");

		log.info("current index:{}", currentIdx);
		log.info("collections done:{}", collectionsDone);

		if (metadataList.isEmpty() && collectionsDone) {
			log.info("collections done, no data objects");
			return false;
		}

		MetaDataAndDomainData last = metadataList.get(metadataList.size() - 1);
		log.info("last element:{}", last);

		if (last.isLastResult()) {
			log.info("last result..see if collections, and if so get data objects");
			if (!collectionsDone) {
				return queryDataObjects(0);
			} else {
				log.info("last result, was doing data objects to nothign else to process");
				return false;
			}
		} else {
			log.info("requery, not at last record");
			if (collectionsDone) {
				log.info("requery data objects");
				return queryDataObjects(last.getCount());
			} else {
				log.info("requery collections");
				return queryCollections(last.getCount());
			}
		}
	}

	/**
	 * Get objects to do AVU query on files and collections, this is called by
	 * the invoker framework
	 */
	@Override
	protected void initializeInvoker() throws JargonException {
		log.info("initializeInvoker()");
		dataObjectAO = getIrodsAccessObjectFactory().getDataObjectAO(
				getIrodsAccount());
		collectionAO = getIrodsAccessObjectFactory().getCollectionAO(
				getIrodsAccount());

		try {
			query = IRODSHiveServiceImpl.buildQueryToFindHiveMetadata();
			metadataList = collectionAO.findMetadataValuesByMetadataQuery(
					query, 0);
			if (metadataList.isEmpty()) {
				queryDataObjects(0);
			}
			log.info("initialized...");

		} catch (JargonQueryException e) {
			log.error("query exception", e);
			throw new JargonException("query exception getting hive metadata",
					e);
		}

		log.info("query collections for HIVE AVUs...");

	}

	/**
	 * Query for data objects based on the offset, resets the index
	 * 
	 * @param offset
	 * @return <code>boolean</code> of <code>true</code> if there are more
	 *         results as a result of this query
	 * @throws JargonException
	 */
	public boolean queryDataObjects(final int offset) throws JargonException {
		log.info("no metadata on collections, go ahead and mark as done and look at data objects");
		log.info("query data objects based on offset:{}", offset);

		try {
			collectionsDone = true;
			metadataList = dataObjectAO.findMetadataValuesByMetadataQuery(
					query, offset);
			// -1 because it will be incremented in 'next()' method, which
			// expects to advance before accessing
			currentIdx = -1;
			if (metadataList.isEmpty()) {
				return false;
			} else {
				return true;
			}

		} catch (JargonQueryException e) {
			log.error("query exception", e);
			throw new JargonException("query exception getting hive metadata",
					e);
		}

	}

	/**
	 * Query collections based on the offset, resets the index
	 * 
	 * @param offset
	 * @return <code>boolean</code> of <code>true</code> if there are more
	 *         results as a result of this query
	 * @throws JargonException
	 */
	public boolean queryCollections(final int offset) throws JargonException {
		log.info("query collections based on offset:{}", offset);

		try {

			metadataList = collectionAO.findMetadataValuesByMetadataQuery(
					query, offset);
			// -1 because it will be incremented in 'next()' method, which
			// expects to advance before accessing
			currentIdx = -1;
			if (metadataList.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (JargonQueryException e) {
			log.error("query exception", e);
			throw new JargonException("query exception getting hive metadata",
					e);
		}

	}

	/**
	 * Next method called by framework, note that the 'hasNext()' method will
	 * have checked and done any background paging, so exceeding the index is
	 * some sort of exception!
	 */
	@Override
	protected MetaDataAndDomainData next() throws NoMoreItemsException,
			JargonException {
		currentIdx++;
		if (currentIdx >= metadataList.size()) {
			throw new NoMoreItemsException("unable to get next item");
		}
		MetaDataAndDomainData metadata = metadataList.get(currentIdx);
		if (metadata.getMetadataDomain() == MetadataDomain.COLLECTION) {
			collectionsProcessed++;
		} else {
			dataObjectsProcessed++;
		}
		return metadata;
	}

	public int getCollectionsProcessed() {
		return collectionsProcessed;
	}

	public int getDataObjectsProcessed() {
		return dataObjectsProcessed;
	}

}
