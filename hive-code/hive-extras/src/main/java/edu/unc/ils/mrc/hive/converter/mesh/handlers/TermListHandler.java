package edu.unc.ils.mrc.hive.converter.mesh.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for the TermsList element. Collects a list of Term objects.
 */
public class TermListHandler extends MeshHandler {
	private static final Log logger = LogFactory.getLog(TermListHandler.class);

	List<Term> terms = new ArrayList<Term>();

	public TermListHandler(final XMLReader parser, final DefaultHandler parent) {
		super(parser, parent);
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {
		logger.trace("startElement: " + uri + "," + localName + "," + qName
				+ "," + attributes);

		if (qName.equals("Term")) {
			String preferred = attributes.getValue("ConceptPreferredTermYN");

			TermHandler handler = new TermHandler(parser, this, preferred);
			childHandler = handler;
			parser.setContentHandler(handler);
		}
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		logger.trace("endElement: " + uri + "," + localName + "," + qName);

		if (qName.equals("Term")) {
			Term term = ((TermHandler) childHandler).getTerm();
			terms.add(term);
		} else if (qName.equals("TermList")) {
			parser.setContentHandler(parent);
		}
	}

	/**
	 * Returns list of terms from this TermList
	 * 
	 * @return
	 */
	public List<Term> getTerms() {
		return terms;
	}
}
