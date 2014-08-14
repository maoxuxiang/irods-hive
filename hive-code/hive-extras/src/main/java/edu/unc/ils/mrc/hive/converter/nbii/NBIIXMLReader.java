package edu.unc.ils.mrc.hive.converter.nbii;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/*
 * Problems to read repgen.xml. Not use this class for the moment!!!
 */
public class NBIIXMLReader extends DefaultHandler {

	private XMLReader xr;
	private String currentElement;
	private Concept concept;
	private Thesaurus thesaurus;
	private boolean ok;

	public NBIIXMLReader() {
		try {
			xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(this);
			xr.setErrorHandler(this);
		} catch (SAXException e) {
			System.err.println("Problem with XMLReader inicialization");
			e.printStackTrace();
		}

		thesaurus = new SKOSThesaurus();
		ok = false;
	}

	public Thesaurus readThesaurus(final String file) {
		FileReader fr;
		try {
			fr = new FileReader(file);
			xr.parse(new InputSource(fr));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thesaurus;
	}

	public Thesaurus readThesaurus(final String[] file) {
		return null;
		// TODO
	}

	@Override
	public void startDocument() {
		System.out.println("Starting XML document");
	}

	@Override
	public void endDocument() {
		System.out.println("Finishing XML document");
	}

	@Override
	public void startElement(final String uri, final String name,
			final String qName, final Attributes atts) {
		currentElement = name;
		if (currentElement.equals("DESCRIPTOR")) {
			concept = new SKOSConcept("http://thesaurus.nbii.gov/");
			ok = true;
		}
	}

	@Override
	public void endElement(final String uri, final String name,
			final String qName) {
		if (currentElement.equals("UPD") && ok) {
			thesaurus.addConcept(concept);
			ok = false;
		}
	}

	@Override
	public void characters(final char[] ch, final int start, final int end)
			throws SAXException {
		String s;
		s = new String(ch, start, end);
		s = s.trim();
		if (currentElement.equals("DESCRIPTOR") && !s.equals("")) {
			concept.setPrefLabel(s);
			concept.setUri(concept.getUri() + s);
			if (concept.getUri().contains(" ")) {
				concept.setUri(concept.getUri().replaceAll(" ", "-"));
			}
		}
		if (currentElement.equals("BT") && !s.equals("")) {
			concept.setBroader(s);
		}
		if (currentElement.equals("UF") && !s.equals("")) {
			concept.setAltLabel(s);
		}
		if (currentElement.equals("NT") && !s.equals("")) {
			concept.setNarrower(s);
		}
		if ((currentElement.equals("SN") || currentElement.equals("SC"))
				&& !s.equals("")) {
			concept.setScopeNote(s);
		}
		if (currentElement.equals("RT") && !s.equals("")) {
			concept.setRelated(s);
		}
	}

	public static void main(final String[] args) throws FileNotFoundException,
			IOException, SAXException {
		NBIIXMLReader lector = new NBIIXMLReader();
		// lector.leer("/home/jose/Desktop/qual2009.xml");
		lector.readThesaurus("/usr/local/hive/sources/nbii/repgen.xml");
		System.out.println("Thesaurus Size: " + lector.thesaurus.getSize());
		lector.thesaurus.printThesaurus("/tmp/nbii.rdf");

	}

}
