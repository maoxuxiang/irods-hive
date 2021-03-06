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

package edu.unc.ils.mrc.hive.ir.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.TopDocCollector;
import org.openrdf.concepts.skos.core.Concept;
import org.openrdf.elmo.sesame.SesameManager;

import edu.unc.ils.mrc.hive.api.SKOSConcept;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSConceptImpl;
import edu.unc.ils.mrc.hive.ir.lucene.analysis.HIVEAnalyzer;

/*
 * This class load Lucene indexes and create a Multisearcher with them
 */

public class ConceptMultiSearcher implements Searcher {

	private static final Log logger = LogFactory
			.getLog(ConceptMultiSearcher.class);

	private static int NUMBER_RESULTS = 1500;

	private Searchable[] searchers;
	private MultiSearcher searcher;

	public ConceptMultiSearcher(final String[] indexes) {
		searchers = new IndexSearcher[indexes.length];
		initIndex(indexes);
	}

	@Override
	public List<SKOSConcept> search(final String word,
			final SesameManager[] managers, final boolean brief) {
		if (brief) {
			return searchBrief(word);
		} else {
			return search(word, managers);
		}

	}

	/**
	 * Searches for the specified word/phrase in Lucene and returns brief
	 * SKOSConcept records. This method does not lookup the concept in Sesame
	 * and compose a full set of relationships.
	 * 
	 * @param word
	 *            Word or phrase to search for
	 * @return List of brief SKOSConcept objects
	 */
	protected List<SKOSConcept> searchBrief(final String word) {
		logger.trace("search " + word);
		String[] fields = { "prefLabel", "altLabel" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
				new HIVEAnalyzer());

		List<SKOSConcept> skosConceptList = new ArrayList<SKOSConcept>();

		try {
			Query query = parser.parse(word);

			TopDocCollector collector = new TopDocCollector(NUMBER_RESULTS);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			logger.debug("Total number of results: " + hits.length);
			for (ScoreDoc hit : hits) {

				int docId = hit.doc;
				Document doc = searcher.doc(docId);
				String uri = doc.get("uri");
				String lp = doc.get("localPart");
				String prefLabel = doc.get("prefLabel");

				SKOSConcept concept = new SKOSConceptImpl(new QName(uri, lp));
				concept.setPrefLabel(prefLabel);

				// Move exact matches on pref-label to the top of the results
				// list
				if (prefLabel.equals(word)) {
					skosConceptList.add(0, concept);
				} else {
					skosConceptList.add(concept);
				}
			}
		} catch (IOException e) {
			logger.error(e);
		} catch (ParseException e) {
			logger.error(e);
		}

		return skosConceptList;
	}

	/**
	 * Searches for the specified word/phrase in Lucene and uses the Sesame
	 * store to compose full SKOSConcept objects including all relations.
	 */
	@Override
	public List<SKOSConcept> search(final String word,
			final SesameManager[] managers) {
		logger.trace("search " + word);
		String[] fields = { "prefLabel", "altLabel" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
				new HIVEAnalyzer());

		List<Concept> ranking = new ArrayList<Concept>();

		try {
			Query query = parser.parse(word);
			TopDocCollector collector = new TopDocCollector(NUMBER_RESULTS);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			logger.debug("Total number of results: " + collector.getTotalHits());
			for (ScoreDoc hit : hits) {

				Concept concept;
				int docId = hit.doc;
				Document doc = searcher.doc(docId);
				String uri = doc.get("uri");
				String lp = doc.get("localPart");

				for (SesameManager manager : managers) {
					concept = manager.find(Concept.class, new QName(uri, lp));
					if (concept != null) {
						// An exact match on prefLabel should always be first.
						if (concept.getSkosPrefLabel().equalsIgnoreCase(word)) {
							ranking.add(0, concept);
						} else {
							ranking.add(concept);
						}
					}
				}

			}
		} catch (IOException e) {
			logger.error(e);
		} catch (ParseException e) {
			logger.error(e);
		}

		List<SKOSConcept> skosConceptList = new ArrayList<SKOSConcept>();

		for (Concept elmoConcept : ranking) {
			SKOSConcept sconcept = new SKOSConceptImpl(elmoConcept.getQName());
			sconcept.setPrefLabel(elmoConcept.getSkosPrefLabel());
			Set<String> altSet = elmoConcept.getSkosAltLabels();
			for (String alt : altSet) {
				sconcept.addAltLabel(alt);
			}
			Set<Concept> broaderSet = elmoConcept.getSkosBroaders();
			for (Concept broader : broaderSet) {
				sconcept.addBroader(broader.getSkosPrefLabel(),
						broader.getQName());
			}
			Set<Concept> narrowerSet = elmoConcept.getSkosNarrowers();
			for (Concept narrower : narrowerSet) {
				sconcept.addNarrower(narrower.getSkosPrefLabel(),
						narrower.getQName());
			}
			Set<Concept> relatedSet = elmoConcept.getSkosRelated();
			for (Concept related : relatedSet) {
				sconcept.addRelated(related.getSkosPrefLabel(),
						related.getQName());
			}

			Set<Object> scopeNotes = elmoConcept.getSkosScopeNotes();
			for (Object scopeNote : scopeNotes) {
				sconcept.addScopeNote((String) scopeNote);
			}

			skosConceptList.add(sconcept);
		}

		return skosConceptList;
	}

	private void initIndex(final String[] indexList) {
		logger.trace("initIndex");
		try {
			for (int i = 0; i < indexList.length; i++) {
				searchers[i] = new IndexSearcher(indexList[i]);
			}
			searcher = new MultiSearcher(searchers);
		} catch (CorruptIndexException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}

	}

	@Override
	public void close() {
		logger.trace("close");

		try {
			searcher.close();
			for (Searchable s : searchers) {
				s.close();
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
