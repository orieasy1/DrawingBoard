package com.team2.ui;

import com.team2.actions.UndoRedo;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JToolBar {
    public ButtonPanel(Canvas canvas, UndoRedo undoRedo) {
        setFloatable(false);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton circleButton = new JButton("Circle");
        circleButton.addActionListener(e -> canvas.setMode("Circle"));
        add(circleButton);

        JButton rectangleButton = new JButton("Rectangle");
        rectangleButton.addActionListener(e -> canvas.setMode("Rectangle"));
        add(rectangleButton);

        JButton lineButton = new JButton("Line");
        lineButton.addActionListener(e -> canvas.setMode("Line"));
        add(lineButton);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoRedo.undo());
        add(undoButton);

        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(e -> undoRedo.redo());
        add(redoButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> canvas.saveToFile());
        add(saveButton);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> canvas.loadFromFile());
        add(loadButton);
    }
}

