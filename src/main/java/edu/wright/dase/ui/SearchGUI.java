/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import edu.wright.dase.handler.ActionHandler.SearchActionListener;
import edu.wright.dase.handler.ActionHandler.SelectInputFilesActionListener;
import edu.wright.dase.handler.ActionHandler.SelectLogOutputFolderActionListener;
import edu.wright.dase.indexing.BuildIndex;
import edu.wright.dase.model.CONSTANTS;
import edu.wright.dase.model.FileContent;
import edu.wright.dase.control.Controller;
import edu.wright.dase.control.WriteVariousStatistics;
import edu.wright.dase.handler.ActionHandler.BuildIndexActionListener;
import edu.wright.dase.handler.ActionHandler.SelectIndexSavingFolderActionListener;

/**
 *
 * @author mdkamruzzamansarker checking git commits
 */
public class SearchGUI extends javax.swing.JFrame {

	private String inputquery = null;
	private static JLabel[] resultlabels = new JLabel[10];

	private final String APP_TITLE = "Naive Search Engine";
	private final String SEARCH_BUTTON_TEXT = "Search";
	private final String SELECT_OUTPUT_LOG_FOLDER_TEXT = "Output Folder";
	private final String SELECT_INPUT_FOLDER_TEXT = "Input Folder";
	private final String BUILD_INDEX_TEXT = "Build Index";
	private final String OPTIONS_TEXT = "Option";
	private final String INDEX_FOLDER_TEXT = "Index Folder";

	private JPanel searchInputPanel;
	private JPanel searchResultPanel;
	private JPanel statusPanel;
	private JPanel configPanel;
	private JPanel holderPanel;

	private JScrollPane searchResultScrollPane;

	private MenuBar menubar;
	private MenuItem menuItemBuildIndex;
	private MenuItem menuItemSelectInputFiles;
	private MenuItem menuItemSelectLogOutputFolder;

	private JButton searchButton;
	private JButton buildIndexButton;
	private JButton selectIndexSavingFolderButton;
	private JButton selectInputFilesButton;
	private JButton selectOutputFolderButton;

	private JTextArea searchInputTextArea;
	private JTextField inputFilesTextField;
	private JTextField outputFilesTextField;
	private JTextField indexFolderTextField;

	Controller controller;

	public SearchGUI() {

		initComponents();

		controller = new Controller(this);

		addListeners();

		setTexts();

	}

	private void initComponents() {
		setTitle(APP_TITLE);
		setLayout(new BorderLayout());
		setSize(700, 500);

		searchInputPanel = new JPanel();
		searchInputPanel.setLayout(new BorderLayout());
		searchInputPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		searchButton = new JButton(SEARCH_BUTTON_TEXT);
		searchButton.setPreferredSize(new Dimension(120, 20));
		searchButton.setSize(new Dimension(120, 20));

		searchInputTextArea = new JTextArea();
		searchInputTextArea.setLineWrap(true);
		searchInputTextArea.setRows(1);

		searchInputPanel.add(searchButton, BorderLayout.EAST);
		searchInputPanel.add(searchInputTextArea, BorderLayout.CENTER);

		configPanel = new JPanel();
		configPanel.setLayout(new BorderLayout());
		configPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		JPanel configInputFilesPanel, configOutputFilesPanel, configIndexFolderPanel;

		configInputFilesPanel = new JPanel();
		configInputFilesPanel.setLayout(new BorderLayout());
		inputFilesTextField = new JTextField();
		inputFilesTextField.setEditable(false);
		inputFilesTextField.setColumns(50);
		selectInputFilesButton = new JButton();
		selectInputFilesButton.setText(SELECT_INPUT_FOLDER_TEXT);
		selectInputFilesButton.setPreferredSize(new Dimension(120, 20));
		selectInputFilesButton.setSize(new Dimension(120, 20));
		configInputFilesPanel.add(inputFilesTextField, BorderLayout.CENTER);
		configInputFilesPanel.add(selectInputFilesButton, BorderLayout.EAST);

		configIndexFolderPanel = new JPanel();
		configIndexFolderPanel.setLayout(new BorderLayout());
		indexFolderTextField = new JTextField();
		indexFolderTextField.setEditable(false);
		indexFolderTextField.setColumns(50);
		selectIndexSavingFolderButton = new JButton();
		selectIndexSavingFolderButton.setText(INDEX_FOLDER_TEXT);
		selectIndexSavingFolderButton.setPreferredSize(new Dimension(120, 20));
		selectIndexSavingFolderButton.setSize(new Dimension(120, 20));
		buildIndexButton = new JButton();
		buildIndexButton.setText(BUILD_INDEX_TEXT);
		buildIndexButton.setPreferredSize(new Dimension(120, 20));
		buildIndexButton.setSize(new Dimension(120, 20));
		configIndexFolderPanel.add(buildIndexButton, BorderLayout.NORTH);
		configIndexFolderPanel.add(indexFolderTextField, BorderLayout.CENTER);
		configIndexFolderPanel.add(selectIndexSavingFolderButton, BorderLayout.EAST);

		configOutputFilesPanel = new JPanel();
		configOutputFilesPanel.setLayout(new BorderLayout());
		outputFilesTextField = new JTextField();
		outputFilesTextField.setEditable(false);
		outputFilesTextField.setColumns(50);
		selectOutputFolderButton = new JButton();
		selectOutputFolderButton.setText(SELECT_OUTPUT_LOG_FOLDER_TEXT);
		selectOutputFolderButton.setPreferredSize(new Dimension(120, 20));
		selectOutputFolderButton.setSize(new Dimension(120, 20));
		configOutputFilesPanel.add(outputFilesTextField, BorderLayout.CENTER);
		configOutputFilesPanel.add(selectOutputFolderButton, BorderLayout.EAST);

		configPanel.add(configInputFilesPanel, BorderLayout.CENTER);
		configPanel.add(configIndexFolderPanel, BorderLayout.NORTH);
		configPanel.add(configOutputFilesPanel, BorderLayout.SOUTH);

		searchResultPanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(searchResultPanel, BoxLayout.Y_AXIS);
		searchResultPanel.setLayout(boxlayout);
		searchResultScrollPane = new JScrollPane(searchResultPanel);

		statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());

		holderPanel = new JPanel();
		holderPanel.setLayout(new BorderLayout());
		holderPanel.add(configPanel, BorderLayout.NORTH);
		holderPanel.add(statusPanel, BorderLayout.CENTER);

		add(searchInputPanel, BorderLayout.NORTH);
		add(searchResultScrollPane, BorderLayout.CENTER);
		add(holderPanel, BorderLayout.SOUTH);

		formulateMenubar();

	}

	private void setTexts() {
		try {
			// indexFolderTextField.setText(CONSTANTS.INDEXPATH);
			inputFilesTextField.setText(CONSTANTS.INPUTFILESDIRECTORY);
			outputFilesTextField.setText(CONSTANTS.RESULTSPATH);

		} catch (Exception exception) {

		}
	}

	private void addListeners() {

		searchButton.addActionListener(new SearchActionListener(this, controller));

		menuItemBuildIndex.addActionListener(new BuildIndexActionListener(this));

		menuItemSelectInputFiles.addActionListener(new SelectInputFilesActionListener(this));

		menuItemSelectLogOutputFolder.addActionListener(new SelectLogOutputFolderActionListener(this));

		buildIndexButton.addActionListener(new BuildIndexActionListener(this));

		selectInputFilesButton.addActionListener(new SelectInputFilesActionListener(this));

		selectOutputFolderButton.addActionListener(new SelectLogOutputFolderActionListener(this));

		selectIndexSavingFolderButton.addActionListener(new SelectIndexSavingFolderActionListener(this));
	}

	private void formulateMenubar() {
		menubar = new MenuBar(); // getJMenuBar();

		Menu defaultMenu = new Menu(OPTIONS_TEXT);

		menuItemBuildIndex = new MenuItem(BUILD_INDEX_TEXT);

		menuItemSelectInputFiles = new MenuItem(SELECT_INPUT_FOLDER_TEXT);

		menuItemSelectLogOutputFolder = new MenuItem(SELECT_OUTPUT_LOG_FOLDER_TEXT);

		defaultMenu.add(menuItemBuildIndex);
		defaultMenu.add(menuItemSelectInputFiles);
		defaultMenu.add(menuItemSelectLogOutputFolder);

		menubar.add(defaultMenu);

		this.setMenuBar(menubar);

	}

	public String getQueryText() {
		return this.searchInputTextArea.getText();
	}

	public void setInputFolderText(String path) {
		this.inputFilesTextField.setText(path);
	}

	public void setOutputFolderText(String path) {
		this.outputFilesTextField.setText(path);
	}

	public void setIndexFolderText(String path) {
		this.indexFolderTextField.setText(path);
	}

	public JPanel getResultPanel() {
		return this.searchResultPanel;
	}

	public JScrollPane getResultScrollPane() {
		return this.searchResultScrollPane;
	}

	public void addResult(String filePath, float score) {

		JPanel eachPanel = new JPanel();
		JButton button = new JButton(filePath);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (Desktop.isDesktopSupported()) {
					try {
						URI uri = new URI("file:" + filePath);
						Desktop.getDesktop().browse(uri);
					} catch (URISyntaxException exception) {
					} catch (IOException exception) {
					}
				} else {
				}
			}
		});

		JLabel label = new JLabel("Score: " + score);
		eachPanel.setLayout(new BorderLayout());
		eachPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		eachPanel.add(button, BorderLayout.NORTH);
		eachPanel.add(label, BorderLayout.SOUTH);

		searchResultPanel.add(eachPanel);

	}

	private void callSplitter() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor((SearchGUI) this);
		try {
			FileContent.chooseFile(topFrame);
		} catch (Exception ex) {
			Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new SearchGUI().setVisible(true);

					CONSTANTS.OSNAME = System.getProperty("os.name");

				} catch (Exception ex) {
					Logger.getLogger(SearchGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
				}
			}
		});
	}

}
