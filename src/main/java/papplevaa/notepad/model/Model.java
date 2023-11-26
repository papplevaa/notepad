package papplevaa.notepad.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents the data model for a notepad application, managing tabs and application settings.
 * Implements the Serializable interface to support serialization.
 */
public class Model implements Serializable {
    /** The default data file path for serialization. */
    private static final File data = new File(System.getProperty("user.home") + File.separator + "notepad.data");
    /** The list of tabs managed by the model. */
    private List<Tab> tabs;
    /** The index of the currently selected tab. */
    private int selectedIndex;
    /** The flag indicating whether the dark mode is enabled or not. */
    private boolean darkMode;
    /** The height of the application window. */
    private int height;
    /** The width of the application window. */
    private int width;
    /** The minimum height of the application window. */
    private static final int MINHEIGHT = 240;
    /** The minimum width of the application window. */
    private static final int MINWIDTH = 320;

    /**
     * Constructs a new Model with default values.
     * Initializes an empty list of tabs, sets the selected index to -1, enables dark mode,
     * and sets the initial window dimensions to minimum values.
     */
    public Model() {
        this.tabs = new ArrayList<>();
        this.selectedIndex = -1;
        this.darkMode = true;
        this.height = Model.MINHEIGHT;
        this.width = Model.MINWIDTH;
    }

    /**
     * Gets the data file path for serialization.
     *
     * @return The data file path.
     */
    public static File getDataPath() {
        return data;
    }

    /**
     * Adds a new tab to the model.
     *
     * @param tab The tab to be added.
     * @throws NullPointerException if the provided tab is null.
     */
    public void addTab(Tab tab) {
        if(tab == null) {
            throw new NullPointerException("Adding null as tab!");
        }
        this.tabs.add(tab);
    }

    /**
     * Removes a tab at the specified index from the model.
     *
     * @param tabIndex The index of the tab to be removed.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public void removeTab(int tabIndex) {
        if(tabIndex < 0 || tabIndex >= this.tabs.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        this.tabs.remove(tabIndex);
        if(this.selectedIndex == tabIndex && tabIndex == tabs.size()) {
            this.selectedIndex = tabs.size() - 1;
        }
    }

    /**
     * Gets the number of tabs in the model.
     *
     * @return The number of tabs.
     */
    public int getNumberOfTabs() {
        return this.tabs.size();
    }

    /**
     * Checks if a tab is currently selected in the model.
     *
     * @return True if a tab is selected, false otherwise.
     */
    public boolean isSelected() {
        return this.selectedIndex != -1;
    }

    /**
     * Gets the index of the currently selected tab.
     *
     * @return The index of the selected tab.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Sets the index of the currently selected tab.
     *
     * @param selectedIndex The index to be set as the selected tab.
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Gets the tab at the specified index.
     *
     * @param index The index of the tab to be retrieved.
     * @return The tab at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public Tab getTabAt(int index) {
        if(index < 0 || index >= this.tabs.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        return this.tabs.get(index);
    }

    /**
     * Gets the index of a specified tab in the model.
     *
     * @param tab The tab to find the index of.
     * @return The index of the specified tab.
     * @throws NoSuchElementException if the tab is not found in the model.
     */
    public int indexOfTab(Tab tab) throws NoSuchElementException {
        int index = this.tabs.indexOf(tab);
        if(index == -1) {
            throw new NoSuchElementException("No such tab in model");
        }
        return index;
    }

    /**
     * Checks if dark mode is enabled.
     *
     * @return True if dark mode is enabled, false otherwise.
     */
    public boolean isDarkMode() {
        return darkMode;
    }

    /**
     * Sets the dark mode status.
     *
     * @param darkMode True to enable dark mode, false to disable.
     */
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    /**
     * Gets the width of the application window.
     *
     * @return The width of the window.
     */
    public int getWindowWidth() {
        return this.width;
    }

    /**
     * Sets the width of the application window.
     *
     * @param width The new width of the window.
     */
    public void setWindowWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the minimum width of the application window.
     *
     * @return The minimum width of the window.
     */
    public int getMinimumWindowWidth() {
        return Model.MINWIDTH;
    }

    /**
     * Gets the height of the application window.
     *
     * @return The height of the window.
     */
    public int getWindowHeight() {
        return this.height;
    }

    /**
     * Sets the height of the application window.
     *
     * @param height The new height of the window.
     */
    public void setWindowHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the minimum height of the application window.
     *
     * @return The minimum height of the window.
     */
    public int getMinimumWindowHeight() {
        return Model.MINHEIGHT;
    }
}
