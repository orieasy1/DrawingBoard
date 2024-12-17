package com.team2.shapes;

import java.awt.*;
import java.io.Serializable;

public interface Shape extends Serializable {
    void draw(Graphics g);
    void setX2(int x2);
    void setY2(int y2);
    boolean contains(int x, int y);
    Rectangle getBoundingBox();
    void setColor(Color color);
    Color getColor();
    int getX1();
    int getY1();
    int getX2();
    int getY2();
    public void moveBy(int dx, int dy);
    public void moveTo(Point p);       
}
