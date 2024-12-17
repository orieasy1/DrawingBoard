package com.team2.shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Group implements Shape {
    private final List<Shape> shapes;
    private boolean isGrouped;

    public Group() {
        this.shapes = new ArrayList<>();
        this.isGrouped = true;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setGrouped(boolean grouped) {
        isGrouped = grouped;
    }

    @Override
    public void draw(Graphics g) {
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }

    @Override
    public void setX2(int x2) {
        int dx = x2 - getX2();
        for (Shape shape : shapes) {
            shape.setX2(shape.getX2() + dx);
        }
    }

    @Override
    public void setY2(int y2) {
        int dy = y2 - getY2();
        for (Shape shape : shapes) {
            shape.setY2(shape.getY2() + dy);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Rectangle getBoundingBox() {
        if (shapes.isEmpty()) return null;

        int minX = getX1();
        int minY = getY1();
        int maxX = getX2();
        int maxY = getY2();

        return new Rectangle(minX, minY, maxX, maxY, Color.BLACK);
    }

    @Override
    public void setColor(Color color) {
        for (Shape shape : shapes) {
            shape.setColor(color);
        }
    }

    @Override
    public Color getColor() {
        return shapes.isEmpty() ? Color.BLACK : shapes.get(0).getColor();
    }

    @Override
    public int getX1() {
        if (shapes.isEmpty()) return 0;
        int minX = Integer.MAX_VALUE;
        for (Shape shape : shapes) {
            minX = Math.min(minX, Math.min(shape.getX1(), shape.getX2()));
        }
        return minX;
    }

    @Override
    public int getY1() {
        if (shapes.isEmpty()) return 0;
        int minY = Integer.MAX_VALUE;
        for (Shape shape : shapes) {
            minY = Math.min(minY, Math.min(shape.getY1(), shape.getY2()));
        }
        return minY;
    }

    @Override
    public int getX2() {
        if (shapes.isEmpty()) return 0;
        int maxX = Integer.MIN_VALUE;
        for (Shape shape : shapes) {
            maxX = Math.max(maxX, Math.max(shape.getX1(), shape.getX2()));
        }
        return maxX;
    }

    @Override
    public int getY2() {
        if (shapes.isEmpty()) return 0;
        int maxY = Integer.MIN_VALUE;
        for (Shape shape : shapes) {
            maxY = Math.max(maxY, Math.max(shape.getY1(), shape.getY2()));
        }
        return maxY;
    }

    @Override
    public void moveBy(int dx, int dy) {
        for (Shape shape : shapes) {
            shape.moveBy(dx, dy);
        }
    }

    @Override
    public void moveTo(Point p) {
        int dx = p.x - getX1();
        int dy = p.y - getY1();
        moveBy(dx, dy);
    }
}
