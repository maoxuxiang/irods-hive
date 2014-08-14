package edu.unc.ils.mrc.hive.ir.tagging;

/**
 * Copyright (c) 2011, UNC-Chapel Hill and Nescent
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

 @author Joan Boone
 */
import java.util.HashSet;
import java.util.List;

import maui.main.MauiTopicExtractor;
import maui.stemmers.PorterStemmer;
import maui.stemmers.Stemmer;
import maui.stopwords.StopwordsEnglish;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import edu.unc.ils.mrc.hive.admin.AdminVocabularies;
import edu.unc.ils.mrc.hive.api.SKOSScheme;

public class MauiTagger implements Tagger {

	private static final Log logger = LogFactory
			.getLog(AdminVocabularies.class);
	private MauiTopicExtractor te;
	private String vocabulary;
	private static final int DEFAULT_NUM_PHRASES = 10;

	public MauiTagger(final String dirName, final String modelName,
			final String stopwordsPath, final SKOSScheme schema) {

		vocabulary = schema.getLongName();
		te = new MauiTopicExtractor();
		te.setInputDirectoryName(dirName);
		te.setModelName(modelName);
		te.setVocabularyName(schema.getName().toLowerCase());
		te.setVocabularyDirectory(schema.getRdfPath());
		te.setVocabularyFormat("skos");
		te.setDocumentEncoding("UTF-8");
		te.setDocumentLanguage("en"); // es for Spanish, fr for French
		String stemmerClass = schema.getMauiStemmerClass();
		try {
			Class cls = Class.forName(stemmerClass);
			Stemmer stemmer = (Stemmer) cls.newInstance();
			te.setStemmer(stemmer);
		} catch (Exception e) {
			System.out
					.println("Error instantiating stemmer: " + e.getMessage());
			te.setStemmer(new PorterStemmer());
		}
		te.setStopwords(new StopwordsEnglish(stopwordsPath));
		te.setBuildGlobalDictionary(false);
		te.setAdditionalInfo(true);

		try {
			te.loadModel();
		} catch (Exception e) {
			logger.fatal("Unable to load model for Maui *", e);
		}

		te.loadThesaurus(te.getStemmer(), te.getStopwords(), schema.getH2Path());
		// this.te.loadThesaurus(this.te.getStemmer(), this.te.getStopwords(),
		// schema.getRdfPath());
	}

	/**
	 * Extracts keyphrases from .txt files in the directory specified in the
	 * constructor. Keyphrases are written to .key files which can be read by
	 * the calling application.
	 */
	@Override
	public void extractKeyphrases(final int numTerms, final int minOccur) {
		StopWatch stopwatch = new Log4JStopWatch();
		try {
			te.setMinNumOccur(minOccur);
			te.setTopicsPerDocument(numTerms);
			te.extractKeyphrases(te.collectStems());
		} catch (Exception e) {
			e.printStackTrace();
		}
		stopwatch.lap("ExtractKeyPhrases - " + vocabulary);
	}

	@Override
	public void extractKeyphrases() {
		extractKeyphrases(DEFAULT_NUM_PHRASES, 2);
	}

	/**
	 * Extracts keyphrases from the specified file baseName. The underlying
	 * class will append a ".txt" suffix to read the file and a ".key" suffix to
	 * the generated keyphrase file name. The calling application can read the
	 * generated keyphrases from the ".key" file.
	 */
	@Override
	public void extractKeyphrasesFromFile(final String baseName,
			final int numTerms, final int minOccur) {
		StopWatch stopwatch = new Log4JStopWatch();
		try {
			// Hashtable<String, Double> stems = new Hashtable<String,
			// Double>();
			// stems.put(baseName, new Double(0));
			HashSet<String> stems = new HashSet<String>();
			stems.add(baseName);
			te.setMinNumOccur(minOccur);
			te.setTopicsPerDocument(numTerms);
			te.extractKeyphrases(stems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stopwatch.lap("ExtractKeyPhrasesFromFile - " + vocabulary);
	}

	@Override
	public void extractKeyphrasesFromFile(final String baseName) {
		extractKeyphrasesFromFile(baseName, DEFAULT_NUM_PHRASES, 2);
	}

	// Not implemented
	@Override
	public List<String> extractKeyphrases(final String text) {
		return null;
	}

	@Override
	public String getVocabulary() {
		return vocabulary;
	}

}
