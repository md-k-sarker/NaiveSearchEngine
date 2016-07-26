/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import utility.CONSTANTS;
import utility.StandardAnalyzerCustomed;
import utility.FileContent;

/**
 *
 * @author mdkamruzzamansarker
 */
public final class BuildIndex {

	private IndexWriter indexwriter; // = new IndexWriter
	private File inputFilesDirectory;
	private File indexFilesDirectory;

	public BuildIndex(File inputFilesDirectory, File indexFilesDirectory) throws IOException {

		this.inputFilesDirectory = inputFilesDirectory;
		this.indexFilesDirectory = indexFilesDirectory;
		rebuildIndex();
		closeIndexWriter();
	}

	public IndexWriter getIndexWriter(boolean create) throws IOException {
		if (indexwriter == null) {
			// String path =
			// getClass().getClassLoader().getResource(CONSTANTS.indexpath).getPath();
			// if (CONSTANTS.OSName.startsWith("Win")) {
			// path = path.substring(1, path.length());
			// }

			Directory _indexFilesDirectory = FSDirectory.open(indexFilesDirectory.toPath());

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

	/// getFileContentSpilted(file)
	// content[0] = title[.T ], content[1] = abstract[.A ], content[2] =
	/// branch[.B ], content[3] = Words [.W ]
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
		IndexWriter writer = getIndexWriter(false);
		writer.addDocument(getDocument(file));
		//System.out.println("Indexing " + file.getCanonicalPath() + " completed.");
	}

	public void rebuildIndex() throws IOException {
		getIndexWriter(true);
		// String path =
		// getClass().getClassLoader().getResource(CONSTANTS.docspath).getPath();
		String path = inputFilesDirectory.toString();
		System.out.println("Started Indexing in  " + path + " this folder.");
		
		if (CONSTANTS.OSName.startsWith("Win")) {
			path = path.substring(1, path.length());
		}

		Files.walk(Paths.get(path)).forEach((Path filepath) -> {
			if (Files.isRegularFile(filepath)) {
				try {
					//System.out.println("filePath: " + filepath);
					updateIndex(filepath.toFile());
				} catch (Exception ex) {
					Logger.getLogger("Something Wrong. " + BuildIndex.class.getName()).log(Level.SEVERE, null, ex);
					ex.printStackTrace();
				}
			} else {
				 System.out.println("Not File: " + filepath);
			}
		});

	}
}
