package org.irods.jargon.hive.external.indexer;

import org.irods.jargon.hive.external.utils.HiveException;

/**
 * General exception for HiveIndexer
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveIndexerException extends HiveException {

	private static final long serialVersionUID = 867196915409098622L;

	/**
	 * @param message
	 */
	public HiveIndexerException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HiveIndexerException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HiveIndexerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveIndexerException(final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveIndexerException(final String message,
			final int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveIndexerException(final String message, final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

}
