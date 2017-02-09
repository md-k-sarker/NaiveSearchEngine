/**
 * 
 */
package edu.wright.dase.handler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wright.dase.control.Controller;
import edu.wright.dase.indexing.BuildIndex;
import edu.wright.dase.model.CONSTANTS;
import edu.wright.dase.ui.SearchGUI;

/**
 * @author sarker
 *
 */
public class ActionHandler {

	final static Logger logger = LoggerFactory.getLogger(SearchGUI.class);

	public static class SearchActionListener implements ActionListener {

		SearchGUI searchGui;
		Controller controller;

		public SearchActionListener(SearchGUI searchGui, Controller controller) {
			this.searchGui = searchGui;
			this.controller = controller;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				String queryText = searchGui.getQueryText();
				if (queryText.length() > 0)
					controller.search(queryText);

			} catch (IOException e1) {
				logger.error("IOException ", e1);
			} catch (ParseException e1) {
				logger.error("ParseException ", e1);
			} catch(Exception e1){
				logger.error("Exception ", e1);
			}
		}
	}

	public static class BuildIndexActionListener implements ActionListener {
		Component parent;

		public BuildIndexActionListener(Component parent) {
			this.parent = parent;
		}

		private void createIndexFolderIfNotExist() {

			if (CONSTANTS.INDEXPATH != null) {

				File indexFilesDirectory = new File(CONSTANTS.INDEXPATH);
				// path does not exist. create a new path/folder
				if (!indexFilesDirectory.exists()) {
					try {
						indexFilesDirectory = new File(CONSTANTS.INDEXPATH + "/Indexes");
						System.out.println(CONSTANTS.INDEXPATH + " not exist. Creating Directory. ");
						File file = File.createTempFile("Indexes", null);
						CONSTANTS.INDEXPATH = file.getParentFile().getCanonicalPath() + "/Indexes";
						logger.info("CONSTANTS.INDEXPATH: " + CONSTANTS.INDEXPATH);
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("IOException", e);
					}
				} else {
				}
			} else {
				try {
					logger.info("CONSTANTS.INDEXPATH is null, creating in Temporary folder");
					File file = File.createTempFile("Indexes", null);
					CONSTANTS.INDEXPATH = file.getParentFile().getCanonicalPath() + "/Indexes";
					logger.info("CONSTANTS.INDEXPATH: " + CONSTANTS.INDEXPATH);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			SearchGUI searchGUI = (SearchGUI) parent;
			searchGUI.setIndexFolderText(CONSTANTS.INDEXPATH);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				createIndexFolderIfNotExist();

				if (CONSTANTS.INPUTFILESDIRECTORY != null) {

					new BuildIndex(CONSTANTS.INPUTFILESDIRECTORY, CONSTANTS.INDEXPATH);

				} else {
					JOptionPane.showMessageDialog(parent,
							"To build index text file is necessary. \nPlease select input folder where the text files are in.",
							"Input Text file not found", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException exception) {
				exception.printStackTrace();
				logger.error("IOException", exception);
				JOptionPane.showMessageDialog(parent, exception.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static class SelectInputFilesActionListener implements ActionListener {

		Component parent;

		public SelectInputFilesActionListener(Component parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			File selectedFolder;
			try {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Chose Folder");

				if (fileChooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
					selectedFolder = fileChooser.getSelectedFile();
					System.out.println("InputFilesPath: " + selectedFolder);
					CONSTANTS.INPUTFILESDIRECTORY = selectedFolder.getCanonicalPath();
					SearchGUI searchGUI = (SearchGUI) parent;
					searchGUI.setInputFolderText(selectedFolder.getCanonicalPath());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public static class SelectLogOutputFolderActionListener implements ActionListener {
		Component parent;

		public SelectLogOutputFolderActionListener(Component parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File selectedFolder;
			try {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Choose Output Folder");

				if (fileChooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
					selectedFolder = fileChooser.getSelectedFile();
					System.out.println("SelectedLogOutputFolder: " + selectedFolder);
					CONSTANTS.RESULTSPATH = selectedFolder.getCanonicalPath();
					SearchGUI searchGUI = (SearchGUI) parent;
					searchGUI.setOutputFolderText(selectedFolder.getCanonicalPath());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public static class SelectIndexSavingFolderActionListener implements ActionListener {
		Component parent;

		public SelectIndexSavingFolderActionListener(Component parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File selectedFolder;
			try {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Choose a folder where you want to save Indexes.");

				if (fileChooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
					selectedFolder = fileChooser.getSelectedFile();
					System.out.println("SelecttedIndexSavingFolder: " + selectedFolder);
					CONSTANTS.INDEXPATH = selectedFolder.getCanonicalPath();
					SearchGUI searchGUI = (SearchGUI) parent;
					searchGUI.setIndexFolderText(selectedFolder.getCanonicalPath());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
