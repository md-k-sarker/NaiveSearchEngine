/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.indexing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wright.dase.model.CONSTANTS;
import edu.wright.dase.model.FileContent;
import edu.wright.dase.model.StandardAnalyzerCustomed;
import edu.wright.dase.ui.SearchGUI;

/**
 *
 * @author mdkamruzzamansarker
 */
public final class BuildIndex {

	final static Logger logger = LoggerFactory.getLogger(BuildIndex.class);

	private IndexWriter indexwriter;
	private File inputFilesDirectory;
	private File indexFilesDirectory;

	public BuildIndex(String inputFilesDirectory, String indexFilesDirectory) throws IOException {

		this.inputFilesDirectory = new File(inputFilesDirectory);

		this.indexFilesDirectory = new File(indexFilesDirectory);
		rebuildIndex();
		closeIndexWriter();
	}

	public IndexWriter getIndexWriter() throws IOException {
		if (indexwriter == null) {

			Directory _indexFilesDirectory = FSDirectory.open(indexFilesDirectory.toPath());

			System.out.println("index Dir: " + _indexFilesDirectory.toString());

			// use the inputFilesDirectory
			IndexWriterConfig indexwriterconfig = new IndexWriterConfig(new StandardAnalyzerCustomed());
			indexwriter = new IndexWriter(_indexFilesDirectory, indexwriterconfig);
		}

		return indexwriter;
	}

	public void closeIndexWriter() throws IOException {
		if (indexwriter != null) {
			indexwriter.close();
		}
	}

	/**
	 * Take a text file as input and convert that file as Lucene Document
	 * 
	 * content[0] = title[.T ], content[1] = abstract[.A ], content[2] =
	 * branch[.B ], content[3] = Words [.W ]
	 * 
	 * @param File
	 * @return Document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Document getDocument(File file) throws FileNotFoundException, IOException {
		Document doc = new Document();
		String[] Contents = new String[4];
		Contents = FileContent.getFileContentSpilted(file);

		// Set ID and store the ID
		doc.add(new StringField("id", file.getCanonicalPath(), Field.Store.YES));

		Field titleField = new TextField("title", Contents[0], Field.Store.NO);
		titleField.setBoost((float) 3);
		doc.add(titleField);

		Field abstractField = new TextField("abstract", Contents[1], Field.Store.NO);
		abstractField.setBoost((float) 2);
		doc.add(abstractField);

		// Field branchField = new TextField("branch", Contents[2],
		// Field.Store.NO);
		// branchField.setBoost((float) .25);
		// doc.add(branchField);

		Field contentField = new TextField("content", Contents[3], Field.Store.NO);
		contentField.setBoost((float) 1);
		doc.add(contentField);

		return doc;
	}

	private void updateIndex(File file) throws IOException {
		IndexWriter writer = getIndexWriter();
		writer.addDocument(getDocument(file));

	}

	public void rebuildIndex() throws IOException {
		getIndexWriter();

		String path = inputFilesDirectory.toString();
		logger.info("Started Indexing using text files of " + path + " folder.");


		Files.walk(Paths.get(path)).forEach((Path filepath) -> {
			if (Files.isRegularFile(filepath)) {
				try {
					String extension = FilenameUtils.getExtension(filepath.toString());
					if (extension.toLowerCase().equals("txt")) {
						updateIndex(filepath.toFile());
					} else {
						logger.error("Not a text file. " + filepath.toString());
					}
				} catch (IOException ex) {
					logger.error("IOException", ex);
				}
			} else {
				logger.error("Not File: " + filepath);
			}
		});
		logger.info("Indexing successfull. ");
	}
}
