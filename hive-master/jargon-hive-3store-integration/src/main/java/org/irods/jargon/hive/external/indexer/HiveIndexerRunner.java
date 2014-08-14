package org.irods.jargon.hive.external.indexer;

import java.util.ArrayList;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to run the HIVE indexer from the command line
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveIndexerRunner {

	public static final Logger log = LoggerFactory
			.getLogger(HiveIndexerRunner.class);

	private IRODSAccount irodsAccount;
	private JenaHiveConfiguration jenaHiveConfiguration;

	private IRODSFileSystem irodsFileSystem;

	/**
	 * 
	 */
	public HiveIndexerRunner() throws HiveIndexerException {
		try {
			irodsFileSystem = new IRODSFileSystem();
		} catch (JargonException e) {
			log.error("error creating IRODSFileSystem");
			throw new HiveIndexerException("error creating irods file system",
					e);
		}
	}

	public void index() throws HiveIndexerException {
		log.info("index()");
		log.info("irodsAccount:{}", irodsAccount);
		log.info("jenaHiveConfiguration:{}", jenaHiveConfiguration);
		JenaHiveIndexer jenaHiveIndexerService;
		try {
			jenaHiveIndexerService = new JenaHiveIndexerServiceImpl(
					irodsFileSystem.getIRODSAccessObjectFactory(),
					irodsAccount, jenaHiveConfiguration);
			jenaHiveIndexerService.execute();
		} catch (JargonException e) {
			log.error("error creating IRODSFileSystem");
			throw new HiveIndexerException(
					"error creating and running indexer", e);
		} finally {
			irodsFileSystem.closeAndEatExceptions();
		}
	}

	/**
	 * @param args
	 *            (host port zone user password indexPath indexUser
	 *            indexPassword idropContext irodsRdfPath vocabList....)
	 */
	public static void main(final String[] args) {
		if (args.length < 10) {
			throw new RuntimeException(
					"unable to run, must be at least 10 parms");
		}

		String host = args[0];
		String port = args[1];
		String zone = args[2];
		String user = args[3];
		String password = args[4];
		String indexPath = args[5];
		String indexUser = args[6];
		String indexPassword = args[7];
		String idropContext = args[8];
		String irodsRdfPath = args[9];

		List<String> vocabs = new ArrayList<String>();

		for (int i = 10; i < args.length; i++) {
			vocabs.add(args[i]);
		}

		try {
			IRODSAccount irodsAccount = IRODSAccount.instance(host,
					Integer.parseInt(port), user, password, "", zone, "");

			JenaHiveConfiguration jenaHiveConfiguration = new JenaHiveConfiguration();
			jenaHiveConfiguration.setIdropContext(idropContext);
			jenaHiveConfiguration.setIrodsRDFFileName(irodsRdfPath);
			jenaHiveConfiguration
					.setJenaDbDriverClass("org.apache.derby.jdbc.EmbeddedDriver"); // hard
																					// code
																					// for
																					// now...
			jenaHiveConfiguration.setJenaDbPassword(indexPassword);
			jenaHiveConfiguration.setJenaDbUri(indexPath);
			jenaHiveConfiguration.setJenaDbUser(indexUser);
			jenaHiveConfiguration.setVocabularyRDFFileNames(vocabs);
			jenaHiveConfiguration
					.setJenaDbType(JenaHiveConfiguration.JENA_DERBY_DB_TYPE);
			jenaHiveConfiguration.setAutoCloseJenaModel(true);
			HiveIndexerRunner runner = new HiveIndexerRunner();
			runner.setIrodsAccount(irodsAccount);
			runner.setJenaHiveConfiguration(jenaHiveConfiguration);
			runner.index();

		} catch (NumberFormatException e) {
			log.error("number format error parsing account", e);
			throw new RuntimeException(e);
		} catch (JargonException e) {
			log.error("jargon exception running indexer", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * @return the irodsAccount
	 */
	public IRODSAccount getIrodsAccount() {
		return irodsAccount;
	}

	/**
	 * @param irodsAccount
	 *            the irodsAccount to set
	 */
	public void setIrodsAccount(final IRODSAccount irodsAccount) {
		this.irodsAccount = irodsAccount;
	}

	/**
	 * @return the jenaHiveConfiguration
	 */
	public JenaHiveConfiguration getJenaHiveConfiguration() {
		return jenaHiveConfiguration;
	}

	/**
	 * @param jenaHiveConfiguration
	 *            the jenaHiveConfiguration to set
	 */
	public void setJenaHiveConfiguration(
			final JenaHiveConfiguration jenaHiveConfiguration) {
		this.jenaHiveConfiguration = jenaHiveConfiguration;
	}

}
