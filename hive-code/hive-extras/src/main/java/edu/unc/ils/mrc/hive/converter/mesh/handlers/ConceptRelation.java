package edu.unc.ils.mrc.hive.converter.mesh.handlers;

/**
 * Represents a concept relation as parsed from the MeSH XML.
 */
public class ConceptRelation {

	String relation = null;

	String concept1 = null;
	String concept2 = null;

	public ConceptRelation() {
	}

	public ConceptRelation(final String relation, final String concept1,
			final String concept2) {
		this.relation = relation;
		this.concept1 = concept1;
		this.concept2 = concept2;

	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}

	public String getConcept1() {
		return concept1;
	}

	public void setConcept1(final String concept1) {
		this.concept1 = concept1;
	}

	public String getConcept2() {
		return concept2;
	}

	public void setConcept2(final String concept2) {
		this.concept2 = concept2;
	}
}
