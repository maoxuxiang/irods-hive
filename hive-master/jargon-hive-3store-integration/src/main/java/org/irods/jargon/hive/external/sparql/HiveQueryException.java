package org.irods.jargon.hive.external.sparql;

import org.irods.jargon.hive.external.utils.HiveException;

/**
 * General exception querying for HIVE/iRODS data
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveQueryException extends HiveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2848562506861698062L;

	/**
	 * @param message
	 */
	public HiveQueryException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HiveQueryException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HiveQueryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryException(final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryException(final String message,
			final int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveQueryException(final String message, final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

}
