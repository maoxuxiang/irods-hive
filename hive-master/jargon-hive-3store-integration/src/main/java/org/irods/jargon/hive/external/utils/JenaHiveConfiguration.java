package org.irods.jargon.hive.external.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class that specifies handling of a Jena model that will be
 * build based on iRODS AVU data representing HIVE terms applied to iRODS data
 * objects and collections.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveConfiguration {

	// FIXME: make config just the plain old jdbc url

	/**
	 * public link format
	 * http://iren-web.renci.org:8080/idrop-web2/home/link?irodsURI
	 * =irods%3A%2F%2F
	 * diamond.ils.unc.edu%3A2247%2FlifelibZone%2Fhome%2Fmconway%2Fxxx
	 * 
	 * file download format:
	 * http://iren-web.renci.org:8080/idrop-web2/browse/index#
	 * 
	 * 
	 */

	public static String SOURCE = "http://www.irods.org/ontologies/2013/2/iRODS.owl";
	public static String NS = SOURCE + "#";

	public static String JENA_DERBY_DB_TYPE = "Derby";

	/**
	 * Determines jena model type
	 * 
	 * @author Mike Conway - DICE (www.irods.org)
	 * 
	 */
	public enum JenaModelType {
		MEMORY_ONT, DATABASE_ONT
	}

	/**
	 * Type of Jena model to build
	 */
	private JenaModelType jenaModelType = JenaModelType.DATABASE_ONT;

	/**
	 * List of vocabulary file paths to load, this is loaded into the triple
	 * store
	 */
	private List<String> vocabularyRDFFileNames = new ArrayList<String>();

	/**
	 * File path to iRODS schema file, this is loaded into the triple store.
	 */
	private String irodsRDFFileName = "";

	/**
	 * Configuration tells indexer service to close the Jena model automatically
	 */
	private boolean autoCloseJenaModel = false;

	/**
	 * Class name of jdbc driver for jena database model. Required if using a
	 * database model
	 */
	private String jenaDbDriverClass = "";

	/**
	 * Uri of connection for jena model database, required if JenaModelTybe is a
	 * database model
	 */
	private String jenaDbUri = "";

	/**
	 * User name for jena model database, required if JenaModelType is a
	 * database model
	 */
	private String jenaDbUser = "";

	/**
	 * User name for jena model database, required if JenaModelType is a
	 * database model
	 */
	private String jenaDbPassword = "";

	/**
	 * Database type for jena model database, required if JenaModelType is a
	 * database moodel
	 */
	private String jenaDbType = "";

	/**
	 * Represents an iDrop context that will cause file download and public url
	 * links to be generated
	 */
	public String idropContext = "";

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("JenaHiveVisitorConfiguration");
		sb.append("\n\t jenaModelType:");
		sb.append(jenaModelType);
		sb.append("\n\t autoCloseJenaModel:");
		sb.append(autoCloseJenaModel);
		sb.append("\n\t  idropContext");
		sb.append(idropContext);
		if (jenaModelType == JenaModelType.DATABASE_ONT) {
			sb.append("\n\t jena database model info");
			sb.append("\n\t\t jenaDbDriverClass:");
			sb.append(jenaDbDriverClass);
			sb.append("\n\t\t jenaDbUri:");
			sb.append(jenaDbUri);
			sb.append("\n\t\t jenaDbUser:");
			sb.append(jenaDbUser);
			sb.append("\n\t\t jenaDbPassword:");
			sb.append("XXXXXXX");
			sb.append("\n\t\t jenaDbType:");
			sb.append(jenaDbType);
		}
		sb.append("\n\t irodsRDFFileName:");
		sb.append(irodsRDFFileName);

		sb.append("\n\t vocabularyFiles:");

		for (String fileName : vocabularyRDFFileNames) {
			sb.append("\n\t\t");
			sb.append(fileName);
		}

		return sb.toString();
	}

	public JenaHiveConfiguration() {
	}

	public JenaModelType getJenaModelType() {
		return jenaModelType;
	}

	public void setJenaModelType(final JenaModelType jenaModelType) {
		this.jenaModelType = jenaModelType;
	}

	public List<String> getVocabularyRDFFileNames() {
		return vocabularyRDFFileNames;
	}

	public void setVocabularyRDFFileNames(
			final List<String> vocabularyRDFFileNames) {
		this.vocabularyRDFFileNames = vocabularyRDFFileNames;
	}

	public String getIrodsRDFFileName() {
		return irodsRDFFileName;
	}

	public void setIrodsRDFFileName(final String irodsRDFFileName) {
		this.irodsRDFFileName = irodsRDFFileName;
	}

	/**
	 * @return the autoCloseJenaModel
	 */
	public boolean isAutoCloseJenaModel() {
		return autoCloseJenaModel;
	}

	/**
	 * @param autoCloseJenaModel
	 *            the autoCloseJenaModel to set
	 */
	public void setAutoCloseJenaModel(final boolean autoCloseJenaModel) {
		this.autoCloseJenaModel = autoCloseJenaModel;
	}

	/**
	 * @return the jenaDbDriverClass
	 */
	public String getJenaDbDriverClass() {
		return jenaDbDriverClass;
	}

	/**
	 * @param jenaDbDriverClass
	 *            the jenaDbDriverClass to set
	 */
	public void setJenaDbDriverClass(final String jenaDbDriverClass) {
		this.jenaDbDriverClass = jenaDbDriverClass;
	}

	/**
	 * @return the jenaDbUri
	 */
	public String getJenaDbUri() {
		return jenaDbUri;
	}

	/**
	 * @param jenaDbUri
	 *            the jenaDbUri to set
	 */
	public void setJenaDbUri(final String jenaDbUri) {
		this.jenaDbUri = jenaDbUri;
	}

	/**
	 * @return the jenaDbUser
	 */
	public String getJenaDbUser() {
		return jenaDbUser;
	}

	/**
	 * @param jenaDbUser
	 *            the jenaDbUser to set
	 */
	public void setJenaDbUser(final String jenaDbUser) {
		this.jenaDbUser = jenaDbUser;
	}

	/**
	 * @return the jenaDbPassword
	 */
	public String getJenaDbPassword() {
		return jenaDbPassword;
	}

	/**
	 * @param jenaDbPassword
	 *            the jenaDbPassword to set
	 */
	public void setJenaDbPassword(final String jenaDbPassword) {
		this.jenaDbPassword = jenaDbPassword;
	}

	/**
	 * @return the jenaDbType
	 */
	public String getJenaDbType() {
		return jenaDbType;
	}

	/**
	 * @param jenaDbType
	 *            the jenaDbType to set
	 */
	public void setJenaDbType(final String jenaDbType) {
		this.jenaDbType = jenaDbType;
	}

	/**
	 * @return the idropContext
	 */
	public String getIdropContext() {
		return idropContext;
	}

	/**
	 * @param idropContext
	 *            the idropContext to set
	 */
	public void setIdropContext(final String idropContext) {
		this.idropContext = idropContext;
	}

}
