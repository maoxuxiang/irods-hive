package edu.unc.ils.mrc.hive.ir.lucene.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Analyzer used by HIVE ConceptIndexer and ConceptMultiSearcher.
 */
public class HIVEAnalyzer extends Analyzer {
	@Override
	public final TokenStream tokenStream(final String fieldName,
			final Reader reader) {
		return new PorterStemFilter(new LowerCaseTokenizer(reader));
	}
}
