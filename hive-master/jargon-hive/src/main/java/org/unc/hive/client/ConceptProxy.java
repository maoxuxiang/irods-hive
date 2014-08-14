package org.unc.hive.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Refactoring of <code>ConceptProxy</code> in hive-web.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class ConceptProxy  {
	private boolean topLevel = false;
	private String preLabel = "";
	private String URI = "";
	private String origin = "";
	private String skosCode = "";
	private Map<String, String> narrower = new HashMap<String, String>();
	private Map<String, String> broader = new HashMap<String, String>();
	private Map<String, String> related = new HashMap<String, String>();
	private List<String> altLabel = new ArrayList<String>();
	private List<String> scopeNotes = new ArrayList<String>();
	private boolean isleaf = false;
	private double score = 0d;
	/**
	 * allows specification that this concept is 'selected' in the current
	 * context. e.g. this concept has been applied to a resource, such as an
	 * iRODS file or collection. This value is meant for custom client use, and
	 * is not set by HIVE itself.
	 */
	private boolean selected = false;

	public ConceptProxy() {

	}

	public ConceptProxy(final String prelabel, final String uri) {
		preLabel = prelabel;
		URI = uri;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final boolean isleaf) {
		this.origin = origin;
		preLabel = prelabel;
		URI = uri;
		this.isleaf = isleaf;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri) {
		this.origin = origin;
		preLabel = prelabel;
		URI = uri;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final double score) {
		this.origin = origin;
		preLabel = prelabel;
		URI = uri;
		this.score = score;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final String skosCode) {
		this.origin = origin;
		preLabel = prelabel;
		URI = uri;
		this.skosCode = skosCode;
	}

	public double getScore() {
		return score;
	}

	public String getSkosCode() {
		return skosCode;
	}

	public boolean getIsLeaf() {
		return isleaf;
	}

	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return origin;
	}

	public void setPreLabel(final String prelabel) {
		preLabel = prelabel;
	}

	public String getPreLabel() {
		return preLabel;
	}

	public void setURI(final String uri) {
		URI = uri;
	}

	public String getURI() {
		return URI;
	}

	public void setNarrower(final Map<String, String> map) {
		narrower = new HashMap<String, String>(map);
	}

	public Map<String, String> getNarrower() {
		return narrower;
	}

	public void setBroader(final Map<String, String> map) {
		broader = new HashMap<String, String>(map);
	}

	public Map<String, String> getBroader() {
		return broader;
	}

	public void setRelated(final HashMap<String, String> map) {
		related = new HashMap<String, String>(map);
	}

	public Map<String, String> getRelated() {
		return related;
	}

	public void setAltLabel(final List<String> altlabel) {
		altLabel = altlabel;
	}

	public List<String> getAltLabel() {
		return altLabel;
	}

	public void setScopeNotes(final List<String> notes) {
		scopeNotes = notes;
	}

	public List<String> getScopeNotes() {
		return scopeNotes;
	}

	/**
	 * Is this concept the 'top level' set of terms for a vocabulary? In this
	 * case there is no URI
	 * 
	 * @return
	 */
	public boolean isTopLevel() {
		return topLevel;
	}

	/**
	 * Indicate that this concept proxy is the top level of a vocabulary 'tree'
	 * 
	 * @param topLevel
	 */
	public void setTopLevel(final boolean topLevel) {
		this.topLevel = topLevel;
	}

	public void put(final List<String> altlabel,
			final Map<String, String> broader,
			final Map<String, String> narrower,
			final Map<String, String> related, final List<String> scopeNote,
			final String skosCode) {
		altLabel = altlabel;
		this.broader = broader;
		this.narrower = narrower;
		this.related = related;
		scopeNotes = scopeNote;
		this.skosCode = skosCode;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}
}