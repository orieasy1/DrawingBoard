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

        // 구분선 생성 (TopPanel과 ToolPanel 사이)
        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalSeparator.setPreferredSize(new Dimension(0, 6)); // 높이 6px로 설정 (더 굵게)
        horizontalSeparator.setForeground(Color.BLACK); // 구분선 색상 진하게

        add(topPanel, BorderLayout.NORTH); // 상단 메뉴 및 Undo/Redo 버튼
        add(horizontalSeparator, BorderLayout.CENTER); // 구분선 추가
        add(toolPanel, BorderLayout.SOUTH); // 하단 도구 버튼들
    }

    // 상단 패널 생성
    private JPanel createTopPanel(Canvas canvas) {
        JPanel topPanel = new JPanel(new BorderLayout());

        // File 메뉴 생성
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Load 메뉴 항목 생성
        JMenuItem loadItem = new JMenuItem("Load");
        ImageIcon loadIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/load.png"));
        Image scaledLoadImage = loadIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // 아이콘 크기 조정
        loadItem.setIcon(new ImageIcon(scaledLoadImage));
        loadItem.setPreferredSize(new Dimension(120, 30)); // 메뉴 항목 크기 조정
        loadItem.addActionListener(e -> canvas.loadFromFile());

        // Save 메뉴 항목 생성
        JMenuItem saveItem = new JMenuItem("Save");
        ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/save.png"));
        Image scaledSaveImage = saveIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // 아이콘 크기 조정
        saveItem.setIcon(new ImageIcon(scaledSaveImage));
        saveItem.setPreferredSize(new Dimension(120, 30)); // 메뉴 항목 크기 조정
        saveItem.addActionListener(e -> canvas.saveToFile());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        // Undo, Redo 버튼
//        JButton undoButton = createButton("", "buttonImages/undo.png",e -> System.out.println("Undo clicked"),false);
//        ImageIcon undoIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/undo.png"));
//        undoButton.setIcon(new ImageIcon(undoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
//        JButton redoButton = createButton("", "buttonImages/redo.png",e -> System.out.println("Redo clicked"), false);
//        ImageIcon redoIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/redo.png"));
//        redoButton.setIcon(new ImageIcon(redoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        JButton undoButton = createButton("", "buttonImages/undo.png",
                e -> canvas.undo(), false);  // Changed to call canvas.undo()
        ImageIcon undoIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/undo.png"));
        undoButton.setIcon(new ImageIcon(undoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        JButton redoButton = createButton("", "buttonImages/redo.png",
                e -> canvas.redo(), false);  // Changed to call canvas.redo()
        ImageIcon redoIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/redo.png"));
        redoButton.setIcon(new ImageIcon(redoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

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
        editToolPanel.add(createButton("Selection", "buttonImages/selection.png", e -> canvas.setMode("Select"), false));      // selection 아이콘 추가
        editToolPanel.add(createButton("Paste", "buttonImages/paste.png", e -> System.out.println("Paste clicked"), false));    // paste 아이콘 추가
        editToolPanel.add(createButton("Cut", "buttonImages/cut.png", e -> System.out.println("Cut clicked"), false));          // cut 아이콘 추가
        editToolPanel.add(createButton("Group", "buttonImages/group.png", e -> System.out.println("Group clicked"), false));    // group 아이콘 추가

        // Drawing 도구 버튼 세트 (원, 사각형, 선) 및 텍스트
        JPanel drawingToolPanel = new JPanel(new BorderLayout());
        JPanel drawingButtonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 도형 버튼들
        drawingButtonPanel.add(createButton(null, "buttonImages/circle.png", e -> canvas.setMode("Circle"), false));          // circle 아이콘 추가
        drawingButtonPanel.add(createButton(null, "buttonImages/rectangle.png", e -> canvas.setMode("Rectangle"), false)); // rectangle 아이콘 추가
        drawingButtonPanel.add(createButton(null, "buttonImages/line.png", e -> canvas.setMode("Line"), false));                // line 아이콘 추가

        // Drawing 텍스트 추가 (하단 가운데 위치)
        JPanel drawingTextPanel = new JPanel(new BorderLayout());
        JLabel drawingLabel = new JLabel("Drawing", SwingConstants.CENTER);  // Drawing 텍스트 추가
        drawingTextPanel.add(drawingLabel, BorderLayout.CENTER);

        // 버튼 패널과 텍스트 패널을 drawingToolPanel에 추가
        drawingToolPanel.add(drawingButtonPanel, BorderLayout.CENTER);
        drawingToolPanel.add(drawingTextPanel, BorderLayout.SOUTH); // 하단 가운데로 배치

        // 색상 선택 버튼 세트 및 텍스트
        JPanel colorToolPanel = new JPanel(new BorderLayout());
        JPanel colorButtonPanel = new JPanel(new GridLayout(2, 6, 5, 5)); // 2행 색상 버튼들
        Color[] colors = {Color.BLACK,
                new Color(165, 42, 42), // Brown
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.BLUE,
                new Color(128, 0, 128), // Purple
                Color.PINK,
                Color.GRAY,
                Color.WHITE};
        for (Color color : colors) {
            JButton colorButton = new RoundButton(color);
            colorButton.setBackground(color);
            colorButton.addActionListener(e -> canvas.setColor(color));
            colorButtonPanel.add(colorButton);
        }

        // Color 텍스트 추가 (하단 가운데 위치)
        JPanel colorTextPanel = new JPanel(new BorderLayout());
        JLabel colorLabel = new JLabel("Color", SwingConstants.CENTER);  // Color 텍스트 추가
        colorTextPanel.add(colorLabel, BorderLayout.CENTER);

        // 버튼 패널과 텍스트 패널을 colorToolPanel에 추가
        colorToolPanel.add(colorButtonPanel, BorderLayout.CENTER);
        colorToolPanel.add(colorTextPanel, BorderLayout.SOUTH); // 하단 가운데로 배치

        // 각 세트를 toolPanel에 추가 (크기 조정 및 간격 설정)
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // 첫 번째 패널 (Edit Tool Panel)
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 10); // 오른쪽 간격
        toolPanel.add(editToolPanel, gbc);

        // 구분선 추가 (Edit Tool Panel과 Drawing Tool Panel 사이)
        gbc.weightx = 0.0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 10); // 좌우 간격
        toolPanel.add(createSeparator(), gbc);

        // 두 번째 패널 (Drawing Tool Panel)
        gbc.weightx = 0.3;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 10, 0, 10); // 좌우 간격
        toolPanel.add(drawingToolPanel, gbc);

        // 구분선 추가 (Drawing Tool Panel과 Color Tool Panel 사이)
        gbc.weightx = 0.0;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 10, 0, 10); // 좌우 간격
        toolPanel.add(createSeparator(), gbc);

        // 세 번째 패널 (Color Tool Panel)
        gbc.weightx = 0.2;
        gbc.gridx = 4;
        gbc.insets = new Insets(0, 10, 0, 0); // 왼쪽 간격
        toolPanel.add(colorToolPanel, gbc);

        return toolPanel;
    }
    
    // 구분선 생성 메서드
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(6, 0)); // 폭 6px로 설정 (더 굵게)
        separator.setForeground(Color.BLACK); // 구분선 색상 진하게
        return separator;
    }


    
    // 공용 버튼 생성 메서드 -> 해당 부분 이미지 나오게 수정
    private JButton createButton(String text, String imagePath, ActionListener action, boolean isSmall) {
        JButton button = new JButton();



        // 텍스트 설정 (text가 null 또는 빈 문자열이면 텍스트를 표시하지 않음)
        if (text != null && !text.isEmpty()) {
            button.setText(text);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        }

        // 이미지 로드 및 설정
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
                int size = isSmall ? 20 : 20; // 크기 조정: 작은 버튼은 20x20, 기본은 30x30
                Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                System.err.println("Image not found: " + imagePath);
            }
        }

        // 버튼 느낌이 안나게 일부 삭제
        button.setFocusPainted(false);       // 클릭 시 포커스 표시 제거
        button.setContentAreaFilled(false); // 배경 채우기 제거
        button.setBorderPainted(false);     // 버튼 테두리 제거

        // 버튼 눌렀을 때 느낌만 나게 설정
        button.getModel().addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) { // 버튼이 눌렸을 때
                button.setContentAreaFilled(true); // 배경 활성화
                button.setBorderPainted(true);     // 테두리 활성화
            } else { // 기본 상태
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
            }
        });

        button.addActionListener(action);

        return button;
    }
    
    // 동그란 버튼 클래스
    static class RoundButton extends JButton {
        public RoundButton(Color color) {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(20, 20)); // 30 -> 20으로 변경
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
            return new Dimension(20, 20); // 원형 크기 30 -> 20으로 변경
        }
    }
}