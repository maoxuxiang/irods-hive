/**
 * Copyright (c) 2010, UNC-Chapel Hill and Nescent
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided 
that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and 
 * the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the 
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the UNC-Chapel Hill or Nescent nor the names of its contributors may be used to endorse or promote 
 * products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

@author Jose R. Perez-Aguera
 */

package edu.unc.ils.mrc.hive.api.impl.elmo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrdf.elmo.sesame.SesameManager;

import edu.unc.ils.mrc.hive.HiveException;
import edu.unc.ils.mrc.hive.api.SKOSConcept;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;
import edu.unc.ils.mrc.hive2.api.HiveConcept;
import edu.unc.ils.mrc.hive2.api.HiveVocabulary;
import edu.unc.ils.mrc.hive2.api.impl.HiveVocabularyImpl;

/**
 * This class represents a HIVE vocabulary and associated indexes as described
 * in the HIVE vocabulary property file.
 * 
 * Each HIVE vocabulary consists of a Sesame store, Lucene index, and two
 * serialized TreeMaps representing the alphabetic and top-concept indexes.
 */
public class SKOSSchemeImpl implements SKOSScheme {

	private static final Log logger = LogFactory.getLog(SKOSSchemeImpl.class);

	/* Vocabulary/scheme name */
	private String schemeName;

	/* Vocabulary/scheme long name */
	private String longName;

	/* Vocabulary/scheme URI */
	private String schemaURI;

	/* Lucene index directory */
	private String indexDirectory;

	/* Sesame store directory */
	private String storeDirectory;

	/* H2 database directory */
	private String h2Directory;

	/* Alphabetic index file name */
	// private String alphaFilePath;

	/* Top concept index file name */
	// private String topConceptIndexPath;

	/* KEA+ stopwords file path */
	private String stopwordsPath;

	/* SKOS RDF/XML file path */
	private String rdfPath;

	/* KAE+ training set path */
	private String KEAtrainSetDir;

	/* KEA+ test set path */
	private String KEAtestSetDir;

	/* KEA+ model path */
	private String KEAModelPath;

	/* Maui model path */
	private String MauiModelPath;

	/* Lingpipe model path */
	private String lingpipeModel;

	/* Vocabulary creation date */
	private Date creationDate;

	/* Atom feed URL */
	private String atomFeedURL;

	/* Autocomplete index path */
	private String autocompletePath;

	/* KEA stemmer class name */
	private String keaStemmerClass;

	/* Maui stemmer class name */
	private String mauiStemmerClass;

	/* RDF Format */
	private String rdfFormat;

	private HiveVocabulary hiveVocab;

	private String date;
	private long numberOfConcepts;
	private long numberOfRelations;
	private long numberOfBroaders;
	private long numberOfNarrowers;
	private long numberOfRelated;

	public SKOSSchemeImpl(final String confPath, final String vocabularyName,
			final boolean firstTime) throws HiveException {
		String propertiesFile = confPath + File.separator + vocabularyName
				+ ".properties";
		init(propertiesFile);

		if (!firstTime) {

			try {
				logger.info("getting stats...");
				Map<String, Long> stats = hiveVocab.getStats();
				date = hiveVocab.getLastUpdateDate().toString();
				numberOfBroaders = stats.get("broader");
				numberOfConcepts = stats.get("concepts");
				numberOfNarrowers = stats.get("narrower");
				numberOfRelated = stats.get("related");
				numberOfRelations = numberOfBroaders + numberOfNarrowers
						+ numberOfRelated;
			} catch (Exception e) {
				logger.error(
						"exception occurred in constructor processing, not first time",
						e);
				throw new HiveException("error constructing skosScheme", e);
			}

		}
	}

	/**
	 * Initialize the scheme based on the specified properties file
	 * 
	 * @param propertiesFile
	 */
	private void init(final String propertiesFile) throws HiveException {
		logger.trace("init " + propertiesFile);

		logger.info("Loading vocabulary configuration from " + propertiesFile);

		Properties properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(propertiesFile);
			properties.load(fis);

			// Scheme name
			schemeName = properties.getProperty("name");
			if (schemeName == null || schemeName.isEmpty()) {
				logger.warn("name property is empty");
			}

			// Scheme long name
			longName = properties.getProperty("longName");
			if (longName == null || longName.isEmpty()) {
				logger.warn("longName property is empty");
			}

			// Scheme URI
			schemaURI = properties.getProperty("uri");
			if (schemaURI == null || schemaURI.isEmpty()) {
				logger.warn("uri property is empty");
			}

			//
			rdfFormat = properties.getProperty("rdfFormat", "rdfxml");
			if (rdfFormat == null || rdfFormat.isEmpty()) {
				logger.warn("rdfFormat property is empty");
			}

			// Lucene index path
			indexDirectory = properties.getProperty("index");
			if (indexDirectory == null || indexDirectory.isEmpty()) {
				logger.warn("index property is empty");
			}

			// Sesame store path
			storeDirectory = properties.getProperty("store");
			if (storeDirectory == null || storeDirectory.isEmpty()) {
				logger.warn("store property is empty");
			}

			// H2 store path
			h2Directory = properties.getProperty("h2");
			if (h2Directory == null || h2Directory.isEmpty()) {
				logger.warn("h2 property is empty");
			}

			// KEA+ model path
			KEAModelPath = properties.getProperty("kea_model");
			if (KEAModelPath == null || KEAModelPath.isEmpty()) {
				logger.warn("kea_model property is empty");
			}

			// Maui model path
			MauiModelPath = properties.getProperty("maui_model");
			if (MauiModelPath == null || MauiModelPath.isEmpty()) {
				logger.warn("maui_model property is empty");
			}

			// KEA+ test set path
			KEAtestSetDir = properties.getProperty("kea_test_set");
			if (KEAtestSetDir == null || KEAtestSetDir.isEmpty()) {
				logger.warn("kea_test_set property is empty");
			}

			// KEA+ training set path
			KEAtrainSetDir = properties.getProperty("kea_training_set");
			if (KEAtrainSetDir == null || KEAtrainSetDir.isEmpty()) {
				logger.warn("kea_training_set property is empty");
			}

			// KEA+ stopwords path
			stopwordsPath = properties.getProperty("stopwords");
			if (stopwordsPath == null || stopwordsPath.isEmpty()) {
				logger.warn("stopwords property is empty");
			}

			// Path to SKOS/RDF file
			rdfPath = properties.getProperty("rdf_file");
			if (rdfPath == null || rdfPath.isEmpty()) {
				logger.warn("rdf_file property is empty");
			}

			// Lingpipe model path
			lingpipeModel = properties.getProperty("lingpipe_model");
			if (lingpipeModel == null || lingpipeModel.isEmpty()) {
				logger.warn("lingpipe_model property is empty");
			}

			String dateStr = properties.getProperty("creationDate");
			SimpleDateFormat df = new SimpleDateFormat("MM-DD-yyyy");
			try {
				creationDate = df.parse(dateStr);
			} catch (Exception e) {
				logger.warn("Missing or invalid creationDate");
			}

			// Atom feed URL for synchronization
			atomFeedURL = properties.getProperty("atomFeedURL");
			if (atomFeedURL == null || atomFeedURL.isEmpty()) {
				logger.warn("atomFeedURL property is empty");
			}

			// Autocomplete index path
			autocompletePath = properties.getProperty("autocomplete");
			if (autocompletePath == null || autocompletePath.isEmpty()) {
				logger.warn("autocomplete property is empty");
			}

			// kea stemmer class
			keaStemmerClass = properties.getProperty("keaStemmerClass", "l");
			System.out.println("Using kea stemmer " + keaStemmerClass);
			if (keaStemmerClass == null || keaStemmerClass.isEmpty()) {
				logger.warn("keaStemmerClass property is empty, defaulting to kea.stemers.PorterStemmer");
			}

			// maui stemmer class
			mauiStemmerClass = properties.getProperty("mauiStemmerClass",
					"maui.stemmers.PorterStemmer");
			System.out.println("Using maui stemmer " + mauiStemmerClass);
			if (mauiStemmerClass == null || mauiStemmerClass.isEmpty()) {
				logger.warn("mauiStemmerClass property is empty, defaulting to maui.stemers.PorterStemmer");
			}

			fis.close();

			hiveVocab = HiveVocabularyImpl.getInstance(schemeName,
					indexDirectory, storeDirectory, h2Directory,
					autocompletePath);

		} catch (FileNotFoundException e) {
			throw new HiveException("Property file not found", e);
		} catch (IOException e) {
			throw new HiveException(
					"Error occurred during scheme initialization", e);
		}
	}

	@Override
	public String getStopwordsPath() {
		return stopwordsPath;
	}

	@Override
	public String getRdfPath() {
		return rdfPath;
	}

	@Override
	public String getKEAtrainSetDir() {
		return KEAtrainSetDir;
	}

	@Override
	public String getKEAtestSetDir() {
		return KEAtestSetDir;
	}

	@Override
	public String getKEAModelPath() {
		return KEAModelPath;
	}

	@Override
	public String getMauiModelPath() {
		return MauiModelPath;
	}

	@Override
	public String getAtomFeedURL() {
		return atomFeedURL;
	}

	@Override
	public String getAutoCompletePath() {
		return autocompletePath;
	}

	@Override
	/**
	   Returns an index of all terms, sorted alphabetically.
	 **/
	public TreeMap<String, QName> getSubAlphaIndex(final String startLetter) {
		TreeMap<String, QName> terms = new TreeMap<String, QName>();

		try {
			List<HiveConcept> hcs = hiveVocab.findConcepts(startLetter + "%",
					false);
			for (HiveConcept hc : hcs) {
				terms.put(hc.getPrefLabel(), hc.getQName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return terms;
	}

	@Override
	public List<SKOSConcept> getSubTopConceptIndex(final String startLetter) {
		List<SKOSConcept> terms = new ArrayList<SKOSConcept>();
		try {
			List<HiveConcept> hcs = hiveVocab.findConcepts(startLetter + "%",
					true);
			for (HiveConcept hc : hcs) {
				SKOSConceptImpl sc = new SKOSConceptImpl(hc.getQName());
				sc.setPrefLabel(hc.getPrefLabel());
				sc.setIsLeaf(hc.isLeaf());
				terms.add(sc);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return terms;
	}

	@Override
	public String getH2Path() {
		return h2Directory;
	}

	@Override
	public String getLastDate() {
		return date;
	}

	@Override
	public String getName() {
		return schemeName;
	}

	@Override
	public long getNumberOfConcepts() {
		return numberOfConcepts;
	}

	@Override
	public long getNumberOfBroader() {
		return numberOfBroaders;
	}

	@Override
	public long getNumberOfNarrower() {
		return numberOfNarrowers;
	}

	@Override
	public long getNumberOfRelated() {
		return numberOfRelated;
	}

	@Override
	public long getNumberOfRelations() {
		return numberOfRelations;
	}

	@Override
	public String getLongName() {
		return longName;
	}

	@Override
	public String getStoreDirectory() {
		return storeDirectory;
	}

	@Override
	public String getIndexDirectory() {
		return indexDirectory;
	}

	@Override
	public String getSchemaURI() {
		return schemaURI;
	}

	@Override
	public SesameManager getManager() {
		try {
			return hiveVocab.getManager();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	@Override
	public String getLingpipeModel() {
		return lingpipeModel;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void importConcept(final String uri) throws Exception {
		hiveVocab.importConcept(QName.valueOf(uri), uri);
	}

	@Override
	public void deleteConcept(final String uri) throws Exception {
		deleteConcept(QName.valueOf(uri));
	}

	@Override
	public void deleteConcept(final QName qname) throws Exception {
		hiveVocab.removeConcept(qname);
	}

	@Override
	public long getNumberOfTopConcepts() throws Exception {
		return hiveVocab.getNumTopConcepts();
	}

	@Override
	public void importConcepts(final String path) throws Exception {
		hiveVocab.importConcepts(path, rdfFormat);
	}

	@Override
	public void importConcepts(final String path, final boolean doSesame,
			final boolean doLucene, final boolean doH2, final boolean doH2KEA,
			final boolean doAutocomplete) throws Exception {
		hiveVocab.importConcepts(path, doSesame, doLucene, doH2, doH2KEA,
				doAutocomplete, rdfFormat);
	}

	@Override
	public void importConcept(final QName qname, final String path)
			throws Exception {
		hiveVocab.importConcept(qname, path);
	}

	@Override
	public Date getLastUpdateDate() {
		Date lastUpdate = null;
		try {
			lastUpdate = hiveVocab.getLastUpdateDate();
		} catch (Exception e) {
			logger.error(e);
		}
		return lastUpdate;
	}

	@Override
	public void close() throws Exception {
		hiveVocab.close();
	}

	@Override
	public List<AutocompleteTerm> suggestTermsFor(final String str,
			final int numTerms) throws Exception {
		return hiveVocab.suggestTermsFor(str, numTerms);
	}

	@Override
	public HiveVocabulary getHiveVocabulary() {
		return hiveVocab;
	}

	@Override
	public Map<String, QName> getAlphaIndex() {
		return hiveVocab.findAllConcepts(false);
	}

	@Override
	public Map<String, QName> getTopConceptIndex() {
		return hiveVocab.findAllConcepts(true);
	}

	@Override
	public String getKeaStemmerClass() {
		return keaStemmerClass;
	}

	@Override
	public String getMauiStemmerClass() {
		return mauiStemmerClass;
	}
}
