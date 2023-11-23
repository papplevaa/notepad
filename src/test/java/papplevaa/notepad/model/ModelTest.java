package papplevaa.notepad.model;

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
        model.addTab(newTab);
        int index = model.getIndexOfTab(newTab);
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
        // Arrange
        Tab newTab = new Tab();
        model.addTab(newTab);
        int newTabIndex = model.getIndexOfTab(newTab);
        Tab bananaTab = new Tab("Bananas", "Absolute bananas!", null);
        model.addTab(bananaTab);
        int bananaTabIndex = model.getIndexOfTab(bananaTab);
        model.setSelectedIndex(bananaTabIndex);
        // Act
        model.removeTab(newTabIndex);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(bananaTabIndex, model.getSelectedIndex());
    }

    @Test
    public void testRemoveSelectedTab() {
        // Arrange
        Tab newTab = new Tab();
        model.addTab(newTab);
        int newTabIndex = model.getIndexOfTab(newTab);
        Tab bananaTab = new Tab("Bananas", "Absolute bananas!", null);
        model.addTab(bananaTab);
        int bananaTabIndex = model.getIndexOfTab(bananaTab);
        model.setSelectedIndex(bananaTabIndex);
        // Act
        model.removeTab(bananaTabIndex);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(newTabIndex, model.getSelectedIndex());
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testRemoveTabInvalidIndex() {
        model.removeTab(1);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testGetTabAtInvalidArgument() {
        model.getTabAt(1);
    }
}
