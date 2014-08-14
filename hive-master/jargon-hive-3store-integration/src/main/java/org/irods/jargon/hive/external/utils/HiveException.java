package org.irods.jargon.hive.external.utils;

import org.irods.jargon.core.exception.JargonException;

/**
 * General HIVE exception that also wraps any underlying
 * <code>JargonException</code> for areas where iRODS interaction may occur
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveException extends JargonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -463769474594043308L;

	/**
	 * @param message
	 */
	public HiveException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HiveException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HiveException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveException(final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveException(final String message,
			final int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public HiveException(final String message, final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

}
