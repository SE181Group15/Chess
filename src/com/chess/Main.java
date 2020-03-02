package com.chess;

import javax.swing.*;

class Main {

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


