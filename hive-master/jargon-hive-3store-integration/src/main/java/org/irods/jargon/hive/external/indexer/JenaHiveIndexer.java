package org.irods.jargon.hive.external.indexer;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface to a service that can execute a process to index iRODS and create a
 * Jena <code>Model</code> based on HIVE metadata terms assigned to iRODS.
 * <p/>
 * Note that this is an initial implementation, and is certainly subject to
 * change!
 * <p/>
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface JenaHiveIndexer {

	/**
	 * Given a configuration, execute a process to index iRODS collections and
	 * data objects that are tagged with HIVE vocabulary terms, and return a
	 * Jena model populated with those terms, as well as additional RDF document
	 * data, such as vocabularies and other schema.
	 * <p/>
	 * Note that the {@link JenaHiveConfiguration} that is passed to
	 * implementations of this method will allow specification of the type of
	 * Jena model, location of additional RDF/XML documents to add, whether to
	 * auto-close the model, etc.
	 * <p/>
	 * This is an initial implementation and subject to modification!
	 * 
	 * @return <code>Model</code> of a Jena triple store based on the iRODS data
	 *         and provided RDF/XML documents. Note that the model may be
	 *         auto-closed, if set in the configuration, or it may need to be
	 *         closed by the caller.
	 * @throws JargonException
	 */
	Model execute() throws JargonException;

}