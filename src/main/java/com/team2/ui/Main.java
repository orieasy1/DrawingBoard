package com.team2.ui;

import com.team2.actions.UndoRedo;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Drawing Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);


        UndoRedo undoRedo = new UndoRedo(null);
        Canvas canvas = new Canvas(undoRedo);
        undoRedo = new UndoRedo(canvas);


        ButtonPanel buttonPanel = new ButtonPanel(canvas);


        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}



