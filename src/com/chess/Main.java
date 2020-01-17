package com.chess;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Create frame and components
        SwingUtilities.invokeLater(() -> {
            try {
                new GUISetup();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}


