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
        assertEquals("Untitled", tab.getName());
        assertEquals("", tab.getLastSavedContent());
        assertEquals("", tab.getCurrentContent());
    }

    @Test
    public void testParameterizedConstructor() {
        // Act
        File file = new File("test.txt");
        Tab tab = new Tab("Title", "Content", file);
        // Assert
        assertEquals(file, tab.getFilePath());
        assertEquals("Title", tab.getName());
        assertEquals("Content", tab.getLastSavedContent());
        assertEquals("Content", tab.getCurrentContent());
    }

    @Test
    public void testSetName() {
        // Arrange
        Tab tab = new Tab();
        // Act
        tab.setName("Title");
        // Assert
        assertEquals("Title", tab.getName());
    }

    @Test
    public void testSetFilePath() {
        // Arrange
        Tab tab = new Tab();
        File file = new File("test.txt");
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
