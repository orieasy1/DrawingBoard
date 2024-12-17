package com.team2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {
    private JButton groupButton;

    public ButtonPanel(Canvas canvas) {
        setLayout(new BorderLayout());


        JPanel topPanel = createTopPanel(canvas);


        JPanel toolPanel = createToolPanel(canvas);


        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalSeparator.setPreferredSize(new Dimension(0, 6)); //
        horizontalSeparator.setForeground(Color.BLACK); //

        add(topPanel, BorderLayout.NORTH); // Undo/Redo
        add(horizontalSeparator, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel(Canvas canvas) {
        JPanel topPanel = new JPanel(new BorderLayout());

        // File
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Load
        JMenuItem loadItem = new JMenuItem("Load");
        ImageIcon loadIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/load.png"));
        Image scaledLoadImage = loadIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        loadItem.setIcon(new ImageIcon(scaledLoadImage));
        loadItem.setPreferredSize(new Dimension(120, 30));
        loadItem.addActionListener(e -> canvas.loadFromFile());

        // Save
        JMenuItem saveItem = new JMenuItem("Save");
        ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("buttonImages/save.png"));
        Image scaledSaveImage = saveIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        saveItem.setIcon(new ImageIcon(scaledSaveImage));
        saveItem.setPreferredSize(new Dimension(120, 30));
        saveItem.addActionListener(e -> canvas.saveToFile());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        // Undo, Redo
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

        topPanel.add(menuBar, BorderLayout.WEST);

        return topPanel;
    }


    private JPanel createToolPanel(Canvas canvas) {
        JPanel toolPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Group
        groupButton = createButton("Group", "buttonImages/group.png", e -> {
            if (canvas.getSelectedShapes().size() >= 2) {
                if (canvas.isGroupActivated()) {
                    canvas.ungroupSelectedShapes();
                    groupButton.setBackground(null);  // 배경색으로 활성화 상태 표시
                } else {
                    canvas.groupSelectedShapes();
                    groupButton.setBackground(new Color(135, 206, 235));  // 하늘색
                }
            }
        }, false);

        // Selection, Paste, Cut, Group
        JPanel editToolPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        editToolPanel.add(createButton("Selection", "buttonImages/selection.png", e -> canvas.setMode("Select"), false));
        editToolPanel.add(createButton("Cut", "buttonImages/cut.png", e -> canvas.cut(), false));
        editToolPanel.add(createButton("Paste", "buttonImages/paste.png", e -> canvas.paste(), false));
        editToolPanel.add(groupButton);

        // Drawing
        JPanel drawingToolPanel = new JPanel(new BorderLayout());
        JPanel drawingButtonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        drawingButtonPanel.add(createButton(null, "buttonImages/circle.png", e -> canvas.setMode("Circle"), false));          // circle
        drawingButtonPanel.add(createButton(null, "buttonImages/rectangle.png", e -> canvas.setMode("Rectangle"), false)); // rectangle
        drawingButtonPanel.add(createButton(null, "buttonImages/line.png", e -> canvas.setMode("Line"), false));                // line

        // Drawing
        JPanel drawingTextPanel = new JPanel(new BorderLayout());
        JLabel drawingLabel = new JLabel("Drawing", SwingConstants.CENTER);  // Drawing
        drawingTextPanel.add(drawingLabel, BorderLayout.CENTER);


        drawingToolPanel.add(drawingButtonPanel, BorderLayout.CENTER);
        drawingToolPanel.add(drawingTextPanel, BorderLayout.SOUTH);


        JPanel colorToolPanel = new JPanel(new BorderLayout());
        JPanel colorButtonPanel = new JPanel(new GridLayout(2, 6, 5, 5));
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

        // Color
        JPanel colorTextPanel = new JPanel(new BorderLayout());
        JLabel colorLabel = new JLabel("Color", SwingConstants.CENTER);  // Color
        colorTextPanel.add(colorLabel, BorderLayout.CENTER);


        colorToolPanel.add(colorButtonPanel, BorderLayout.CENTER);
        colorToolPanel.add(colorTextPanel, BorderLayout.SOUTH);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;


        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        toolPanel.add(editToolPanel, gbc);


        gbc.weightx = 0.0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 10);
        toolPanel.add(createSeparator(), gbc);


        gbc.weightx = 0.3;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 10, 0, 10);
        toolPanel.add(drawingToolPanel, gbc);


        gbc.weightx = 0.0;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 10, 0, 10);
        toolPanel.add(createSeparator(), gbc);


        gbc.weightx = 0.2;
        gbc.gridx = 4;
        gbc.insets = new Insets(0, 10, 0, 0);
        toolPanel.add(colorToolPanel, gbc);


        return toolPanel;
    }


    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(6, 0));
        separator.setForeground(Color.BLACK);
        return separator;
    }



    private JButton createButton(String text, String imagePath, ActionListener action, boolean isSmall) {
        JButton button = new JButton();




        if (text != null && !text.isEmpty()) {
            button.setText(text);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        }


        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
                int size = isSmall ? 20 : 20;
                Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                System.err.println("Image not found: " + imagePath);
            }
        }


        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);


        button.getModel().addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {
                button.setContentAreaFilled(true);
                button.setBorderPainted(true);
            } else {
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
            }
        });

        button.addActionListener(action);

        return button;
    }
    

    static class RoundButton extends JButton {
        public RoundButton(Color color) {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(20, 20));
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
            return new Dimension(20, 20);
        }
    }
}