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
 * Handler for ConceptRelationList element
 */
public class ConceptRelationListHandler extends MeshHandler {
	private static final Log logger = LogFactory
			.getLog(ConceptRelationListHandler.class);

	List<ConceptRelation> relations = new ArrayList<ConceptRelation>();

	public ConceptRelationListHandler(final XMLReader parser,
			final DefaultHandler parent) {
		super(parser, parent);
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {
		logger.trace("startElement: " + uri + "," + localName + "," + qName
				+ "," + attributes);

		if (qName.equals("ConceptRelation")) {
			String relation = attributes.getValue("RelationName");

			ConceptRelationHandler handler = new ConceptRelationHandler(parser,
					this, relation);
			childHandler = handler;
			parser.setContentHandler(handler);
		}
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		logger.trace("endElement: " + uri + "," + localName + "," + qName);

		if (qName.equals("ConceptRelation")) {
			ConceptRelation relation = ((ConceptRelationHandler) childHandler)
					.getRelation();
			relations.add(relation);
		} else if (qName.equals("ConceptRelationList")) {
			parser.setContentHandler(parent);
		}
	}

	/**
	 * Returns the list of concept relations
	 * 
	 * @return
	 */
	public List<ConceptRelation> getRelations() {
		return relations;
	}

}
