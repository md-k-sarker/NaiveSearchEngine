/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.model;

import static edu.wright.dase.model.CONSTANTS.ENCODING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wright.dase.ui.SearchGUI;

/**
 *
 * @author mdkamruzzamansarker
 */
public class FileContent {

	final static Logger logger = LoggerFactory.getLogger(SearchGUI.class);
	
    public FileContent() {

    }

    public static String getFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())), StandardCharsets.UTF_8);
    }
    static int i = 0;

    public static String[] getFileContentSpilted(File file) throws IOException {
        String[] contents = new String[5];
        String Title;
        String Abstract;
        String Branch;
        String Content;
        //content[0] = title[.T ], content[1] = abstract[.A ], content[2] =  branch[.B ], content[3] = Words [.W ] 

        String fullcontent = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())), StandardCharsets.UTF_8);

        contents = fullcontent.split(".T", 2);
        contents = contents[1].split(".A", 2);
        Title = contents[0];

        contents = contents[1].split(".B", 2);
        Abstract = contents[0];

        contents = contents[1].split(".W", 2);
        Branch = contents[0];
        Content = contents[1];

        contents = new String[4];

        contents[0] = Title;
        contents[1] = Abstract;
        contents[2] = Branch;
        contents[3] = Content;

        return contents;
    }

    public static String chooseFile(JFrame frame) throws IOException {
        String filepath = "";
        String filename = "";
        File file = null;
        BufferedWriter writer = null;

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            filepath = fileChooser.getSelectedFile().getPath();
            filename = fileChooser.getSelectedFile().getAbsolutePath();

            int i = 0;
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), ENCODING)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(".I ", 0)) {

                        file = new File(fileChooser.getSelectedFile().getParent() + "/Doc-" + ++i);
                        if (i > 1) {
                            writer.close();
                        }
                        writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), ENCODING);
                    }

                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
            }
        }
        return fileChooser.getSelectedFile().getAbsolutePath();
    }


}
