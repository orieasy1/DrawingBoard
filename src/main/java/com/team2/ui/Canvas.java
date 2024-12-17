package com.team2.ui;

import com.team2.actions.FileHandler;
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
    private final Stack<Shape> undoStack = new Stack<>();
    private final Stack<Shape> redoStack = new Stack<>();
    private final UndoRedo undoRedo;
    private Shape currentShape = null;
    private String currentMode = "Select";
    private Color currentColor = Color.BLACK;

    public Canvas(UndoRedo undoRedo) {
        this.undoRedo = undoRedo;
        setBackground(Color.WHITE);
        initializeMouseListeners();
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
        undoStack.push(shape);
        redoStack.clear();
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
            shape.setColor(color);
        }
        repaint();
    }

    public void saveToFile() {
        FileHandler.saveToFile(shapes, this);
    }

    public void loadFromFile() {
        ArrayList<Shape> loadedShapes = FileHandler.loadFromFile(this); // 명확한 타입 사용
        if (!loadedShapes.isEmpty()) {
            shapes.clear();
            shapes.addAll(loadedShapes); // 타입 불일치 오류 해결
            undoStack.clear();
            redoStack.clear();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No shapes to load or file is empty.");
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
