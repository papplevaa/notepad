package papplevaa.notepad.ui;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class UndoableTextArea extends JTextArea {
    private UndoManager undoManager;

    public UndoableTextArea() {
        super();
        setupUndoManager();
    }

    public UndoableTextArea(String content) {
        super(content);
        setupUndoManager();
    }

    public UndoableTextArea(Document doc) {
        super(doc);
        setupUndoManager();
    }

    public void undo() {
        if(this.undoManager.canUndo()) {
            this.undoManager.undo();
        } else {
            throw new RuntimeException("Can not undo!");
        }
    }

    public void redo() {
        if(this.undoManager.canRedo()) {
            this.undoManager.redo();
        } else {
            throw new RuntimeException("Can not redo!");
        }
    }

    private void setupUndoManager() {
        this.undoManager = new UndoManager();
        this.undoManager.setLimit(100);
        this.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
    }
}
