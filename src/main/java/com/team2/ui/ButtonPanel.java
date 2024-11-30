package com.team2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    public ButtonPanel(Canvas canvas) {
        setLayout(new BorderLayout()); // 전체 레이아웃 설정

        // 상단 버튼 및 메뉴
        JPanel topPanel = createTopPanel(canvas);

        // 아래쪽 도구 버튼들
        JPanel toolPanel = createToolPanel(canvas);

        add(topPanel, BorderLayout.NORTH); // 상단 메뉴 및 Undo/Redo 버튼
        add(toolPanel, BorderLayout.CENTER); // 하단 도구 버튼들
    }

    // 상단 패널 생성
    private JPanel createTopPanel(Canvas canvas) {
        JPanel topPanel = new JPanel(new BorderLayout());

        // File 메뉴 생성
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> canvas.loadFromFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> canvas.saveToFile());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        // Undo, Redo 버튼
        JButton undoButton = createButton("←", e -> System.out.println("Undo clicked"));
        JButton redoButton = createButton("→", e -> System.out.println("Redo clicked"));
        menuBar.add(undoButton);
        menuBar.add(redoButton);

        topPanel.add(menuBar, BorderLayout.WEST); // 왼쪽에 File 메뉴

        return topPanel;
    }

    // 도구 패널 생성
    private JPanel createToolPanel(Canvas canvas) {
        JPanel toolPanel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
        GridBagConstraints gbc = new GridBagConstraints();

        // Selection, Paste, Cut, Group 버튼 세트
        JPanel editToolPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        editToolPanel.add(createButton("Selection", e -> canvas.setMode("Select")));
        editToolPanel.add(createButton("Paste", e -> System.out.println("Paste clicked")));
        editToolPanel.add(createButton("Cut", e -> System.out.println("Cut clicked")));
        editToolPanel.add(createButton("Group", e -> System.out.println("Group clicked")));

        // Drawing 도구 버튼 세트 (원, 사각형, 선)
        JPanel drawingToolPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        drawingToolPanel.add(createButton("Circle", e -> canvas.setMode("Circle")));
        drawingToolPanel.add(createButton("Rectangle", e -> canvas.setMode("Rectangle")));
        drawingToolPanel.add(createButton("Line", e -> canvas.setMode("Line")));

        // 색상 선택 버튼 세트
        JPanel colorToolPanel = new JPanel(new GridLayout(1, 10, 5, 0));
        Color[] colors = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA, Color.GRAY, Color.CYAN};
        for (Color color : colors) {
            JButton colorButton = new RoundButton(color);
            colorButton.setBackground(color);
            colorButton.addActionListener(e -> canvas.setColor(color));
            colorToolPanel.add(colorButton);
        }

        // 각 세트를 toolPanel에 추가 (크기 조정)
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5; // editToolPanel이 더 넓게 차지
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        toolPanel.add(editToolPanel, gbc);

        gbc.weightx = 0.3; // drawingToolPanel이 적게 차지
        gbc.gridx = 1;
        toolPanel.add(drawingToolPanel, gbc);

        gbc.weightx = 0.2; // colorToolPanel이 가장 적게 차지
        gbc.gridx = 2;
        toolPanel.add(colorToolPanel, gbc);

        return toolPanel;
    }

    // 공용 버튼 생성 메서드
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(80, 40));
        return button;
    }

    // 동그란 버튼 클래스
    static class RoundButton extends JButton {
        public RoundButton(Color color) {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(30, 30));
            setBackground(color);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            g2.setColor(getBackground());
            g2.fillOval(0, 0, size, size);

            setOpaque(false);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(30, 30); // 원형 크기
        }
    }
}
