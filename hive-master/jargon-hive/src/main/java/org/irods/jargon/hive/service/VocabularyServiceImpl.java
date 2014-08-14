package org.irods.jargon.hive.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.exception.VocabularyNotFoundException;
import org.irods.jargon.hive.service.domain.VocabularyInfo;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.api.ConceptNode;
import edu.unc.ils.mrc.hive.api.SKOSConcept;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.SKOSSearcher;
import edu.unc.ils.mrc.hive.api.SKOSServer;
import edu.unc.ils.mrc.hive.api.SKOSTagger;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;

/**
 * Refactored version originally found in hive-web from the original HIVE
 * project
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class VocabularyServiceImpl implements VocabularyService {
	private static final Log logger = LogFactory
			.getLog(VocabularyServiceImpl.class); // jpb
	private HiveContainer hiveContainer = null;

	private final int DEFAULT_MIN_OCCUR = 2;

	@Override
	public void setHiveContainer(final HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}

	public VocabularyServiceImpl() {

	}

	public VocabularyServiceImpl(final HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}

	private SKOSServer getSkosServer() {
		return hiveContainer.getSkosServer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#getSKOSSearcher()
	 */
	@Override
	public SKOSSearcher getSKOSSearcher() {
		return hiveContainer.getSkosServer().getSKOSSearcher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getNumberOfConcept(java
	 * .lang.String)
	 */
	@Override
	public long getNumberOfConcept(final String vocabularyName)
			throws VocabularyNotFoundException {
		SKOSScheme vocab = getSkosServer().getSKOSSchemas().get(vocabularyName);
		if (vocab == null) {
			throw new VocabularyNotFoundException("did not find:"
					+ vocabularyName);
		}
		return vocab.getNumberOfConcepts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getNumerOfRelations(java
	 * .lang.String)
	 */
	@Override
	public long getNumerOfRelations(final String vocabularyName) {
		return getSkosServer().getSKOSSchemas().get(vocabularyName)
				.getNumberOfRelations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getLastUpdateDate(java
	 * .lang.String)
	 */
	@Override
	public Date getLastUpdateDate(final String vocabularyName) {
		return getSkosServer().getSKOSSchemas().get(vocabularyName)
				.getLastUpdateDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#getAllVocabularies()
	 */
	@Override
	public List<VocabularyInfo> getAllVocabularies() {
		logger.info("getAllVocabularies()");
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		logger.info("vocabMap from skosServer}" + vocabularyMap);
		List<VocabularyInfo> vocabularyList = new ArrayList<VocabularyInfo>();
		Set<String> vnames = vocabularyMap.keySet();
		Iterator<String> it = vnames.iterator();
		logger.info("getting ready to iterate through vocabularies...");
		VocabularyInfo vocabularyInfo = null;
		while (it.hasNext()) {
			logger.info("vocab:" + it);
			SKOSScheme vocabulary = vocabularyMap.get(it.next());
			vocabularyInfo = new VocabularyInfo();
			vocabularyInfo.setVocabularyName(vocabulary.getName());
			vocabularyInfo
					.setNumberOfConcepts(vocabulary.getNumberOfConcepts());
			vocabularyInfo.setNumberOfRelations(vocabulary
					.getNumberOfRelations());
			vocabularyInfo.setLastUpdated(vocabulary.getLastUpdateDate());
			vocabularyList.add(vocabularyInfo);

		}
		return vocabularyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getVocabularyByName(java
	 * .lang.String)
	 */
	@Override
	public SKOSScheme getVocabularyByName(final String vocabularyName)
			throws VocabularyNotFoundException {
		logger.info("getVocabularyByName()");

		if (vocabularyName == null || vocabularyName.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabularyName");
		}

		logger.info("looking for name:" + vocabularyName);

		SKOSScheme vocab = getSkosServer().getSKOSSchemas().get(vocabularyName);

		if (vocab == null) {
			throw new VocabularyNotFoundException(
					"did not find requested vocabulary in HIVE");
		}

		return vocab;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getAllVocabularyNames()
	 */
	@Override
	public List<String> getAllVocabularyNames() {
		logger.info("getAllVocabularyNames()");
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		Set<String> keys = vocabularyMap.keySet();
		List<String> names = new ArrayList<String>();
		for (String key : keys) {
			names.add(key);
		}

		logger.info("names:" + names);
		return names;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getVocabularyProperties()
	 */
	@Override
	public HashMap<String, HashMap<String, String>> getVocabularyProperties() {
		HashMap<String, HashMap<String, String>> props = new HashMap<String, HashMap<String, String>>();
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		Set<String> keys = vocabularyMap.keySet();
		HashMap<String, String> propVals;
		for (String key : keys) {
			propVals = new HashMap<String, String>();
			SKOSScheme vocabulary = vocabularyMap.get(key);
			propVals.put("uri", vocabulary.getSchemaURI());
			props.put(key, propVals);
		}
		return props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#
	 * getConceptProxyForTopOfVocabulary(java.lang.String, java.lang.String,
	 * boolean)
	 */
	@Override
	public ConceptProxy getConceptProxyForTopOfVocabulary(
			final String vocabulary, String letter, final boolean brief)
			throws VocabularyNotFoundException {

		logger.info("getConceptProxyForTopOfVocabulary()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabulary");
		}

		if (letter == null || letter.isEmpty()) {
			letter = "A";
		}

		logger.info("vocabulary:" + vocabulary);
		logger.info("letter:" + letter);
		logger.info("brief:" + brief);

		logger.info("getting list of concept proxies for children...");
		List<ConceptProxy> children = getSubTopConcept(vocabulary, letter,
				brief);
		logger.info("converting to hashmap and building wrapper concept proxy");

		ConceptProxy parent = new ConceptProxy();
		parent.setOrigin(vocabulary);
		parent.setTopLevel(true);
		parent.setNarrower(new HashMap<String, String>());
		for (ConceptProxy child : children) {
			parent.getNarrower().put(child.getPreLabel(), child.getURI());
		}

		return parent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getSubTopConcept(java
	 * .lang.String, java.lang.String, boolean)
	 */
	@Override
	public List<ConceptProxy> getSubTopConcept(final String vocabulary,
			final String letter, final boolean brief)
			throws VocabularyNotFoundException {
		TreeMap<String, SKOSScheme> vocabularies = getSkosServer()
				.getSKOSSchemas();
		SKOSScheme targetVoc = vocabularies.get(vocabulary);

		if (targetVoc == null) {
			throw new VocabularyNotFoundException("vocabulary not found");
		}

		List<SKOSConcept> top = targetVoc.getSubTopConceptIndex(letter);
		List<ConceptProxy> fatherList = new ArrayList<ConceptProxy>();
		for (SKOSConcept sc : top) {

			QName q = sc.getQName();
			boolean isleaf = sc.isLeaf();

			if (!brief) {
				SKOSConcept concept = getSkosServer().getSKOSSearcher()
						.searchConceptByURI(q.getNamespaceURI(),
								q.getLocalPart());
				int numberOfChildren = concept.getNumberOfChildren();

				if (numberOfChildren == 0) {
					isleaf = true;
				}
			}
			String uri = q.getNamespaceURI();
			String localPart = q.getLocalPart();
			String URI = uri + " " + localPart;
			String prefLabel = sc.getPrefLabel();
			ConceptProxy father = new ConceptProxy(vocabulary, prefLabel, URI,
					isleaf);
			fatherList.add(father);
		}
		return fatherList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getChildConcept(java.
	 * lang.String, java.lang.String)
	 */

	@Override
	public List<ConceptProxy> getChildConcept(final String nameSpaceURI,
			final String localPart) {
		SKOSSearcher searcher = getSkosServer().getSKOSSearcher();
		TreeMap<String, QName> children = searcher.searchChildrenByURI(
				nameSpaceURI, localPart);
		List<ConceptProxy> childrenList = null;
		if (children.size() != 0) {
			childrenList = new ArrayList<ConceptProxy>();
			for (String cl : children.keySet()) {
				String origin = getSkosServer().getOrigin(children.get(cl));
				String preLabel = cl;
				String namespace = children.get(cl).getNamespaceURI();
				String lp = children.get(cl).getLocalPart();
				SKOSConcept concept = getSkosServer().getSKOSSearcher()
						.searchConceptByURI(namespace, localPart);
				int numberOfChildren = concept.getNumberOfChildren();
				boolean isleaf = true;
				if (numberOfChildren != 0) {
					isleaf = false;
				}
				String URI = namespace + " " + lp;
				ConceptProxy cpr = new ConceptProxy(origin, preLabel, URI,
						isleaf);
				childrenList.add(cpr);
			}
		}
		return childrenList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#searchConcept(java.lang
	 * .String, java.util.List)
	 */

	@Override
	public Set<ConceptProxy> searchConcept(final String keyword,
			final List<String> openedVocabularies) {

		// maintain the rank list
		SKOSSearcher searcher = getSkosServer().getSKOSSearcher();
		List<SKOSConcept> result = searcher.searchConceptByKeyword(keyword,
				true);
		Set<ConceptProxy> rankedset = new HashSet<ConceptProxy>();
		Map<String, ConceptProxy> resultMap = new HashMap<String, ConceptProxy>();

		for (String s : openedVocabularies) {
			System.out.println(s);
		}
		if (result.size() != 0) {
			for (SKOSConcept c : result) {
				String origin = getSkosServer().getOrigin(c.getQName());
				// shim for case problem in search of vocabulary service mcc
				if (openedVocabularies.contains(origin.toLowerCase())
						|| openedVocabularies.contains(origin.toUpperCase())) {
					String preLabel = c.getPrefLabel();
					QName qname = c.getQName();
					String namespace = qname.getNamespaceURI();
					String localPart = qname.getLocalPart();
					String uri = namespace + " " + localPart;
					logger.info("SKOSConcept: preLabel: " + preLabel
							+ "; uri: " + uri + "; QName: " + qname
							+ "; origin: " + origin);
					ConceptProxy cp = new ConceptProxy(origin, preLabel, uri);

					resultMap.put(uri, cp);

				}
			}
		}

		rankedset.addAll(resultMap.values());
		return rankedset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getConceptByURI(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public ConceptProxy getConceptByURI(final String namespaceURI,
			final String localPart) {

		logger.info("getConceptByURI()");

		if (namespaceURI == null || namespaceURI.isEmpty()) {
			throw new IllegalArgumentException("Null or empty namespaceURI");
		}

		if (localPart == null || localPart.isEmpty()) {
			throw new IllegalArgumentException("Null or empty localPart");
		}

		logger.info("namespaceURI:" + namespaceURI);
		logger.info("localPart:" + localPart);

		SKOSSearcher searcher = getSkosServer().getSKOSSearcher();
		SKOSConcept concept = searcher.searchConceptByURI(namespaceURI,
				localPart);

		if (concept == null) {
			logger.info("no concept found, returning null");
			return null;
		}

		String preLabel = concept.getPrefLabel();
		QName q = concept.getQName();
		String origin = getSkosServer().getOrigin(q);
		String uri = q.getNamespaceURI() + q.getLocalPart();
		String skosCode = concept.getSKOSFormat();
		List<String> altLabel = concept.getAltLabels();
		List<String> scopeNotes = concept.getScopeNote();

		TreeMap<String, QName> broader = concept.getBroaders();
		Iterator<String> it = broader.keySet().iterator();
		HashMap<String, String> broaders = new HashMap<String, String>();
		while (it.hasNext()) {
			String key = it.next();
			QName qq = broader.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			broaders.put(key, value);
		}
		TreeMap<String, QName> narrower = concept.getNarrowers();
		Iterator<String> itn = narrower.keySet().iterator();
		HashMap<String, String> narrowers = new HashMap<String, String>();
		while (itn.hasNext()) {
			String key = itn.next();
			QName qq = narrower.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			narrowers.put(key, value);
		}

		TreeMap<String, QName> related = concept.getRelated();
		Iterator<String> itr = related.keySet().iterator();
		HashMap<String, String> relateds = new HashMap<String, String>();
		while (itr.hasNext()) {
			String key = itr.next();
			QName qq = related.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			relateds.put(key, value);
		}

		ConceptProxy cp = new ConceptProxy(origin, preLabel, uri);
		cp.put(altLabel, broaders, narrowers, relateds, scopeNotes, skosCode);
		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTagsBasedOnFilePath
	 * (java.lang.String, java.util.List, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTagsBasedOnFilePath(
			final String sourceFilePath, final List<String> openedVocabularies,
			final int numTerms, final String algorithm) {
		logger.debug("getTags for " + sourceFilePath);

		SKOSTagger tagger = getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger.getTags(sourceFilePath,
				openedVocabularies, getSKOSSearcher(), numTerms,
				DEFAULT_MIN_OCCUR);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			preLabel = preLabel.replaceAll("\\(", "&#40;");
			preLabel = preLabel.replaceAll("\\)", "&#41;");
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTagsBasedOnFilePath
	 * (java.lang.String, java.util.List, int, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTagsBasedOnFilePath(final String input,
			final List<String> openedVocabularies, final int numTerms,
			final int minPhraseOccur, final String algorithm) {
		SKOSTagger tagger = getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger
				.getTags(input, openedVocabularies, getSKOSSearcher(),
						numTerms, minPhraseOccur);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			preLabel = preLabel.replaceAll("\\(", "&#40;");
			preLabel = preLabel.replaceAll("\\)", "&#41;");
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);

			Map<String, String> broaderMap = getStringMap(concept.getBroaders());
			cp.setBroader(broaderMap);

			Map<String, String> narrowerMap = getStringMap(concept
					.getNarrowers());
			cp.setNarrower(narrowerMap);
			cp.setAltLabel(concept.getAltLabels());
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getStringMap(java.util
	 * .Map)
	 */
	@Override
	public Map<String, String> getStringMap(final Map<String, QName> qnameMap) {
		Map<String, String> stringMap = new HashMap<String, String>();
		for (String key : qnameMap.keySet()) {
			QName value = qnameMap.get(key);
			stringMap.put(key, value.getNamespaceURI() + value.getLocalPart());
		}
		return stringMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTags(java.net.URL,
	 * java.util.List, int, int, boolean, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTags(final URL url,
			final List<String> openedVocabularies, final int maxHops,
			final int numTerms, final boolean diff, final int minOccur,
			final String algorithm) {
		SKOSTagger tagger = getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger.getTags(url, openedVocabularies,
				getSKOSSearcher(), maxHops, numTerms, diff, minOccur);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTagsAsTree(java.lang
	 * .String, java.util.List, int, int, java.lang.String)
	 */
	@Override
	public List<ConceptNode> getTagsAsTree(final String text,
			final List<String> openedVocabularies, final int maxHops,
			final int numTerms, final String algorithm) {
		SKOSTagger tagger = getSkosServer().getSKOSTagger(algorithm);
		List<ConceptNode> tree = tagger.getTagsAsTree(text, openedVocabularies,
				getSKOSSearcher(), maxHops, numTerms);
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getFirstConcept(java.
	 * lang.String)
	 */
	@Override
	public ConceptProxy getFirstConcept(final String vocabulary) {
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		SKOSScheme voc = vocabularyMap.get(vocabulary.toLowerCase());
		List<SKOSConcept> top = voc.getSubTopConceptIndex("a");
		QName value = top.get(0).getQName();
		ConceptProxy cp = getConceptByURI(value.getNamespaceURI(),
				value.getLocalPart());
		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#suggestTermsFor(java.
	 * lang.String, java.lang.String, int)
	 */
	@Override
	public List<AutocompleteTerm> suggestTermsFor(final String vocabulary,
			final String str, final int numTerms) throws Exception {
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		SKOSScheme voc = vocabularyMap.get(vocabulary.toLowerCase());
		return voc.suggestTermsFor(str, numTerms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#close()
	 */
	@Override
	public void close() {
		hiveContainer.shutdown();
	}

}