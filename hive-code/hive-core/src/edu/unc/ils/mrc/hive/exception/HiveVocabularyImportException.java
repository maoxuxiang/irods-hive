/**
 * 
 */
package edu.unc.ils.mrc.hive.exception;

import edu.unc.ils.mrc.hive.HiveException;

/**
 * Generic exception importing vocabularies into HIVE
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveVocabularyImportException extends HiveException {

	private static final long serialVersionUID = -5201011855128541296L;

	/**
	 * @param arg0
	 */
	public HiveVocabularyImportException(final String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HiveVocabularyImportException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HiveVocabularyImportException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
