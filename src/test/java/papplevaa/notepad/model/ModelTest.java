package papplevaa.notepad.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    Model model;

    @Before
    public void setup() {
        this.model = new Model();
    }

    @Test
    public void testAddTab() {
        // Arrange
        Tab newTab = new Tab();
        // Act
        int index = model.addTab(newTab);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertFalse(model.isSelected());
        assertEquals(-1, model.getSelectedIndex());
        assertEquals(newTab, model.getTabAt(index));
        assertSame(newTab, model.getTabAt(index));
    }

    @Test (expected = NullPointerException.class)
    public void testAddNullTab() {
        // Act
        model.addTab(null);
    }

    @Test
    public void testRemoveNotSelectedTab() {
        // Arrange
        int first = model.addTab(new Tab());
        int second = model.addTab(new Tab("Bananas", "Absolute bananas!", null));
        model.setSelectedIndex(second);
        // Act
        model.removeTab(first);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(second, model.getSelectedIndex());
    }

    @Test
    public void testRemoveSelectedTab() {
        // Arrange
        int first = model.addTab(new Tab());
        int second = model.addTab(new Tab("Bananas", "Absolute bananas!", null));
        model.setSelectedIndex(second);
        // Act
        model.removeTab(second);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(first, model.getSelectedIndex());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRemoveTabInvalidIndex() {
        model.removeTab(1);
    }
}
