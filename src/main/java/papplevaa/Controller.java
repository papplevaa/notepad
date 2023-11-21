package papplevaa;

import java.io.File;

public class Controller implements CallbackHandler {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.view.registerCallback(this);
        this.model = model;
        this.view.initialize(model);
    }

    @Override
    public void newTab() {
        Tab newTab = new Tab();
        int idx = this.model.addTab(newTab);
        this.view.addTab(newTab.getName(), newTab.getCurrentContent());
        this.model.setSelectedIndex(idx);
        this.view.changeSelectedTab(idx);
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
        if(!this.model.isSelected()) {
            return;
        }
        int idx = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(idx);
        if(!selectedTab.getCurrentContent().equals(selectedTab.getLastSavedContent())) {
            int result = this.view.showDialog();
            if(result == 0) {
                this.save();
                this.model.removeTab(idx);
                this.view.removeTab(idx);
            } else if (result == 1) {
                this.model.removeTab(idx);
                this.view.removeTab(idx);
            }
        } else {
            this.model.removeTab(idx);
            this.view.removeTab(idx);
        }
        System.out.println("Close Tab");
    }

    @Override
    public void open() {
        File filePath = this.view.chooseFile(false);
        String name = FileUtil.getNameFromPath(filePath);
        String lastSavedContent = FileUtil.loadContent(filePath);
        this.model.addTab(new Tab(name, lastSavedContent, filePath));
        System.out.println("Open");
    }

    @Override
    public void save() {
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        Tab selectedTab = this.model.getTabAt(this.model.getSelectedIndex());
        if(selectedTab.getFilePath() == null) {
            this.saveAs();
        } else {
            String currentContent = selectedTab.getCurrentContent();
            File filePath = selectedTab.getFilePath();
            FileUtil.saveContent(currentContent, filePath);
            selectedTab.commitChanges();
        }
        System.out.println("Save");
    }

    @Override
    public void saveAs() {
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        Tab selectedTab = this.model.getTabAt(this.model.getSelectedIndex());
        File filePath = this.view.chooseFile(true);
        String name = FileUtil.getNameFromPath(filePath);
        String currentContent = selectedTab.getCurrentContent();
        FileUtil.saveContent(currentContent, filePath);
        selectedTab.setName(name);
        this.view.updateName(name);
        selectedTab.setFilePath(filePath);
        selectedTab.commitChanges();
        System.out.println("Save as");
    }

    @Override
    public void undo() {
        System.out.println("Undo");
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.undo();
        }
    }

    @Override
    public void redo() {
        System.out.println("Redo");
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.redo();
        }
    }

    @Override
    public void copy() {
        System.out.println("Copy");
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.copy();
        }
    }

    @Override
    public void cut() {
        System.out.println("Cut");
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.cut();
        }
    }

    @Override
    public void paste() {
        System.out.println("Paste");
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.paste();
        }
    }

    @Override
    public void closeWindow() {
        // Ask model if there are modified tabs
        // if true, show are you sure? dialog
        // else close window
        if(false) {
            //this.view.showDialog();
            System.out.println("Show dialog");
        } else {
            // Get window size from view
            // Set window size in model
            // Serialize model for next startup
            this.view.closeFrame();
            System.out.println("Close frame");
        }
    }

    @Override
    public void invertTheme() {
        this.model.setDarkMode(!this.model.isDarkMode());
        this.view.setDarkMode(!this.model.isDarkMode());
        System.out.println("Theme changed");
    }

    @Override
    public void updateContent(String newContent) {
        this.model.getTabAt(this.model.getSelectedIndex()).setCurrentContent(newContent);
        System.out.println("Content updated");
    }

    @Override
    public void changeTab(int selectedIndex) {
        if(this.model.getSelectedIndex() != selectedIndex) {
            this.model.setSelectedIndex(selectedIndex);
        }
        System.out.println("Changed tab");
    }
}
