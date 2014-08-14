package edu.unc.ils.mrc.hive.api;

import java.util.ArrayList;
import java.util.List;

public class ConceptNode {
	private String uri;
	private String label;
	private List<ConceptNode> children = new ArrayList<ConceptNode>();
	private List<String> altLabels = new ArrayList<String>();

	public String getUri() {
		return uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public List<ConceptNode> getChildren() {
		return children;
	}

	public void setChildren(final List<ConceptNode> children) {
		this.children = children;
	}

	public void addChild(final ConceptNode child) {
		children.add(child);
	}

	public List<String> getAltLabels() {
		return altLabels;
	}

	public void setAltLabels(final List<String> altLabels) {
		this.altLabels = altLabels;
	}

	@Override
	public String toString() {
		return toString(this);
	}

	private String toString(final ConceptNode node) {
		String str = node.getUri() + "," + node.getLabel();
		for (ConceptNode child : node.getChildren()) {
			str += "|" + toString(child);
		}
		return str;
	}
}
