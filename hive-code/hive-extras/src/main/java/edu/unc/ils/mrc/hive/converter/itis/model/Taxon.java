package edu.unc.ils.mrc.hive.converter.itis.model;

import java.util.ArrayList;
import java.util.List;

public class Taxon {

	private String tsn;
	private String name;
	private String longName;
	private List<String> children;
	private String parent;
	private List<String> synonyms;
	private String taxonomic_rank;

	public Taxon(final String tsn) {
		this.tsn = tsn;
		children = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(final String longName) {
		this.longName = longName;
	}

	public List<String> getChilds() {
		return children;
	}

	public void setChilds(final List<String> children) {
		this.children = children;
	}

	public void addchild(final String child) {
		children.add(child);
	}

	public String getParent() {
		return parent;
	}

	public void setParent(final String parent) {
		this.parent = parent;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(final List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public String getTaxonomic_rank() {
		return taxonomic_rank;
	}

	public void setTaxonomic_rank(final String taxonomicRank) {
		taxonomic_rank = taxonomicRank;
	}

	public String getTsn() {
		return tsn;
	}

	public String toSKOS() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<rdf:Description rdf:about=\"http://www.itis.gov/itis/");
		buffer.append(tsn);
		buffer.append("#concept\">\n");
		buffer.append("\t<rdf:type rdf:resource=\"http://www.w3.org/2004/02/skos/core#Concept\"/>\n");
		buffer.append("\t<skos:inScheme rdf:resource=\"http://www.itis.gov/itis#conceptScheme\"/>\n");

		if (children != null) {
			for (String c : children) {
				buffer.append("\t<skos:narrower rdf:resource=\"http://www.itis.gov/itis/");
				buffer.append(c);
				buffer.append("#concept\"/>\n");
			}
		}
		if (parent != null) {
			buffer.append("\t<skos:broader rdf:resource=\"http://www.itis.gov/itis/");
			buffer.append(parent);
			buffer.append("#concept\"/>\n");
		}
		buffer.append("\t<skos:prefLabel>");
		buffer.append(longName);
		buffer.append("</skos:prefLabel>\n");

		if (synonyms != null) {
			for (String alt : synonyms) {
				buffer.append("\t<skos:altLabel>");
				buffer.append(alt);
				buffer.append("</skos:altLabel>\n");
			}
		}
		buffer.append("</rdf:Description>");

		return buffer.toString();
	}
}
