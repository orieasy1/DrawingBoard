package com.team2;

import com.team2.actions.UndoRedo;
import com.team2.ui.ButtonPanel;
import com.team2.ui.Canvas;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Drawing Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        UndoRedo undoRedo = new UndoRedo(null); // 초기화 시 Canvas와 상호 참조 방지
        com.team2.ui.Canvas canvas = new Canvas(undoRedo);
        undoRedo = new UndoRedo(canvas); // Canvas와 UndoRedo 상호 연결
        ButtonPanel buttonPanel = new ButtonPanel(canvas, undoRedo);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}



