package org.irods.jargon.hive.external.indexer;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.service.AbstractJargonService;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Service to index data objects and collections on an iRODS data grid that
 * contain HIVE vocabulary terms, producing a Jena model
 * <p/>
 * The <code>JenaHiveConfiguration</code> will provide information on various
 * other thigns to load into the Jena model, type of model to return, etc.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveIndexerServiceImpl extends AbstractJargonService implements
		JenaHiveIndexer {

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveIndexerServiceImpl.class);
	private final JenaHiveConfiguration jenaHiveConfiguration;

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 */
	public JenaHiveIndexerServiceImpl(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount,
			final JenaHiveConfiguration jenaHiveConfiguration) {
		super(irodsAccessObjectFactory, irodsAccount);

		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}

		this.jenaHiveConfiguration = jenaHiveConfiguration;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.external.indexer.JenaHiveIndexer#execute()
	 */
	@Override
	public Model execute() throws JargonException {
		log.info("execute()");

		JenaHiveIndexerVisitor visitor = new JenaHiveIndexerVisitor(
				jenaHiveConfiguration);
		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				getIrodsAccessObjectFactory(), getIrodsAccount(), visitor);
		log.info("executing...");
		invoker.execute();
		log.info("executed, now get the Jena model and return to the caller");
		Model jenaModel = visitor.getJenaModel();
		if (jenaHiveConfiguration.isAutoCloseJenaModel()) {
			log.info("configuration says to autoclose jena model, closing..."); // FIXME:
																				// double
																				// close
			jenaModel.close();
		}
		return jenaModel;

	}

}
