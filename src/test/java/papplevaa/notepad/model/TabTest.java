package papplevaa.notepad.model;

import org.junit.Test;

import java.io.File;

import static  org.junit.Assert.*;

/**
 * Unit tests for the Tab class.
 */
public class TabTest {
    /**
     * Test: Default constructor.
     * Expected: Tab is created with default values.
     */
    @Test
    public void testDefaultConstructor() {
        // Act
        Tab tab = new Tab();
        // Assert
        assertNull(tab.getFilePath());
        assertEquals("Untitled", tab.getTitle());
        assertEquals("", tab.getLastSavedContent());
        assertEquals("", tab.getCurrentContent());
    }

    /**
     * Test: Parameterized constructor.
     * Expected: Tab is created with specified values.
     */
    @Test
    public void testParameterizedConstructor() {
        // Act
        File file = new File("input/load.txt");
        Tab tab = new Tab("Title", "Content", file);
        // Assert
        assertEquals(file, tab.getFilePath());
        assertEquals("Title", tab.getTitle());
        assertEquals("Content", tab.getLastSavedContent());
        assertEquals("Content", tab.getCurrentContent());
    }

    /**
     * Test: setName method.
     * Expected: Tab's title is set correctly.
     */
    @Test
    public void testSetName() {
        // Arrange
        Tab tab = new Tab();
        // Act
        tab.setTitle("Title");
        // Assert
        assertEquals("Title", tab.getTitle());
    }

    /**
     * Test: setFilePath method.
     * Expected: Tab's file path is set correctly.
     */
    @Test
    public void testSetFilePath() {
        // Arrange
        Tab tab = new Tab();
        File file = new File("input/load.txt");
        // Act
        tab.setFilePath(file);
        // Assert
        assertEquals(file, tab.getFilePath());
    }

    /**
     * Test: setCurrentContent method.
     * Expected: Tab's current content is set correctly.
     */
    @Test
    public void testSetCurrentContent() {
        // Arrange
        Tab tab = new Tab();
        // Act
        tab.setCurrentContent("Content");
        // Assert
        assertEquals("Content", tab.getCurrentContent());
    }

    /**
     * Test: isUnsaved method.
     * Expected: Returns true.
     */
    @Test
    public void testIsUnsaved() {
        // Arrange
        Tab tab = new Tab();
        tab.setCurrentContent("Unsaved content");
        // Act
        boolean unsaved = tab.isUnsaved();
        // Assert
        assertTrue(unsaved);
    }

    /**
     * Test: commitChanges method.
     * Expected: Last saved content is updated with the current content.
     */
    @Test
    public void testCommitChanges() {
        // Arrange
        Tab tab = new Tab();
        tab.setCurrentContent("Modified Content");
        // Act
        tab.commitChanges();
        // Assert
        assertEquals(tab.getCurrentContent(), tab.getLastSavedContent());
    }
}
