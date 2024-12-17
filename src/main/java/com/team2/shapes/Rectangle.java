package com.team2.shapes;

import java.awt.*;

public class Rectangle implements Shape {
    private int x1, y1, x2, y2;
    private Color color;

    public Rectangle(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public Rectangle(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = Color.BLACK;
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
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);
        g.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
    }

    @Override
    public boolean contains(int x, int y) {
        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);
        return x >= left && x <= right && y >= top && y <= bottom;
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
    // Rectangle 클래스에 추가
    public boolean intersects(Shape other) {
        int thisLeft = Math.min(getX1(), getX2());
        int thisRight = Math.max(getX1(), getX2());
        int thisTop = Math.min(getY1(), getY2());
        int thisBottom = Math.max(getY1(), getY2());

        int otherLeft = Math.min(other.getX1(), other.getX2());
        int otherRight = Math.max(other.getX1(), other.getX2());
        int otherTop = Math.min(other.getY1(), other.getY2());
        int otherBottom = Math.max(other.getY1(), other.getY2());

        // 두 사각형이 겹치지 않는 경우를 체크
        return !(thisLeft > otherRight ||
                thisRight < otherLeft ||
                thisTop > otherBottom ||
                thisBottom < otherTop);
    }
}




