package papplevaa;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class UndoableTextArea extends JTextArea {
    private UndoManager undoManager;

    public UndoableTextArea(String content) {
        super(content);
        this.undoManager = new UndoManager();
        this.undoManager.setLimit(100);
    }

    public UndoManager getUndoManager() {
        return this.undoManager;
    }

    public void undo() {
        if(this.undoManager.canUndo()) {
            this.undoManager.undo();
        }
    }

    public void redo() {
        if(this.undoManager.canRedo()) {
            this.undoManager.redo();
        }
    }
}
