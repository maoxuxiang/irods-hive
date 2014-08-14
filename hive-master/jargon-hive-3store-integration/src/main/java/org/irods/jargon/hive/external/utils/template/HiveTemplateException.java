package org.irods.jargon.hive.external.utils.template;

import org.irods.jargon.hive.external.utils.HiveException;

/**
 * General exception working with templates
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveTemplateException extends HiveException {

	public HiveTemplateException(final String message,
			final int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	public HiveTemplateException(final String message, final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

	public HiveTemplateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HiveTemplateException(final String message) {
		super(message);
	}

	public HiveTemplateException(final Throwable cause,
			final int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	public HiveTemplateException(final Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5613906531102500238L;

}
