/**
 * 
 */
package org.irods.jargon.hive.exception;

/**
 * A vocabulary was not found in the loaded SKOSServer
 * 
 * @author Koushyar
 * 
 */
public class VocabularyNotFoundException extends JargonHiveException {

	/**
	 * 
	 */
	public static final long serialVersionUID = 5554520739432231007L;

	/**
	 * 
	 */
	public VocabularyNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VocabularyNotFoundException(final String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VocabularyNotFoundException(final Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VocabularyNotFoundException(final String message,
			final Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
