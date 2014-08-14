package maui.util;

import java.util.HashMap;
import java.util.Vector;

import com.aliasi.medline.Article;
import com.google.gwt.user.client.ui.Anchor;

public class Candidate {

	/** Normalized string or vocabulary id */
	String name;

	/** The original full form as it appears in the document */
	String fullForm;

	/** The title of the descriptor in the vocabulary */
	String title;

	/** Number of occurrences of the candidate in the document */
	int frequency;

	/** Normalized frequenc */
	double termFrequency;

	/** Position of the first occurrence */
	double firstOccurrence;

	/** Position of the last occurrence */
	double lastOccurrence;

	/** Wikipedia keyphraseness */
	double wikipKeyphraseness = 0;

	/** Total wikipedia keyphraseness */
	double totalWikipKeyphraseness = 0;

	Anchor anchor = null;

	Vector<Anchor> anchors = null;

	/**
	 * HashMap to store occurrence frequencies of all full forms
	 */
	HashMap<String, Counter> fullForms;

	/**
	 * Constructor for the first occurrence of a candidate
	 */
	public Candidate(final String name, final String fullForm,
			final int firstOccurrence) {
		this.name = name;
		frequency = 1;

		this.firstOccurrence = firstOccurrence;
		lastOccurrence = firstOccurrence;
		this.fullForm = fullForm;

		fullForms = new HashMap<String, Counter>();
		fullForms.put(fullForm, new Counter());

	}

	public Candidate(final String name, final String fullForm,
			final int firstOccurrence, final Anchor anchor,
			final double probability) {

		this.name = name;
		frequency = 1;

		this.firstOccurrence = firstOccurrence;
		lastOccurrence = firstOccurrence;
		this.fullForm = fullForm;

		fullForms = new HashMap<String, Counter>();
		fullForms.put(fullForm, new Counter());

		totalWikipKeyphraseness = probability;
		wikipKeyphraseness = probability;
		this.anchor = anchor;
	}

	public Candidate getCopy() {
		Candidate newCandidate = new Candidate(name, fullForm,
				(int) firstOccurrence);

		newCandidate.frequency = frequency;
		newCandidate.termFrequency = termFrequency;
		newCandidate.firstOccurrence = firstOccurrence;
		newCandidate.lastOccurrence = lastOccurrence;
		newCandidate.fullForms = fullForms;
		newCandidate.totalWikipKeyphraseness = totalWikipKeyphraseness;
		newCandidate.wikipKeyphraseness = wikipKeyphraseness;
		newCandidate.anchor = anchor;
		return newCandidate;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Anchor getAnchor() {
		return anchor;
	}

	public double getWikipKeyphraseness() {
		return wikipKeyphraseness;
	}

	public double getTotalWikipKeyphraseness() {
		return totalWikipKeyphraseness;
	}

	/**
	 * Returns all document phrases that were mapped to this candidate.
	 * 
	 * @return HashMap in which the keys are the full forms and the values are
	 *         their frequencies
	 */
	public HashMap<String, Counter> getFullForms() {
		return fullForms;
	}

	/**
	 * Records the occurrence position and the full form of a candidate
	 * 
	 * @param fullForm
	 * @param occurrence
	 */
	public void recordOccurrence(final String fullForm, final int occurrence) {
		frequency++;

		lastOccurrence = occurrence;
		if (fullForms.containsKey(fullForm)) {
			fullForms.get(fullForm).increment();
		} else {
			fullForms.put(fullForm, new Counter());
		}

		if (totalWikipKeyphraseness != 0) {
			totalWikipKeyphraseness += wikipKeyphraseness;

		}
	}

	/**
	 * In case of free indexing, e.g. tagging or keyphrase extraction, retrieves
	 * the most frequent full form for a given candidate.
	 * 
	 * @return best full form of a candidate
	 */
	public String getBestFullForm() {
		int maxFrequency = 0;
		String bestFullForm = "";
		for (String form : fullForms.keySet()) {
			int formFrequency = fullForms.get(form).value();
			if (formFrequency > maxFrequency) {
				bestFullForm = form;
				maxFrequency = formFrequency;
			}
		}
		return bestFullForm;
	}

	public String getName() {
		return name;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getTermFrequency() {
		return termFrequency;
	}

	public double getFirstOccurrence() {
		return firstOccurrence;
	}

	public double getLastOccurrence() {
		return lastOccurrence;
	}

	public double getSpread() {
		return lastOccurrence - firstOccurrence;
	}

	/**
	 * Normalizes all occurrence positions and frequencies by the total values
	 * in the given document
	 */
	public void normalize(final int totalFrequency, final int documentLength) {
		termFrequency = frequency / (double) totalFrequency;
		firstOccurrence = firstOccurrence / documentLength;
		lastOccurrence = lastOccurrence / documentLength;

	}

	@Override
	public String toString() {
		return name + " (" + fullForm + "," + title + ")";
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out
				.println("This is a method for creating candidate topics in a document");
	}

	public String getIdAndTitle() {

		return name + ": " + title;
	}

	/**
	 * If two candidates were disambiguated to the same topic, their values are
	 * merged.
	 * 
	 * @param previousCandidate
	 */
	public void mergeWith(final Candidate previousCandidate) {

		// name stays the same
		// full form stays the same
		// title stays the same

		// frequency increments
		frequency += previousCandidate.frequency;

		// term frequency increments
		termFrequency += previousCandidate.termFrequency;

		// update first occurrence to the earliest one
		double previous = previousCandidate.firstOccurrence;
		if (previous < firstOccurrence) {
			firstOccurrence = previous;
		}

		// and the opposite with the last occurrence
		previous = previousCandidate.lastOccurrence;
		if (previous > lastOccurrence) {
			lastOccurrence = previous;
		}

		// increment wikip keyphr
		totalWikipKeyphraseness += previousCandidate.totalWikipKeyphraseness;
		wikipKeyphraseness += previousCandidate.wikipKeyphraseness;

		// anchor should be added to the list of anchors
		if (anchors == null) {
			anchors = new Vector<Anchor>();
			anchors.add(anchor);
		}
		anchors.add(previousCandidate.anchor);

		// full forms should be added to the hash of full forms
		if (fullForms == null) {
			System.err.println("Is it ever empty??? ");
			fullForms = previousCandidate.fullForms;
		}
		HashMap<String, Counter> prevFullForms = previousCandidate.fullForms;
		for (String prevForm : prevFullForms.keySet()) {
			int count = prevFullForms.get(prevForm).value();
			if (fullForms.containsKey(prevForm)) {
				fullForms.get(prevForm).increment(count);
			} else {
				fullForms.put(prevForm, new Counter(count));
			}
		}

	}

	/**
	 * Retrieves all recorded info about a candidate
	 * 
	 * @return info about a candidate formatted as a string
	 */
	public String getInfo() {

		String result = "";

		String allFullForms = "";
		for (String form : fullForms.keySet()) {
			allFullForms += form + " (" + fullForms.get(form) + "), ";
		}

		String allAnchors = "";
		if (anchors != null) {
			for (Anchor anch : anchors) {
				allAnchors += anch + ", ";
			}
		}

		result += "\tName: " + name + "\n";
		result += "\tFullForm: " + fullForm + "\n";
		result += "\tArticle: " + article + "\n";
		result += "\tAllFullForms: " + allFullForms + "\n";
		result += "\tTitle: " + title + "\n";
		result += "\tFreq " + frequency + "\n";
		result += "\tTermFreq: " + termFrequency + "\n";
		result += "\tFirstOcc: " + firstOccurrence + "\n";
		result += "\tLastOcc: " + lastOccurrence + "\n";
		result += "\tWikipKeyphr: " + wikipKeyphraseness + "\n";
		result += "\tTotalWikipKeyphr: " + totalWikipKeyphraseness + "\n";
		result += "\tAnchor: " + anchor + "\n";
		result += "\tAnchors: " + allAnchors + "\n";

		return result;
	}

	public Vector<Anchor> getAnchors() {
		return anchors;
	}

	private Article article;

	public void setArticle(final Article article) {
		this.article = article;
	}

	public Article getArticle() {
		return article;
	}

}
