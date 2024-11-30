package com.team2.ui;

import com.team2.actions.UndoRedo;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Drawing Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Canvas 생성
        UndoRedo undoRedo = new UndoRedo(null);
        Canvas canvas = new Canvas(undoRedo);
        undoRedo = new UndoRedo(canvas); // Canvas와 UndoRedo 상호 연결

        // ButtonPanel 생성
        ButtonPanel buttonPanel = new ButtonPanel(canvas);

        // JFrame에 추가
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH); // 버튼 패널을 상단에 추가
        frame.add(canvas, BorderLayout.CENTER); // 캔버스를 중앙에 추가

        frame.setVisible(true);
    }
}



