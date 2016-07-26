/**
 * 
 */
package edu.wright.dase.handler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.lucene.util.Constants;

import indexing.BuildIndex;
import utility.CONSTANTS;

/**
 * @author sarker
 *
 */
public class ActionHandler {

	public static class SearchActionListener implements ActionListener {

		public SearchActionListener() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public static class BuildIndexActionListener implements ActionListener {
		Component parent;

		public BuildIndexActionListener(Component parent) {
			this.parent = parent;
		}

		private void createIndexFolderIfNotExist() {
			if (CONSTANTS.indexFilesDirectory != null) {
				if (!CONSTANTS.indexFilesDirectory.exists()) {
					try {
						CONSTANTS.indexFilesDirectory.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				try {
					File file = File.createTempFile("Indexes", null);
					CONSTANTS.indexFilesDirectory = file.getParentFile();
					System.out.println("Set CONSTANTS.indexFilesDirectory: " + CONSTANTS.indexFilesDirectory);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				createIndexFolderIfNotExist();

				if (CONSTANTS.inputFilesDirectory != null) {

					new BuildIndex(CONSTANTS.inputFilesDirectory, CONSTANTS.indexFilesDirectory);

				} else {
					JOptionPane.showMessageDialog(parent, "Please specify input folders first.", "Input Folder Not Specified", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException exception) {
				exception.printStackTrace();
				JOptionPane.showMessageDialog(parent, "", "", JOptionPane.ERROR_MESSAGE);
			}
			// TODO Auto-generated method stub

		}
	}

	public static class SelectIndexFilesActionListener implements ActionListener {

		Component parent;

		public SelectIndexFilesActionListener(Component parent) {
			this.parent = parent;
			System.out.println("initialized");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String tmpFolderDir;
			File selectedFolder;
			System.out.println("called");
			try {

				tmpFolderDir = File.createTempFile("_temp_", null).getParent();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(tmpFolderDir));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Chose Folder");

				if (fileChooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
					selectedFolder = fileChooser.getSelectedFile();
					System.out.println("selectedFolder " + selectedFolder);
					CONSTANTS.inputFilesDirectory = selectedFolder;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated method stub

		}
	}

}
