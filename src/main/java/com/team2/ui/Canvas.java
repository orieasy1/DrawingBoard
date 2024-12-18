package com.team2.ui;

import com.team2.shapes.*;
import com.team2.actions.UndoRedo;
import com.team2.actions.ActionRecord;
import com.team2.shapes.Rectangle;
import com.team2.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Canvas extends JPanel {
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> selectedShapes = new ArrayList<>();
    private boolean isGroupActivated = false;
    private final UndoRedo undoRedo;
    private Shape currentShape = null;
    private String currentMode = "Select";
    private Color currentColor = Color.BLACK;
    private com.team2.shapes.Rectangle selectionBox = null;
    private Point dragStart = null;
    private boolean isDragging = false;
    private ArrayList<Shape> clipboard = new ArrayList<>();

    public Canvas(UndoRedo undoRedo) {
        //this.undoRedo = undoRedo;
        this.undoRedo = new UndoRedo();
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
        dragStart = new Point(e.getX(), e.getY());
        switch (currentMode) {
            case "Circle" -> currentShape = new Circle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Rectangle" -> currentShape = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Line" -> currentShape = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
            case "Select" -> {
                if (!selectedShapes.isEmpty()) {
                    // 이미 선택된 도형이 있고, 그 도형을 클릭했다면 드래그 모드
                    boolean clickedSelected = false;
                    for (Shape shape : selectedShapes) {
                        if (shape.contains(e.getX(), e.getY())) {
                            clickedSelected = true;
                            isDragging = true;
                            break;
                        }
                    }
                    if (!clickedSelected) {
                        // 선택된 도형을 클릭하지 않았다면 새로운 선택 시작
                        selectionBox = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), new Color(0, 0, 255, 50));
                        selectedShapes.clear();
                    }
                } else {
                    // 선택된 도형이 없다면 새로운 선택 시작
                    selectionBox = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), new Color(0, 0, 255, 50));
                }
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        if (currentShape != null) {
            currentShape.setX2(e.getX());
            currentShape.setY2(e.getY());
            repaint();
        } else if (currentMode.equals("Select")) {
            if (isDragging && !selectedShapes.isEmpty()) {
                // 선택된 도형 이동
                int dx = e.getX() - dragStart.x;
                int dy = e.getY() - dragStart.y;
                for (Shape shape : selectedShapes) {
                    shape.moveBy(dx, dy);
                }
                dragStart = new Point(e.getX(), e.getY());
                repaint();
            } else if (selectionBox != null) {
                // 선택 영역 그리기
                selectionBox.setX2(e.getX());
                selectionBox.setY2(e.getY());
                repaint();
            }
        }
    }

    private void handleMouseReleased() {
        if (currentShape != null) {
            addShape(currentShape, true);
            currentShape = null;
        } else if (currentMode.equals("Select")) {
            if (isDragging) {
                // 도형 이동 종료
                isDragging = false;
                // 이동 작업을 UndoRedo 스택에 기록
                for (Shape shape : selectedShapes) {
                    Point endPoint = new Point(shape.getX1(), shape.getY1());
                    undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.MOVE, shape, dragStart, endPoint));
                }
            } else if (selectionBox != null) {
                // 선택 영역으로 도형들 선택
                selectedShapes.clear();

                int selectionX = Math.min(selectionBox.getX1(), selectionBox.getX2());
                int selectionY = Math.min(selectionBox.getY1(), selectionBox.getY2());
                int selectionWidth = Math.abs(selectionBox.getX2() - selectionBox.getX1());
                int selectionHeight = Math.abs(selectionBox.getY2() - selectionBox.getY1());

                Rectangle selectionBounds = new Rectangle(
                        selectionX, selectionY, selectionWidth, selectionHeight, new Color(0, 0, 0, 0));

                for (Shape shape : shapes) {
                    if (selectionBounds.intersects(shape)) {
                        selectedShapes.add(shape);
                    }
                }
            }
            selectionBox = null;
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

        // Draw selection box if active
        if (selectionBox != null) {
            Graphics2D g2d = (Graphics2D) g;
            selectionBox.draw(g2d);
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, new float[]{3}, 0));
            g2d.drawRect(
                    Math.min(selectionBox.getX1(), selectionBox.getX2()),
                    Math.min(selectionBox.getY1(), selectionBox.getY2()),
                    Math.abs(selectionBox.getX2() - selectionBox.getX1()),
                    Math.abs(selectionBox.getY2() - selectionBox.getY1())
            );
        }
        g.setColor(Color.RED);
        for (Shape shape : selectedShapes) {
            g.drawRect(shape.getX1() - 5, shape.getY1() - 5, 10, 10);
        }

        // Draw the shape currently being created
        if (currentShape != null) {
            currentShape.draw(g);
        }
    }

    public void groupSelectedShapes() {

        if (selectedShapes.size() < 2) {
            return;
        }


        Group group = new Group();


        for (Shape shape : new ArrayList<>(selectedShapes)) {
            shapes.remove(shape);
            group.addShape(shape);
        }

        shapes.add(group);
        selectedShapes.clear();
        selectedShapes.add(group);

        isGroupActivated = true;
        System.out.println("Group created with " + group.getShapes().size() + " shapes");
        repaint();
    }

    public void ungroupSelectedShapes() {

        boolean hasGroup = false;
        for (Shape shape : selectedShapes) {
            if (shape instanceof Group) {
                hasGroup = true;
                break;
            }
        }

        if (!hasGroup) {
            return;
        }

        ArrayList<Shape> newShapes = new ArrayList<>();


        for (Shape shape : new ArrayList<>(selectedShapes)) {
            if (shape instanceof Group) {

                Group group = (Group) shape;

                newShapes.addAll(group.getShapes());

                shapes.remove(group);
            }
        }


        shapes.addAll(newShapes);


        selectedShapes.clear();
        selectedShapes.addAll(newShapes);

        isGroupActivated = false;
        System.out.println("Ungroup completed. New shapes: " + newShapes.size()); // 디버깅용
        repaint();
    }

    public boolean isGroupActivated() {
        return isGroupActivated;
    }
    public ArrayList<Shape> getSelectedShapes() {
        return selectedShapes;
    }

    private Shape cloneShape(Shape shape) {
        if (shape instanceof Rectangle) {
            return new Rectangle(
                    shape.getX1(), shape.getY1(),
                    shape.getX2(), shape.getY2(),
                    shape.getColor()
            );
        } else if (shape instanceof Circle) {
            return new Circle(
                    shape.getX1(), shape.getY1(),
                    shape.getX2(), shape.getY2(),
                    shape.getColor()
            );
        } else if (shape instanceof Line) {
            return new Line(
                    shape.getX1(), shape.getY1(),
                    shape.getX2(), shape.getY2(),
                    shape.getColor()
            );
        } else if (shape instanceof Group) {
            Group groupClone = new Group();
            for (Shape child : ((Group) shape).getShapes()) {
                groupClone.addShape(cloneShape(child)); // Recursively clone child shapes
            }
            return groupClone;
        }
        return null;
    }

    public void cut() {
        if (!selectedShapes.isEmpty()) {
            clipboard.clear();
            for (Shape shape : selectedShapes) {
                Shape clonedShape = cloneShape(shape); // Clone before removing
                clipboard.add(clonedShape);
                undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.REMOVE, shape));
            }
            shapes.removeAll(selectedShapes);
            selectedShapes.clear();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No shapes selected to cut!");
        }
    }

    public void paste() {
        if (!clipboard.isEmpty()) {
            ArrayList<Shape> pastedShapes = new ArrayList<>();
            for (Shape shape : clipboard) {
                Shape newShape = cloneShape(shape); // Clone each shape from clipboard
                if (newShape != null) {
                    shapes.add(newShape);
                    pastedShapes.add(newShape);
                    undoRedo.recordAction(new ActionRecord(ActionRecord.ActionType.ADD, newShape));
                }
            }
            selectedShapes.clear();
            selectedShapes.addAll(pastedShapes);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Clipboard is empty! Nothing to paste.");
        }
    }


}
