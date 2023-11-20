package papplevaa;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Tab> tabs;
    private int selectedIndex;
    // Make view transient so it is not serialized?
    private View view;
    private boolean darkMode;
    private int height;
    private int width;
    private final int MINHEIGHT = 240;
    private final int MINWIDTH = 320;

    public Model(View view) {
        this.tabs = new ArrayList<>();
        this.selectedIndex = -1;
        this.view = view;
        this.darkMode = true;
        this.height = this.MINHEIGHT;
        this.width = this.MINWIDTH;
    }

    public int addTab(Tab tab) {
        if(tab == null) {
            throw new NullPointerException("Adding null as tab!");
        }
        this.tabs.add(tab);
        this.view.tabAdded(tab.getName(), tab.getCurrentContent());
        return this.tabs.indexOf(tab);
    }

    public void removeTab(int tabIndex) {
        if(tabIndex < 0 || tabIndex >= this.tabs.size()) {
            throw new IllegalArgumentException("Index is out of bounds!");
        }
        this.tabs.remove(tabIndex);
        this.view.tabRemoved(tabIndex);
        if(this.tabs.isEmpty()) {
            this.selectedIndex = -1;
            this.view.activeTabUpdated(this.selectedIndex);
        }
    }

    public boolean isSelected() {
        return this.selectedIndex != -1;
    }

    public void clearSelection() {
        this.selectedIndex = -1;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if(selectedIndex < 0 || selectedIndex >= tabs.size())
            throw new IllegalArgumentException("Index is out of bounds!");
        this.selectedIndex = selectedIndex;
        this.view.activeTabUpdated(selectedIndex);
    }

    public Tab getSelectedTab() {
        if(!isSelected()) {
            throw new RuntimeException("No tab is selected!");
        }
        return this.tabs.get(selectedIndex);
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        this.view.updateUIManager(darkMode);
    }

    public int getWindowWidth() {
        return this.width;
    }

    public void setWindowWidth(int width) {
        this.width = width;
    }

    public int getMinimumWindowWidth() {
        return this.MINWIDTH;
    }

    public int getWindowHeight() {
        return this.height;
    }

    public void setWindowHeight(int height) {
        this.height = height;
    }

    public int getMinimumWindowHeight() {
        return this.MINHEIGHT;
    }
}
