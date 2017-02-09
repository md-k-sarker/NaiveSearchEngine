/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.control;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wright.dase.model.CONSTANTS;
import edu.wright.dase.model.StandardAnalyzerCustomed;
import edu.wright.dase.ui.SearchGUI;

/**
 *
 * @author mdkamruzzamansarker
 */
public class SearchEngine {

	final static Logger logger = LoggerFactory.getLogger(SearchGUI.class);

	private IndexSearcher searcher = null;
	private QueryParser qparser = null;
	MultiFieldQueryParser mfqparser = null;

	/*
	 * Create SearchEngine Instance
	 * 
	 * This creates Lucene IndexSearcher instance
	 */
	public SearchEngine() throws IOException {
		
		// constructor
		String path = CONSTANTS.INDEXPATH;

		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(path))));

		System.out.println("Total indexed docs: " + searcher.getIndexReader().numDocs());
	}

	/*
	 * Build Query
	 */
	public Query buildQuery(String querystring, int n) throws ParseException {

		mfqparser = new MultiFieldQueryParser(new String[] { "title", "abstract", "content" },
				new StandardAnalyzerCustomed());

		// qparser = new QueryParser("content", new StandardAnalyzerCustomed());
		// BooleanQuery booleanQuery = new BooleanQuery();
		// Query query1 = new TermQuery(new Term("content", querystring));
		// Query query2 = new TermQuery(new Term("title", querystring));
		// booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
		// booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		Query query = mfqparser.parse(querystring);
		System.out.println("Query after build: " + query.toString());

		return query;
	}

	/*
	 * Perform Search on predefined index files
	 * 
	 * First build query from raw string
	 * 
	 * Then search
	 */
	public TopDocs performSearch(String queryAsRawString, int maximumAllowedResult) throws ParseException, IOException {

		// Build Query
		Query query = buildQuery(queryAsRawString, maximumAllowedResult);

		// Search Query
		TopDocs topDocs = searcher.search(query, maximumAllowedResult);
		return topDocs;
	}

	public Document getDocument(int docId) throws IOException {
		return searcher.doc(docId);
	}

}
