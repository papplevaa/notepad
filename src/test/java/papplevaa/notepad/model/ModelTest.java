package papplevaa.notepad.model;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Unit tests for the Model class.
 */
public class ModelTest {
    Model model;

    @Before
    public void setup() {
        this.model = new Model();
    }

    /**
     * Test: indexOfTab with a non-existing tab.
     * Expected: NoSuchElementException is thrown.
     */
    @Test (expected = NoSuchElementException.class)
    public void testIndexOfNonExistingTab() {
        model.indexOfTab(new Tab());
    }

    /**
     * Test: getTabAt with an invalid index.
     * Expected: IndexOutOfBoundsException is thrown.
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testGetTabAtInvalidArgument() {
        model.getTabAt(1);
    }

    /**
     * Test: addTab method.
     * Expected: Tab is added to the model, and the model state is updated accordingly.
     */
    @Test
    public void testAddTab() {
        // Arrange
        Tab newTab = new Tab();
        // Act
        model.addTab(newTab);
        int index = model.indexOfTab(newTab);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertFalse(model.isSelected());
        assertEquals(-1, model.getSelectedIndex());
        assertEquals(newTab, model.getTabAt(index));
        assertSame(newTab, model.getTabAt(index));
    }

    /**
     * Test: addTab method with a null tab.
     * Expected: NullPointerException is thrown.
     */
    @Test (expected = NullPointerException.class)
    public void testAddNullTab() {
        // Act
        model.addTab(null);
    }

    /**
     * Test: removeTab method for a tab that is not selected.
     * Expected: Tab is removed, and the model state is updated accordingly.
     */
    @Test
    public void testRemoveNotSelectedTab() {
        // Arrange
        Tab newTab = new Tab();
        model.addTab(newTab);
        int newTabIndex = model.indexOfTab(newTab);
        Tab bananaTab = new Tab("Bananas", "Absolute bananas!", null);
        model.addTab(bananaTab);
        int bananaTabIndex = model.indexOfTab(bananaTab);
        model.setSelectedIndex(bananaTabIndex);
        // Act
        model.removeTab(newTabIndex);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(bananaTabIndex, model.getSelectedIndex());
    }

    /**
     * Test: removeTab method for a tab that is selected.
     * Expected: Tab is removed, and the model state is updated accordingly.
     */
    @Test
    public void testRemoveSelectedTab() {
        // Arrange
        Tab newTab = new Tab();
        model.addTab(newTab);
        int newTabIndex = model.indexOfTab(newTab);
        Tab bananaTab = new Tab("Bananas", "Absolute bananas!", null);
        model.addTab(bananaTab);
        int bananaTabIndex = model.indexOfTab(bananaTab);
        model.setSelectedIndex(bananaTabIndex);
        // Act
        model.removeTab(bananaTabIndex);
        // Assert
        assertEquals(1, model.getNumberOfTabs());
        assertEquals(newTabIndex, model.getSelectedIndex());
    }

    /**
     * Test: removeTab method with an invalid index.
     * Expected: IndexOutOfBoundsException is thrown.
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void testRemoveTabInvalidIndex() {
        model.removeTab(1);
    }
}
