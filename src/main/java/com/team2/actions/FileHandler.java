package com.team2.actions;

import com.team2.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void saveToFile(List<Shape> shapes, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(parent);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                oos.writeObject(shapes);
                JOptionPane.showMessageDialog(parent, "File saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error saving file: " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked") // 경고 억제
    public static ArrayList<Shape> loadFromFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(parent);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                return (ArrayList<Shape>) ois.readObject(); // 명시적 캐스팅
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(parent, "Error loading file: " + e.getMessage());
            }
        }
        return new ArrayList<>(); // 실패 시 빈 리스트 반환
    }

}



