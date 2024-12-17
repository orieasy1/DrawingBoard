package com.team2.ui;

import com.team2.shapes.Circle;
import com.team2.shapes.Line;
import com.team2.shapes.Rectangle;
import com.team2.shapes.Shape;
import com.team2.actions.UndoRedo;
import com.team2.actions.ActionRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Canvas extends JPanel {
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> selectedShapes = new ArrayList<>();
//    private final Stack<Shape> undoStack = new Stack<>();
//    private final Stack<Shape> redoStack = new Stack<>();
    private final UndoRedo undoRedo;
    private Shape currentShape = null;
    private String currentMode = "Select";
    private Color currentColor = Color.BLACK;

    public Canvas(UndoRedo undoRedo) {
        //this.undoRedo = undoRedo;
        this.undoRedo = new UndoRedo();  // Canvas 참조 없이 생성
        this.undoRedo.setCanvas(this);
        setBackground(Color.WHITE);
        initializeMouseListeners();
    }

    public void undo() {
        undoRedo.undo();
    }

    public void redo() {
        undoRedo.redo();
    }

    private void initializeMouseListeners() {
        // Mouse listener for shape creation and selection
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased();
            }
        });

        // Mouse motion listener for dragging shapes
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    private void handleMousePressed(MouseEvent e) {
        switch (currentMode) {
            case "Circle" -> currentShape = new Circle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Rectangle" -> currentShape = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Line" -> currentShape = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Select" -> handleSelection(e.getX(), e.getY());
        }
    }

    private void handleMouseReleased() {
        if (currentShape != null) {
            addShape(currentShape, true);
            currentShape = null;
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        if (currentShape != null) {
            currentShape.setX2(e.getX());
            currentShape.setY2(e.getY());
            repaint();
        }
    }

    public void addShape(Shape shape, boolean recordAction) {
        shapes.add(shape);
        if (recordAction) {
            undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.ADD, shape));
        }
//        undoStack.push(shape);
//        redoStack.clear();
        repaint();
    }

    public void removeShape(Shape shape, boolean recordAction) {
        shapes.remove(shape);
        if (recordAction) {
            undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.REMOVE, shape));
        }
        repaint();
    }

    private void handleSelection(int x, int y) {
        selectedShapes.clear();
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                selectedShapes.add(shape);
            }
        }
        repaint();
    }

    public void setMode(String mode) {
        this.currentMode = mode;
    }

    public void setColor(Color color) {
        this.currentColor = color;
        // Update selected shapes if applicable
        for (Shape shape : selectedShapes) {
            Color previousColor = shape.getColor();
            shape.setColor(color);
            undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.COLOR_CHANGE, shape, previousColor));

        }
        repaint();
    }

    public void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                oos.writeObject(shapes);
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
            }
        }
    }

    public void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                shapes.clear();
                shapes.addAll((ArrayList<Shape>) ois.readObject());
//                undoStack.clear();
//                redoStack.clear();
                undoRedo.clearStacks();
                shapes.addAll((ArrayList<Shape>) ois.readObject());
                repaint();
                JOptionPane.showMessageDialog(this, "File loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw all shapes
        for (Shape shape : shapes) {
            shape.draw(g);
        }

        // Highlight selected shapes
        g.setColor(Color.RED);
        for (Shape shape : selectedShapes) {
            g.drawRect(shape.getX1() - 5, shape.getY1() - 5, 10, 10);
        }

        // Draw the shape currently being created
        if (currentShape != null) {
            currentShape.draw(g);
        }
    }
}
