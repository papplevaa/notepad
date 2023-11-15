package papplevaa;

import java.util.ArrayList;
import java.util.List;

public class Model {
    // Make view transient so it is not serialized?
    private View view;
    private final List<Tab> tabs;
    private int activeTab;
    private boolean darkMode;
    private int height;
    private int width;

    public Model(View view) {
        this.view = view;
        this.tabs = new ArrayList<>();
        this.activeTab = -1;
        this.darkMode = true;
        this.height = 320;
        this.width = 240;
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
    }

    public int getWindowWidth() {
        return this.width;
    }

    public void setWindowWidth(int width) {
        this.width = width;
    }

    public int getWindowHeight() {
        return this.height;
    }

    public void setWindowHeight(int height) {
        this.height = height;
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
    }

    public void removeTab(int tabIndex) {
        tabs.remove(tabIndex);
        if(activeTab == tabIndex) {
            if(tabs.isEmpty()) activeTab = -1;
            else activeTab = (tabIndex - 1) % tabs.size();
        }
    }
}
