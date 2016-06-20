package orm.common.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import orm.common.util.Producer;
import orm.common.util.Scanner;

/*
 * A simple Text Editor.  This demonstrates the use of a 
 * JFileChooser for the user to select a file to read from or write to.
 * It also demonstrates reading and writing text files.
 */
public class TextEditor implements ActionListener {
	// Size of editing text area.
	private static final int NUM_ROWS = 25;
	private static final int NUM_COLS = 59;

	// Buttons to save and load files.
	private JButton saveButton, loadButton, compileButton, runButton, clear;
	private DefaultComboBoxModel inheritanceTypes = new DefaultComboBoxModel();
	// Area where the user does the editing
	private JTextArea text, result;
	JFrame frame = new JFrame("ObjectRelationMapping-ORM");

	// Creates the GUI
	public TextEditor() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height - 50);
		frame.setResizable(false);

		JPanel buttonPanel = new JPanel();

		inheritanceTypes.addElement("Choose Inheritance Mapping Type");
		inheritanceTypes.addElement("Map the entire class hierarchy to a single table");
		inheritanceTypes.addElement("Map each concrete class to its own table");
		inheritanceTypes.addElement("Map each class to its own table");

		final JComboBox fruitCombo = new JComboBox(inheritanceTypes);
		fruitCombo.setSelectedIndex(0);

		JScrollPane fruitListScrollPane = new JScrollPane(fruitCombo);
		buttonPanel.add(fruitListScrollPane);

		saveButton = new JButton("Save The Result Query");
		loadButton = new JButton("Load The Input File");
		compileButton = new JButton("Compile And Validate");
		runButton = new JButton("Run");
		loadButton = new JButton("Load The Input File");
		clear = new JButton("Clear");
		buttonPanel.add(loadButton);
		buttonPanel.add(compileButton);
		buttonPanel.add(runButton);
		buttonPanel.add(clear);

		text = new JTextArea(NUM_ROWS, NUM_COLS);
		text.setBorder(
				BorderFactory.createCompoundBorder(text.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		text.setFont(new Font("Time New Roman", Font.ITALIC, 13));
		text.setBackground(Color.BLACK);
		text.setForeground(Color.WHITE);
		text.setEditable(false);
		JScrollPane textScroller = new JScrollPane(text);

		result = new JTextArea(NUM_ROWS, NUM_COLS);
		result.setBorder(
				BorderFactory.createCompoundBorder(text.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		result.setFont(new Font("Time New Roman", Font.ITALIC, 14));
		JScrollPane textScroller2 = new JScrollPane(result);
		Container contentPane = frame.getContentPane();

		contentPane.add(textScroller, BorderLayout.WEST);
		contentPane.add(textScroller2, BorderLayout.EAST);
		contentPane.add(buttonPanel, BorderLayout.NORTH);

		loadButton.addActionListener(this);
		compileButton.addActionListener(this);
		runButton.addActionListener(this);
		saveButton.addActionListener(this);
		clear.addActionListener(this);
		
		frame.setVisible(true);
	}

	// Listener for button clicks that loads the
	// specified files and puts it in the
	// editor.
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveButton) {
			saveFile();
		} else if (event.getSource() == loadButton) {
			loadFile();
		} else if (event.getSource() == compileButton) {
			compile();
		} else if (event.getSource() == runButton) {
			run();
		} else {
			Scanner.errors.clear();
			Scanner.scanner.clear();
			Scanner.model.associations.clear();
			Scanner.model.classes.clear();
			frame.dispose();
			new TextEditor();
		}
	}

	private void run() {
		result.setText("");
		String type = "";
		if (inheritanceTypes.getSelectedItem().equals("Map the entire class hierarchy to a single table"))
			type = "1";
		else if (inheritanceTypes.getSelectedItem().equals("Map each concrete class to its own table"))
			type = "2";
		else if (inheritanceTypes.getSelectedItem().equals("Map each class to its own table"))
			type = "3";
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please choose type", "Dialog", JOptionPane.ERROR_MESSAGE);

		if (type.equals("1") || type.equals("2") || type.equals("3")) {
			Scanner.run(type);
			Scanner.produceQueries();
			loadResult();
		}
	}

	// Display a file chooser so the user can select a file
	// to save to. Then write the contents of the text area
	// to that file. Does nothing if the user cancels out
	// of the file chooser
	private void compile() {
		result.setText("");
		Scanner.errors.clear();
		Scanner.syntaxAnalyzer();

		List<String> errors = Scanner.errors;
		String error = "";
		if (errors.size() != 0) {
			for (int i = 0; i < errors.size(); i++)
				error += errors.get(i) + "\n";
			result.setText(error);
		} else {
			Scanner.PrepareModel();
		}
	}

	private void saveTextFile() {
		File file;

		// create and display dialog box to get file name
		JFileChooser dialog = new JFileChooser();

		// Make sure the user didn't cancel the file chooser
		if (dialog.showSaveDialog(text) == JFileChooser.APPROVE_OPTION) {

			// Get the file the user selected
			file = dialog.getSelectedFile();

			try {
				// Now write to the file
				PrintWriter output = new PrintWriter(new FileWriter(file));
				output.println(text.getText());
				output.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(text, "Can't save file " + e.getMessage());
			}
		}
	}

	private void saveFile() {
		File file;

		// create and display dialog box to get file name
		JFileChooser dialog = new JFileChooser();

		// Make sure the user didn't cancel the file chooser
		if (dialog.showSaveDialog(result) == JFileChooser.APPROVE_OPTION) {

			// Get the file the user selected
			file = dialog.getSelectedFile();

			try {
				// Now write to the file
				PrintWriter output = new PrintWriter(new FileWriter(file));
				output.println(result.getText());
				output.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(result, "Can't save file " + e.getMessage());
			}
		}
	}

	// Display a file chooser so the user can select a file to load.
	// Then load the file into the editing area. Does nothing if
	// the user cancels the file chooser.
	private void loadFile() {
		result.setText("");
		Scanner.scanner.clear();
		String line;
		File file;

		// create and display dialog box to get file name
		JFileChooser dialog = new JFileChooser();

		// Make sure the user did not cancel.
		if (dialog.showOpenDialog(text) == JFileChooser.APPROVE_OPTION) {
			// Find out which file the user selected.
			file = dialog.getSelectedFile();

			try {
				// Open the file.
				BufferedReader input = new BufferedReader(new FileReader(file));

				// Clear the editing area
				text.setText("");

				// Fill up the ediitng area with the contents of the file being
				// read.
				line = input.readLine();
				while (line != null) {
					text.append(line + "\n");
					Scanner.scanner.add(line);
					line = input.readLine();
				}

				// Close the file
				input.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(text, "Can't load file " + e.getMessage());
			}
		}
	}

	private void loadResult() {
		String line;
		File file = new File("query.sql");

		try {
			// Open the file.
			BufferedReader input = new BufferedReader(new FileReader(file));

			// Clear the editing area
			result.setText("");
			line = input.readLine();
			while (line != null) {
				result.append(line + "\n");
				line = input.readLine();
			}
			input.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(result, "Can't load file " + e.getMessage());
		}

	}

	// Main program for the application
	public static void main(String[] args) {
		new TextEditor();
	}
}