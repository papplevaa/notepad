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
        Tab newTab = new Tab();
        int index = model.addTab(newTab);
        assertEquals(1, model.getNumberOfTabs());
        assertFalse(model.isSelected());
        assertEquals(-1, model.getSelectedIndex());
        assertEquals(newTab, model.getTabAt(index));
        assertSame(newTab, model.getTabAt(index));
    }
}
