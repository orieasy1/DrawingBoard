package com.team2.actions;

import com.team2.ui.Canvas;

import java.util.Stack;

public class UndoRedo {
    private Stack<ActionRecord> undoStack = new Stack<>();
    private Stack<ActionRecord> redoStack = new Stack<>();
    private Canvas canvas;

    public UndoRedo(Canvas canvas) {
        this.canvas = canvas;
    }

    public UndoRedo(){

    }

    public void recordAction(ActionRecord action) {
        undoStack.push(action);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            ActionRecord action = undoStack.pop();
            redoStack.push(action);

            switch (action.getType()) {
                case ADD:
                    canvas.removeShape(action.getShape(), false);
                    System.out.println("add");
                    break;
                case REMOVE:
                    canvas.addShape(action.getShape(), false);
                    System.out.println("rm");
                    break;
                case COLOR_CHANGE:
                    action.getShape().setColor(action.getPreviousColor());
                    canvas.repaint();
                    break;
                case MOVE:
                    action.getShape().moveTo(action.getPreviousPosition());
                    canvas.repaint();
                    break;
            }
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            ActionRecord action = redoStack.pop();
            undoStack.push(action);

            switch (action.getType()) {
                case ADD:
                    canvas.addShape(action.getShape(), false);
                    break;
                case REMOVE:
                    canvas.removeShape(action.getShape(), false);
                    break;
                case COLOR_CHANGE:
                    action.getShape().setColor(action.getNewColor());
                    canvas.repaint();
                    break;
                case MOVE:
                    action.getShape().moveTo(action.getNewPosition());
                    canvas.repaint();
                    break;
            }
        }
    }

//    public void undo() {
//        if (!undoStack.isEmpty()) {
//            ActionRecord action = undoStack.pop();
//            redoStack.push(action);
//
//            switch (action.getType()) {
//                case ADD:
//                    canvas.removeShape(action.getShape(), false);
//                    break;
//                case REMOVE:
//                    canvas.addShape(action.getShape(), false);
//                    break;
//                case COLOR_CHANGE:
//                    action.getShape().setColor(action.getPreviousColor());
//                    canvas.repaint();
//                    break;
//                case MOVE:
//                    action.getShape().moveTo(action.getPreviousPosition());
//                    canvas.repaint();
//                    break;
//            }
//        }
//    }

//    public void redo() {
//        if (!redoStack.isEmpty()) {
//            ActionRecord action = redoStack.pop();
//            undoStack.push(action);
//
//            switch (action.getType()) {
//                case ADD:
//                    canvas.addShape(action.getShape(), false);
//                    break;
//                case REMOVE:
//                    canvas.removeShape(action.getShape(), false);
//                    break;
//                case COLOR_CHANGE:
//                    action.getShape().setColor(action.getPreviousColor());
//                    canvas.repaint();
//                    break;
//                case MOVE:
//                    action.getShape().moveTo(action.getPreviousPosition());
//                    canvas.repaint();
//                    break;
//            }
//        }
//
//
//    }
    public void clearStacks() {
        undoStack.clear();
        redoStack.clear();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
