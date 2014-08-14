package org.irods.jargon.hive.irods.exception;

import org.irods.jargon.core.exception.JargonException;

/**
 * An exception occurred in storing or retrieving HIVE information in iRODS.
 * This is a general catch-all exception.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class IRODSHiveException extends JargonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7319553692038887010L;

	/**
	 * @param message
	 */
	public IRODSHiveException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IRODSHiveException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IRODSHiveException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(final String message,
			final int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(final String message, final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

}
