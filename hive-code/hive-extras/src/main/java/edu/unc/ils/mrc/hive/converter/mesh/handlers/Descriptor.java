package edu.unc.ils.mrc.hive.converter.mesh.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a descriptor as parsed from the MeSH Descriptor element
 */
public class Descriptor {

	String descriptorId = null;

	List<Concept> concepts = new ArrayList<Concept>();;
	List<String> relatedDescriptors = new ArrayList<String>();
	List<String> treeNumbers = new ArrayList<String>();

	public Descriptor() {
	}

	public Descriptor(final String descriptorId) {
		this.descriptorId = descriptorId;
	}

	public String getDescriptorId() {
		return descriptorId;
	}

	public void setDescriptorId(final String descriptorId) {
		this.descriptorId = descriptorId;
	}

	public void setConcepts(final List<Concept> concepts) {
		this.concepts = concepts;
	}

	public List<Concept> getConcepts() {
		return concepts;
	}

	public List<String> getRelatedDescriptors() {
		return relatedDescriptors;
	}

	public void setRelatedDescriptors(final List<String> relatedDescriptors) {
		this.relatedDescriptors = relatedDescriptors;
	}

	public void addTreeNumber(final String treeNumber) {
		treeNumbers.add(treeNumber);
	}

	public List<String> getTreeNumbers() {
		return treeNumbers;
	}
}
