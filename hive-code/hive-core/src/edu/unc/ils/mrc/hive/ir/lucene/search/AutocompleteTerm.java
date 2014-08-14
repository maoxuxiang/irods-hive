package edu.unc.ils.mrc.hive.ir.lucene.search;

public class AutocompleteTerm {

	protected String id;
	protected String value;

	public AutocompleteTerm(final String id, final String value) {
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}
