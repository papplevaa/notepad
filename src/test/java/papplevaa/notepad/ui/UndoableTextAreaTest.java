package papplevaa.notepad.ui;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotUndoException;

/**
 * Unit tests for the UndoableTextArea class.
 */
public class UndoableTextAreaTest {
    UndoableTextArea textArea;

    /**
     * Test: Default constructor.
     * Expected: UndoableTextArea is created successfully.
     */
    @Test
    public void testUndoableTextAreaInitialization() {
        // Act
        textArea = new UndoableTextArea();
        // Assert
        assertNotNull(textArea);
    }

    /**
     * Test: Parameterized constructor where the parameter is the initial content stored in a String.
     * Expected: UndoableTextArea is created with the specified initial content.
     */
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

    /**
     * Test: Parameterized constructor where the parameter is the initial content stored in a document.
     * Expected: UndoableTextArea is created with the specified initial document.
     */
    @Test
    public void testUndoableTextAreaWithDocument() {
        // Arrange
        Document initialDocument = new PlainDocument();
        // Act
        textArea = new UndoableTextArea(initialDocument);
        // Assert
        assertEquals(initialDocument, textArea.getDocument());
    }

    /**
     * Test: Undo operation.
     * Expected: Content is set to the state before the last change.
     */
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

    /**
     * Test: Undo operation without any change.
     * Expected: RuntimeException (CannotUndoException) is thrown.
     */
    @Test (expected = RuntimeException.class)
    public void testUndoWithoutChange() {
        // Arrange
        textArea = new UndoableTextArea();
        // Act
        textArea.undo();
    }

    /**
     * Test: Redo operation.
     * Expected: Content is restored to the state after the last undone change.
     */
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

    /**
     * Test: Redo operation without any undo.
     * Expected: RuntimeException (CannotRedoException) is thrown.
     */
    @Test (expected = RuntimeException.class)
    public void testRedoWithoutUndo() {
        // Arrange
        textArea = new UndoableTextArea("Test");
        // Act
        textArea.redo();
    }

    /**
     * Test: UndoManager limit.
     * Expected: RuntimeException is thrown when exceeding the limit.
     */
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
