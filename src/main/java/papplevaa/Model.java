package papplevaa;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Tab> tabs;
    private int activeTab;
    // Make view transient so it is not serialized?
    private View view;
    private boolean darkMode;
    private int height;
    private int width;
    private final int MINHEIGHT = 240;
    private final int MINWIDTH = 320;

    public Model(View view) {
        this.tabs = new ArrayList<>();
        this.activeTab = -1;
        this.darkMode = true;
        this.view = view;
        //this.view.initialize(this);
        // Windowsize comes from view
        // Should not serialize view (make it transient?)
        //  - store window size in model
        //  - view initializes based on model upon start
    }

    public int getActiveTabIndex() {
        return activeTab;
    }

    public Tab getActiveTab() {
        return tabs.get(activeTab);
    }

    public void setActiveTabIndex(int activeTab) {
        if(activeTab < 0 || activeTab >= tabs.size())
            throw new IllegalArgumentException("Index is out of bounds!");
        this.activeTab = activeTab;
        this.view.activeTabUpdated(activeTab);
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

    public void addTab(Tab tab) {
        this.tabs.add(tab);
        this.view.tabAdded(tab.getName(), tab.getCurrentContent());
    }

    public void removeTab(int tabIndex) {
        tabs.remove(tabIndex);
        if(activeTab == tabIndex) {
            if(tabs.isEmpty()) activeTab = -1;
            else activeTab = (tabIndex - 1) % tabs.size();
        }
        this.view.tabRemoved(tabIndex);
    }
}
