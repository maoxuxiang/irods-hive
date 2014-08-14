package edu.unc.ils.mrc.hive.converter.embne;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MARCtoSKOS {
	public static String inputMARCfile = "C:\\hive\\HIVE-ES\\para_skos2.mrc";
	public static String resultsFileName = "C:\\hive\\HIVE-ES\\marc21format2.txt";
	public static PrintWriter outputStream = null;
	public static String skosFileName = "C:\\hive\\HIVE-ES\\embne.rdf";
	public static PrintWriter skosOutputStream = null;
	public static MARCtoSKOSConverter conv = new MARCtoSKOSConverter();

	public static void main(final String[] args) {
		createOutputFiles();
		conv.writeSKOSheader(skosOutputStream);
		conv.writeSKOSConceptSchemes(skosOutputStream);
		conv.readInputFile(inputMARCfile, skosOutputStream, outputStream);
		conv.writeSKOSfooter(skosOutputStream);
		skosOutputStream.close();
		outputStream.close();
	}

	public static void createOutputFiles() {
		try { // MARC21 format
			outputStream = new PrintWriter(resultsFileName);
		} catch (FileNotFoundException e) {
			System.out.println("Error opening file " + resultsFileName);
			System.exit(0);
		}
		try { // SKOS format
			skosOutputStream = new PrintWriter(skosFileName, "UTF-8");
		} catch (Exception e) {
			System.out.println("Error opening file " + skosFileName);
			System.exit(0);
		}
	}

}
