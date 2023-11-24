package papplevaa.notepad.model;

import org.junit.Test;

import java.io.File;

import static  org.junit.Assert.*;

public class TabTest {
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

    @Test
    public void testSetName() {
        // Arrange
        Tab tab = new Tab();
        // Act
        tab.setTitle("Title");
        // Assert
        assertEquals("Title", tab.getTitle());
    }

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

    @Test
    public void testSetCurrentContent() {
        // Arrange
        Tab tab = new Tab();
        // Act
        tab.setCurrentContent("Content");
        // Assert
        assertEquals("Content", tab.getCurrentContent());
    }

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
