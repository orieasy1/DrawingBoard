package com.team2.ui;

import com.team2.shapes.Circle;
import com.team2.shapes.Line;
import com.team2.Shape;
import com.team2.actions.ActionRecord;
import com.team2.actions.UndoRedo;
import com.team2.shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Canvas extends JPanel {
    private ArrayList<com.team2.Shape> shapes = new ArrayList<>();
    private ArrayList<com.team2.Shape> selectedShapes = new ArrayList<>();
    private Stack<com.team2.Shape> undoStack = new Stack<>();
    private Stack<com.team2.Shape> redoStack = new Stack<>();
    private com.team2.Shape currentShape = null;
    private String currentMode = "Select";
    private Color currentColor = Color.BLACK;
    private UndoRedo undoRedo;

    public Canvas(UndoRedo undoRedo) {
        this.undoRedo = undoRedo;
        this.setBackground(Color.WHITE);

        // 마우스 이벤트
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentMode.equals("Circle")) {
                    currentShape = new Circle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                } else if (currentMode.equals("Rectangle")) {
                    currentShape = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                } else if (currentMode.equals("Line")) {
                    currentShape = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                } else if (currentMode.equals("Select")) {
                    handleSelection(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentShape != null) {
                    addShape(currentShape);
                    currentShape = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentShape != null) {
                    currentShape.setX2(e.getX());
                    currentShape.setY2(e.getY());
                    repaint();
                }
            }
        });
    }

    public void addShape(com.team2.Shape shape) {
        shapes.add(shape);
        undoStack.push(shape);
        redoStack.clear(); // 새로운 작업 후 Redo 스택 초기화
        repaint();
    }

    public void removeShape(com.team2.Shape shape) {
        shapes.remove(shape);
        undoStack.push(shape);
        repaint();
    }

    public void addShape(com.team2.Shape shape, boolean record) {
        shapes.add(shape);
        if (record) {
            undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.ADD, shape));
        }
        repaint();
    }

    public void removeShape(com.team2.Shape shape, boolean record) {
        shapes.remove(shape);
        if (record) {
            undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.REMOVE, shape));
        }
        repaint();
    }


    public void setMode(String mode) {
        this.currentMode = mode;
    }

    public void setColor(Color color) {
        this.currentColor = color;
        for (com.team2.Shape shape : selectedShapes) {
            if (shape instanceof Circle) ((Circle) shape).setColor(color);
            if (shape instanceof Rectangle) ((Rectangle) shape).setColor(color);
            if (shape instanceof Line) ((Line) shape).setColor(color);
        }
        repaint();
    }

    public void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
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
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                shapes = (ArrayList<com.team2.Shape>) ois.readObject();
                undoStack.clear();
                redoStack.clear();
                repaint();
                JOptionPane.showMessageDialog(this, "File loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
            }
        }
    }

    private void handleSelection(int x, int y) {
        selectedShapes.clear();
        for (com.team2.Shape shape : shapes) {
            if (shape instanceof Circle && ((Circle) shape).contains(x, y)) {
                selectedShapes.add(shape);
            }
            if (shape instanceof Rectangle && ((Rectangle) shape).contains(x, y)) {
                selectedShapes.add(shape);
            }
            if (shape instanceof Line && ((Line) shape).contains(x, y)) {
                selectedShapes.add(shape);
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (com.team2.Shape shape : shapes) {
            shape.draw(g);
        }
        for (Shape shape : selectedShapes) {
            g.setColor(Color.RED);
            g.drawRect(shape.getX1() - 5, shape.getY1() - 5, 10, 10); // 간단한 강조 효과
        }
        if (currentShape != null) {
            currentShape.draw(g);
        }
    }
}
