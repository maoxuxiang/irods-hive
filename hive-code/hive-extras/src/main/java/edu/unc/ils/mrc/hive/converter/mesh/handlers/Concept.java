package edu.unc.ils.mrc.hive.converter.mesh.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a concept as parsed from the MeSH Concept element
 */
public class Concept {

	String conceptId = null;
	String conceptName = null;
	String descriptorId = null;

	boolean isPreferred = false;
	String scopeNote = null;
	List<Term> terms = new ArrayList<Term>();
	List<ConceptRelation> relations = new ArrayList<ConceptRelation>();

	public Concept() {
	}

	public Concept(final String conceptId, final String conceptName,
			final boolean isPreferred) {
		this.conceptId = conceptId;
		this.conceptName = conceptName;
		this.isPreferred = isPreferred;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(final String conceptId) {
		this.conceptId = conceptId;
	}

	public String getName() {
		return conceptName;
	}

	public void setName(final String conceptName) {
		this.conceptName = conceptName;
	}

	public boolean isPreferred() {
		return isPreferred;
	}

	public void setPreferred(final boolean isPreferred) {
		this.isPreferred = isPreferred;
	}

	public void setTerms(final List<Term> terms) {
		this.terms = terms;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public String getScopeNote() {
		return scopeNote;
	}

	public void setScopeNote(final String scopeNote) {
		this.scopeNote = scopeNote;
	}

	public List<ConceptRelation> getRelations() {
		return relations;
	}

	public void setRelations(final List<ConceptRelation> relations) {
		this.relations = relations;
	}

	public String getDescriptorId() {
		return descriptorId;
	}

	public void setDescriptorId(final String descriptorId) {
		this.descriptorId = descriptorId;
	}

}
