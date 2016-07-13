/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utility.CONSTANTS;
import utility.StandardAnalyzerCustomed;

/**
 *
 * @author mdkamruzzamansarker
 */
public class SearchEngine {

    private IndexSearcher searcher = null;
    private QueryParser qparser = null;
    MultiFieldQueryParser mfqparser = null;

    public SearchEngine() throws IOException {
        //constructor 
        String path = getClass().getClassLoader().getResource(CONSTANTS.indexpath).getPath();
        if (CONSTANTS.OSName.startsWith("Win")) {
            path = path.substring(1, path.length());
        }
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(path))));

        System.out.println("Total indexed docs: " + searcher.getIndexReader().numDocs());
    }

    public Query buildQuery(String querystring, int n) throws ParseException {

        mfqparser = new MultiFieldQueryParser(new String[]{"title", "abstract", "content"}, new StandardAnalyzerCustomed());

//        qparser = new QueryParser("content", new StandardAnalyzerCustomed());
//        BooleanQuery booleanQuery = new BooleanQuery();
//        Query query1 = new TermQuery(new Term("content", querystring));
//        Query query2 = new TermQuery(new Term("title", querystring));
//        booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
//        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        Query query = mfqparser.parse(querystring);
        System.out.println("Query after build: " + query.toString());

        return query;
    }

    public TopDocs performSearch(String querystring, int n) throws ParseException, IOException {

        // Build Query
        Query Q = buildQuery(querystring, n);

        //Search Query
        TopDocs td = searcher.search(Q, n);
        return td;
    }

    public Document getDocument(int docId) throws IOException {
        return searcher.doc(docId);
    }

}
