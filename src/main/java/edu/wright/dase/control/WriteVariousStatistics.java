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

import edu.wright.dase.model.CONSTANTS;

/**
 *
 * @author mdkamruzzamansarker
 */
public class WriteVariousStatistics {

    public void writeCalcTimes() throws IOException {
        BufferedWriter writer = null;

        String path = getClass().getClassLoader().getResource(CONSTANTS.RESULTSPATH).getPath() + "/times.txt";
        if (CONSTANTS.OSNAME.startsWith("Win")) {
            path = path.substring(1, path.length());
        }
        if (Files.exists(Paths.get(path))) {
            writer = new BufferedWriter(new FileWriter(path, true));
        } else {
            File f = new File(path);
            writer = new BufferedWriter(new FileWriter(f.getCanonicalPath(), true));
        }
        writer.write(".Q " + "\n");
        writer.write("Indexsearchduration: " + CONSTANTS.indexsearchduration + " milisec.\n");
        writer.write("FetchEachRelevantDoc: " + CONSTANTS.FetchEachRelevantDoc + " milisec.\n");
        writer.write("Displayduration: " + CONSTANTS.displayduration + " milisec.\n");
        writer.write("Totalduration: " + CONSTANTS.totalduration + " milisec.\n\n\n");

        writer.close();

    }

    public void writeOutputResults() throws IOException {

    }

}
