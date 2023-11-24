package papplevaa.notepad.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Model implements Serializable {
    private static final File data = new File(System.getProperty("user.home") + File.separator + "notepad.data");
    private List<Tab> tabs;
    private int selectedIndex;
    private boolean darkMode;
    private int height;
    private int width;
    private static final int MINHEIGHT = 240;
    private static final int MINWIDTH = 320;

    public Model() {
        this.tabs = new ArrayList<>();
        this.selectedIndex = -1;
        this.darkMode = true;
        this.height = Model.MINHEIGHT;
        this.width = Model.MINWIDTH;
    }

    public static File getDataPath() {
        return data;
    }

    public void addTab(Tab tab) {
        if(tab == null) {
            throw new NullPointerException("Adding null as tab!");
        }
        this.tabs.add(tab);
    }

    public void removeTab(int tabIndex) {
        if(tabIndex < 0 || tabIndex >= this.tabs.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        this.tabs.remove(tabIndex);
        if(this.selectedIndex == tabIndex && tabIndex == tabs.size()) {
            this.selectedIndex = tabs.size() - 1;
        }
    }

    public int getNumberOfTabs() {
        return this.tabs.size();
    }

    public boolean isSelected() {
        return this.selectedIndex != -1;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Tab getTabAt(int index) {
        if(index < 0 || index >= this.tabs.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        return this.tabs.get(index);
    }

    public int indexOfTab(Tab tab) throws NoSuchElementException {
        int index = this.tabs.indexOf(tab);
        if(index == -1) {
            throw new NoSuchElementException("No such tab in model");
        }
        return index;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public int getWindowWidth() {
        return this.width;
    }

    public void setWindowWidth(int width) {
        this.width = width;
    }

    public int getMinimumWindowWidth() {
        return Model.MINWIDTH;
    }

    public int getWindowHeight() {
        return this.height;
    }

    public void setWindowHeight(int height) {
        this.height = height;
    }

    public int getMinimumWindowHeight() {
        return Model.MINHEIGHT;
    }
}
