package papplevaa;


import java.io.File;

import static papplevaa.FileUtil.*;

public class Controller implements CallbackHandler {
    private View view;
    private Model model;

    public Controller() {
        this.view = new View();
        this.view.registerCallback(this);
        this.model = new Model(view);
        this.view.initialize(model);
    }

    @Override
    public void newTab() {
        int idx = this.model.addTab(new Tab());
        this.model.setSelectedIndex(idx);
        this.view.changeSelectedTab(idx);
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
        int idx = this.model.getSelectedIndex();
        this.model.removeTab(idx);
        //this.view.showDialogForUnsavedChanges();
        System.out.println("Close Tab");
    }

    @Override
    public void open() {
        File filePath = this.view.chooseFile();
        String name = getNameFromPath(filePath);
        String lastSavedContent = loadContent(filePath);
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
        File filePath = this.view.chooseFile();
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
