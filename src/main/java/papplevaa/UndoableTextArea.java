package papplevaa;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class UndoableTextArea extends JTextArea {
    private UndoManager undoManager;

    public UndoableTextArea() {
        super();
        initUndoManager();
    }

    public UndoableTextArea(String content) {
        super(content);
        initUndoManager();
    }

    public UndoableTextArea(Document doc) {
        super(doc);
        initUndoManager();
    }

    public UndoManager getUndoManager() {
        return this.undoManager;
    }

    public void undo() {
        if(this.undoManager.canUndo()) {
            this.undoManager.undo();
            System.out.println("Undo request proccessed succesfully");
        }
    }

    public void redo() {
        if(this.undoManager.canRedo()) {
            this.undoManager.redo();
            System.out.println("Redo request proccessed succesfully");
        }
    }

    private void initUndoManager() {
        this.undoManager = new UndoManager();
        this.undoManager.setLimit(100);
    }
}
