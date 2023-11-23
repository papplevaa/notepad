package papplevaa.notepad.controller;

import papplevaa.notepad.ui.*;
import papplevaa.notepad.model.*;
import papplevaa.notepad.util.*;

import java.io.*;

public class Controller implements CallbackHandler {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.view.registerCallback(this);
        if(!this.deserializeModel()) {
            this.model = model;
        }
        this.view.initialize(this.model);
        System.out.println("Initialize app");
    }

    @Override
    public void newTab() {
        // Create new empty tab
        Tab newTab = new Tab();
        // Add tab to model and view
        int idx = this.model.addTab(newTab);
        this.view.addTab(newTab.getName(), newTab.getCurrentContent());
        // Set new tab as selected in both model and view
        this.model.setSelectedIndex(idx);
        this.view.changeSelectedTab(idx);
        // Log
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
        // Get selected tab
        if(!this.model.isSelected()) {
            return;
        }
        int idx = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(idx);
        // Check for unsaved changes
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
        // Get path to open
        File filePath = this.view.chooseFile(false);
        if(filePath == null) {
            System.out.println("No file chosen!");
            return;
        }
        // Create tab
        String name = filePath.getName();
        String lastSavedContent = FileUtil.loadContent(filePath);
        Tab openedTab = new Tab(name, lastSavedContent, filePath);
        // Add tab to the model and view
        int idx = this.model.addTab(openedTab);
        this.view.addTab(openedTab.getName(), openedTab.getCurrentContent());
        this.model.setSelectedIndex(idx);
        this.view.changeSelectedTab(idx);
        // Log
        System.out.println("Open");
    }

    @Override
    public void save() {
        // Get selected tab
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        Tab selectedTab = this.model.getTabAt(this.model.getSelectedIndex());
        // If tab was never saved, call saveAs method
        // Else save to path already associated with the tab
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
        // Get selected tab
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        Tab selectedTab = this.model.getTabAt(this.model.getSelectedIndex());
        // Choose save path
        File filePath = this.view.chooseFile(true);
        if(filePath == null) {
            System.out.println("No file chosen!");
            return;
        }
        // Save file to chosen path
        String currentContent = selectedTab.getCurrentContent();
        FileUtil.saveContent(currentContent, filePath);
        // Update model and view
        String name = filePath.getName();
        selectedTab.setName(name);
        selectedTab.setFilePath(filePath);
        selectedTab.commitChanges();
        this.view.updateName(name);
        // Log
        System.out.println("Save as");
    }

    @Override
    public void undo() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        try {
            if(textArea != null) {
                textArea.undo();
                System.out.println("Undo happened");
            }
        } catch(RuntimeException exception) {
            System.out.println("Can not undo!");
        }
    }

    @Override
    public void redo() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        try {
            if(textArea != null) {
                textArea.redo();
                System.out.println("Redo happened");
            }
        } catch(RuntimeException exception) {
            System.out.println("Can not redo!");
        }
    }

    @Override
    public void copy() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.copy();
            System.out.println("Copy");
        }
    }

    @Override
    public void cut() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.cut();
            System.out.println("Cut");
        }
    }

    @Override
    public void paste() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.paste();
            System.out.println("Paste");
        }
    }

    @Override
    public void closeWindow() {
        this.serializeModel();
        this.view.closeFrame();
        System.out.println("Close frame");
    }

    @Override
    public void invertTheme() {
        boolean isDarkMode = this.model.isDarkMode();
        this.model.setDarkMode(!isDarkMode);
        this.view.setDarkMode(!isDarkMode);
        System.out.println("Theme changed");
    }

    @Override
    public void updateContent(String newContent) {
        this.model.getTabAt(this.model.getSelectedIndex()).setCurrentContent(newContent);
        //System.out.println("Content updated");
    }

    @Override
    public void updateFrameSize(int width, int height) {
        this.model.setWindowWidth(width);
        this.model.setWindowHeight(height);
        //System.out.println("Frame resized");
    }

    @Override
    public void changeTab(int selectedIndex) {
        if(this.model.getSelectedIndex() != selectedIndex) {
            this.model.setSelectedIndex(selectedIndex);
            System.out.println("Changed tab");
        }
    }

    private void serializeModel() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(Model.getDataPath()))) {
            stream.writeObject(this.model);
        } catch (IOException exception) {
            System.out.println("Failed to save data for next session");
        }
    }

    private boolean deserializeModel() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(Model.getDataPath()))) {
            this.model = (Model) stream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Failed to load data from previous session");
            return false;
        }
        return true;
    }
}
