/**
 * 
 */
package org.irods.jargon.hive.query.rest.service;

import org.irods.jargon.core.exception.JargonException;

/**
 * General exception occurring while processing Hive queries
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveQueryServiceException extends JargonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1509431093240868608L;

	/**
	 * @param message
	 */
	public HiveQueryServiceException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HiveQueryServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public HiveQueryServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryServiceException(String message, Throwable cause,
			int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryServiceException(Throwable cause, int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryServiceException(String message, int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

}
