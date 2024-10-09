package com.example;

// JsonToUrlEncodedConverter/src/Main.java

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ConverterFrame();
        });
    }
}