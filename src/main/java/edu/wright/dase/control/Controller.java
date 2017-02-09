/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wright.dase.model.CONSTANTS;
import edu.wright.dase.ui.SearchGUI;

/**
 *
 * @author mdkamruzzamansarker
 */
public class Controller {
	
	final static Logger logger = LoggerFactory.getLogger(SearchGUI.class);

	int totalDocsFound = 0;
	public static int counter = 0;
	SearchGUI searchGUI;

	public Controller(SearchGUI searchGUI) {
		this.searchGUI = searchGUI;
	}

	private void createList(TopDocs topDocs, SearchEngine se) throws IOException {
		ScoreDoc[] hits = topDocs.scoreDocs;
		int totalresults = 0;
		totalresults = hits.length;

		if (hits.length > 10) {
			for (int i = 0; i < 10; i++) {

			}
		} else {
			for (int i = 0; i < 10; i++) {

			}
		}
		// retrieve each matching document from the ScoreDoc array
		for (int i = 0; i < hits.length; i++) {
			Document doc = se.getDocument(hits[i].doc);
			System.out.println("doc: " + doc.get("id") + " score: " + hits[i].score);

		}
	}

	BufferedWriter writer = null;

	private BufferedWriter openwriter() throws IOException {
		if (CONSTANTS.RESULTSPATH != null) {
			String path = CONSTANTS.RESULTSPATH + "/result.txt";
			
			if (Files.exists(Paths.get(path))) {
				writer = new BufferedWriter(new FileWriter(path, true));
			} else {
				File f = new File(path);
				writer = new BufferedWriter(new FileWriter(f.getCanonicalPath(), true));
			}
		} else {
			System.out.println("Please specify output folder first");
		}
		return writer;
	}

	private int seperateResults(TopDocs topDocs, SearchEngine se) throws IOException {
		ScoreDoc[] hits = topDocs.scoreDocs;

		// calculate time to fetch a doc
		long startTime = System.nanoTime();
		Document d = se.getDocument(hits[0].doc);
		d.get("id");
		long endTime = System.nanoTime();
		CONSTANTS.FetchEachRelevantDoc = (endTime - startTime) / 1000000;

		int freqs = 1;
		double score = -1;
		writer = openwriter();
		writer.write("Id\tOutputDocs \n");

		// retrieve each matching document from the ScoreDoc array
		for (int i = 0; i < hits.length; i++) {
			Document doc = se.getDocument(hits[i].doc);
			if (hits[i].score == score && hits[i].score > CONSTANTS.THRESHOLD) {
				freqs++;
			} else if (hits[i].score > CONSTANTS.THRESHOLD) {

				// print on system console
				System.out.println("doc: " + doc.get("id") + " score: " + hits[i].score);

				// show on user interface
				searchGUI.addResult(doc.get("id"), hits[i].score);

				// write on output file
				writer.write(counter + 1 + "\t" + doc.get("id") + "\tScore: " + hits[i].score + "\n");

				freqs = 1;
				totalDocsFound++;
			}
			score = hits[i].score;
		}

		counter++;
		System.out.println("Total Documents found: " + totalDocsFound);
		writer.write("\t\t" + "\n");
		writer.close();
		return totalDocsFound;

		// createList(topDocs, se);
	}

	/*
	 * Entry point to search the query on predefined index file.
	 * 
	 * Creates an Instance of SearchEngine and Execute search
	 * 
	 */
	public int search(String s) throws IOException, ParseException {

		searchGUI.getResultPanel().removeAll();

		writer = openwriter();
		writer.write(".Q " + (counter + 1) + "\n " + s + "\n");
		writer.close();

		// instantiate the  engine
		SearchEngine searchEngine = new SearchEngine();
		totalDocsFound = 0;

		long startTime = System.nanoTime();
		TopDocs topDocs = searchEngine.performSearch(s, 1000);
		long endTime = System.nanoTime();

		/*
		 * divide by 1000000 to get milliseconds.
		 */
		CONSTANTS.indexsearchduration = (endTime - startTime) / 1000000;

		// obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
		ScoreDoc[] hits = topDocs.scoreDocs;

		if (hits.length > 0) {
			startTime = System.nanoTime();
			totalDocsFound = seperateResults(topDocs, searchEngine);
			endTime = System.nanoTime();

			CONSTANTS.displayduration = (endTime - startTime) / 1000000;
		} else {
			System.out.println("Total document found: " + hits.length);
			totalDocsFound = 0;
		}

		searchGUI.getResultPanel().validate();
		searchGUI.getResultPanel().repaint();

		searchGUI.getResultScrollPane().validate();
		searchGUI.getResultScrollPane().repaint();

		return totalDocsFound;
	}

}
