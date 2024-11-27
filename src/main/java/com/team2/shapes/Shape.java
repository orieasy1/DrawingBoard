package com.team2.shapes;

import java.awt.*;
import java.io.Serializable;

public interface Shape extends Serializable {
    void draw(Graphics g);
    void setX2(int x2);
    void setY2(int y2);
    boolean contains(int x, int y);
    java.awt.Rectangle getBoundingBox(); // 반환 타입을 명시적으로 java.awt.Rectangle로 변경
    void setColor(Color color);
    Color getColor();
    int getX1();
    int getY1();
    int getX2();
    int getY2();
    public void moveBy(int dx, int dy); // 상대 이동
    public void moveTo(Point p);        // 절대 위치로 이동

}
