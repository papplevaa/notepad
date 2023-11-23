package papplevaa.notepad.ui;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotUndoException;

public class UndoableTextAreaTest {
    UndoableTextArea textArea;

    @Test
    public void testUndoableTextAreaInitialization() {
        // Act
        textArea = new UndoableTextArea();
        // Assert
        assertNotNull(textArea);
    }

    @Test
    public void testUndoableTextAreaWithContent() {
        // Arrange
        String initialContent = "Some test content";
        // Act
        textArea = new UndoableTextArea(initialContent);
        // Assert
        assertNotNull(textArea);
        assertEquals(initialContent, textArea.getText());
    }

    @Test
    public void testUndoableTextAreaWithDocument() {
        // Arrange
        Document initialDocument = new PlainDocument();
        // Act
        textArea = new UndoableTextArea(initialDocument);
        // Assert
        assertEquals(initialDocument, textArea.getDocument());
    }

    @Test
    public void testUndo() throws CannotUndoException {
        // Arrange
        textArea = new UndoableTextArea();
        textArea.append("Test");
        // Act
        textArea.undo();
        // Assert
        assertEquals("", textArea.getText());
    }

    @Test (expected = RuntimeException.class)
    public void testUndoWithoutChange() {
        // Arrange
        textArea = new UndoableTextArea();
        // Act
        textArea.undo();
    }

    @Test
    public void testRedo() {
        // Arrange
        textArea = new UndoableTextArea();
        textArea.append("Testing!!!");
        textArea.undo();
        // Act
        textArea.redo();
        // Assert
        assertEquals("Testing!!!", textArea.getText());
    }

    @Test (expected = RuntimeException.class)
    public void testRedoWithoutUndo() {
        // Arrange
        textArea = new UndoableTextArea("Test");
        // Act
        textArea.redo();
    }

    @Test (expected = RuntimeException.class)
    public void testUndoManagerLimit() {
        // Arrange
        textArea = new UndoableTextArea();
        for(int i = 1; i <= 110; i++) {
            textArea.append("Undoable edit #" + i);
        }
        // Act
        for(int i = 1; i <= 101; i++) {
            textArea.undo();
        }
    }
}
