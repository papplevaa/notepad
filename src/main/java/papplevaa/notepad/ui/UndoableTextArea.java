package papplevaa.notepad.ui;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

/**
 * A custom extension of JTextArea that supports undo and redo operations using an UndoManager.
 */
public class UndoableTextArea extends JTextArea {
    /** The UndoManager responsible for handling undo and redo operations. */
    private UndoManager undoManager;

    /**
     * Constructs a new UndoableTextArea with default content and sets up the UndoManager.
     */
    public UndoableTextArea() {
        super();
        setupUndoManager();
    }

    /**
     * Constructs a new UndoableTextArea with the specified initial content and sets up the UndoManager.
     *
     * @param content The initial content of the text area.
     */
    public UndoableTextArea(String content) {
        super(content);
        setupUndoManager();
    }

    /**
     * Constructs a new UndoableTextArea with the specified document and sets up the UndoManager.
     *
     * @param doc The document to use for the text area.
     */
    public UndoableTextArea(Document doc) {
        super(doc);
        setupUndoManager();
    }

    /**
     * Performs an undo operation using the UndoManager.
     */
    public void undo() {
        undoManager.undo();
    }

    /**
     * Performs a redo operation using the UndoManager.
     */
    public void redo() {
        undoManager.redo();
    }

    /**
     * Sets up the UndoManager for the text area.
     * Configures the UndoManager with a limit of 100 edits and adds an UndoableEditListener to the document associated with the text area.
     */
    private void setupUndoManager() {
        this.undoManager = new UndoManager();
        this.undoManager.setLimit(100);
        this.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
    }
}
