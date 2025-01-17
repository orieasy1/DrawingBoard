package com.team2.shapes;

import java.awt.*;

public class Circle implements Shape {
    private int x1, y1, x2, y2;
    private Color color;

    public Circle(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public void setX2(int x2) { this.x2 = x2; }
    public void setY2(int y2) { this.y2 = y2; }
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }

    @Override
    public void moveBy(int dx, int dy) {
        this.x1 += dx;
        this.y1 += dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    @Override
    public void moveTo(Point p) {
        int dx = p.x - this.x1;
        int dy = p.y - this.y1;
        moveBy(dx, dy);
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int diameter = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        g.drawOval(Math.min(x1, x2), Math.min(y1, y2), diameter, diameter);
    }

    @Override
    public boolean contains(int x, int y) {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radius = Math.abs(x2 - x1) / 2;
        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2);
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                Math.min(getX1(), getX2()),
                Math.min(getY1(), getY2()),
                Math.abs(getX2() - getX1()),
                Math.abs(getY2() - getY1())
        );
    }
}


