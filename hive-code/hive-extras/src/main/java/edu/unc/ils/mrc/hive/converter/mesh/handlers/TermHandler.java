package edu.unc.ils.mrc.hive.converter.mesh.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for the TermList->Term element
 */
public class TermHandler extends MeshHandler {
	private static final Log logger = LogFactory.getLog(TermHandler.class);

	Term term = new Term();

	public TermHandler(final XMLReader parser, final DefaultHandler parent,
			final String preferred) {
		super(parser, parent);
		term.setPreferred(preferred.equals("Y"));
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {
		logger.trace("startElement: " + uri + "," + localName + "," + qName
				+ "," + attributes);

		if (qName.equals("TermUI")) {
			currentValue = "";
		} else if (qName.equals("String")) {
			currentValue = "";
		}
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		logger.trace("endElement: " + uri + "," + localName + "," + qName);

		if (qName.equals("TermUI")) {
			term.setTermId(currentValue);
		} else if (qName.equals("String")) {
			term.setTermValue(currentValue);
			parser.setContentHandler(parent);
		}
	}

	/**
	 * Returns the parsed Term
	 * 
	 * @return
	 */
	public Term getTerm() {
		return term;
	}
}
