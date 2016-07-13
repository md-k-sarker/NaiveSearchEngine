/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import indexing.BuildIndex;
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
import utility.CONSTANTS;
import static utility.CONSTANTS.ENCODING;

/**
 *
 * @author mdkamruzzamansarker
 */
public class Controller {

    int totalDocsFound = 0;
    public static int counter = 0;

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

    public class Result {

    }
    BufferedWriter writer = null;

    private boolean writeResult() throws IOException {

        return false;
    }

    private BufferedWriter openwriter() throws IOException {
        String path = getClass().getClassLoader().getResource(CONSTANTS.resultspath).getPath() + "/result.txt";
        if (CONSTANTS.OSName.startsWith("Win")) {
            path = path.substring(1, path.length());
        }
        if (Files.exists(Paths.get(path))) {
            writer = new BufferedWriter(new FileWriter(path, true));
        } else {
            File f = new File(path);
            writer = new BufferedWriter(new FileWriter(f.getCanonicalPath(), true));
        }
        return writer;
    }

    private int seperateResults(TopDocs topDocs, SearchEngine se) throws IOException {
        ScoreDoc[] hits = topDocs.scoreDocs;

        //calculate time to fetch a doc
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
                System.out.println("doc: " + doc.get("id") + " score: " + hits[i].score);
                //writer.write( doc.get("id") + "\tscore: " + hits[i].score );
                SearchGUI sg =new SearchGUI();
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

        //createList(topDocs, se);
    }

    protected int search(String s) throws IOException, ParseException {

        writer = openwriter();
        writer.write(".Q " + (counter + 1) + "\n " + s + "\n");
        //writer.write("\t\t"+"##############################"+"\n");
        writer.close();
        // instantiate the search engine
        SearchEngine se = new SearchEngine();
        totalDocsFound = 0;

        long startTime = System.nanoTime();
        TopDocs topDocs = se.performSearch(s, 1000);
        long endTime = System.nanoTime();

        CONSTANTS.indexsearchduration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
        ScoreDoc[] hits = topDocs.scoreDocs;

        if (hits.length > 0) {
            startTime = System.nanoTime();
            totalDocsFound = seperateResults(topDocs, se);
            endTime = System.nanoTime();
            CONSTANTS.displayduration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        } else {
            System.out.println("Total document found: " + hits.length);
            totalDocsFound = 0;
        }
        return totalDocsFound;
    }

}
