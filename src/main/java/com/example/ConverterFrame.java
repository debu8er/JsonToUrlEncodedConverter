package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;

public class ConverterFrame extends JFrame {

    private JTextArea jsonInput;
    private JTextArea urlEncodedOutput;
    private JButton convertButton;
    private JButton clearButton; // Clear button
    private JButton copyButton;  // Copy button

    public ConverterFrame() {
        setTitle("JSON to URL-Encoded Converter");
        setSize(800, 600); // Increased size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        setLocationRelativeTo(null); // **Center the frame on the screen**
        setVisible(true);
    }

    private void initializeComponents() {
        // Initialize components
        jsonInput = new JTextArea();
        urlEncodedOutput = new JTextArea();
        urlEncodedOutput.setEditable(false);
        convertButton = new JButton("Convert");
        clearButton = new JButton("Clear");
        copyButton = new JButton("Copy Output");

        // Set layout manager
        setLayout(new BorderLayout(10, 10));
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel buttonPanel = new JPanel();

        // Add components to centerPanel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("JSON Input:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(jsonInput), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("URL-Encoded Output:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(urlEncodedOutput), BorderLayout.CENTER);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        // Add buttons to buttonPanel
        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);

        // Add panels to the frame
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        convertButton.addActionListener(new ConvertButtonListener());
        clearButton.addActionListener(new ClearButtonListener());
        copyButton.addActionListener(new CopyButtonListener());
    }

    // Action Listener for Convert Button
    private class ConvertButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jsonString = jsonInput.getText().trim();
            if (jsonString.isEmpty()) {
                JOptionPane.showMessageDialog(null, "JSON input cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String urlEncoded = JsonUrlEncoder.encode(jsonString);
                urlEncodedOutput.setText(urlEncoded);
            } catch (JsonProcessingException ex) {
                JOptionPane.showMessageDialog(null, "Invalid JSON input.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (UnsupportedEncodingException ex) {
                JOptionPane.showMessageDialog(null, "Encoding error.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Action Listener for Clear Button
    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonInput.setText("");
            urlEncodedOutput.setText("");
        }
    }

    // Action Listener for Copy Button
    private class CopyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String outputText = urlEncodedOutput.getText();
            if (outputText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Output is empty to copy.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringSelection stringSelection = new StringSelection(outputText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "Output copied to clipboard.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}