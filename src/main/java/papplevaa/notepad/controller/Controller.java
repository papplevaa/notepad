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
        this.model = model;
    }

    public void loadModel() {
        Model savedModel = FileUtil.deserialize(Model.getDataPath(), Model.class);
        if(savedModel != null) {
            this.model = savedModel;
        }
    }

    public void start() {
        this.view.initialize(this.model);
        this.view.run();
        System.out.println("Start the app!");
    }

    @Override
    public void newTab() {
        // Create new empty tab
        Tab newTab = new Tab();
        // Add tab to model and view
        this.model.addTab(newTab);
        int index = this.model.indexOfTab(newTab);
        this.view.addTab(newTab.getTitle(), newTab.getCurrentContent());
        // Set new tab as selected in both model and view
        this.model.setSelectedIndex(index);
        this.view.changeSelectedTab(index);
        // Log
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
        // Get selected tab
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        int index = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(index);
        // Check for unsaved changes
        if(selectedTab.isUnsaved()) {
            // Prompt to save changes
            ConfirmDialogOptions option = this.view.showConfirmDialog();
            if(option == ConfirmDialogOptions.CANCEL) {
                return;
            } else if(option == ConfirmDialogOptions.SAVE) {
                this.save();
                if(selectedTab.getFilePath() == null) {
                    return;
                }
            }
        }
        // Close tab
        this.model.removeTab(index);
        this.view.removeTab(index);
        System.out.println("Close Tab");
    }

    @Override
    public void open() {
        // Get path to open
        File filePath = this.view.chooseFile(ChooseFileDialogType.OPEN);
        if(filePath == null) {
            System.out.println("No file chosen!");
            return;
        }
        // Do not open the tab again
        int numberOfTabs = this.model.getNumberOfTabs();
        for(int index = 0; index < numberOfTabs; index++) {
            if(this.model.getTabAt(index).getFilePath().equals(filePath)) {
                System.out.println("File is already open!");
                return;
            }
        }
        // Create tab
        String name = filePath.getName();
        String lastSavedContent = FileUtil.loadContent(filePath);
        Tab openedTab = new Tab(name, lastSavedContent, filePath);
        // Add tab to the model and view
        this.model.addTab(openedTab);
        int index = this.model.indexOfTab(openedTab);
        this.view.addTab(openedTab.getTitle(), openedTab.getCurrentContent());
        // Set the opened tab as selected
        this.model.setSelectedIndex(index);
        this.view.changeSelectedTab(index);
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
        int index = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(index);
        // If tab was never saved, call saveAs method
        if(selectedTab.getFilePath() == null) {
            this.saveAs();
            return;
        }
        // Else save to path already associated with the tab
        String currentContent = selectedTab.getCurrentContent();
        File filePath = selectedTab.getFilePath();
        FileUtil.saveContent(currentContent, filePath);
        selectedTab.commitChanges();
        this.view.updateTitleAt(this.model.getSelectedIndex(), selectedTab.getTitle(), selectedTab.isUnsaved());
        System.out.println("Save");
    }

    @Override
    public void saveAs() {
        // Get selected tab
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        int index = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(index);
        // Choose save path
        File filePath = this.view.chooseFile(ChooseFileDialogType.SAVE);
        if(filePath == null) {
            System.out.println("No file chosen!");
            return;
        }
        // Save file to chosen path
        String currentContent = selectedTab.getCurrentContent();
        FileUtil.saveContent(currentContent, filePath);
        // Update model and view
        String title = filePath.getName();
        selectedTab.setTitle(title);
        selectedTab.setFilePath(filePath);
        selectedTab.commitChanges();
        this.view.updateTitleAt(this.model.getSelectedIndex(), title, selectedTab.isUnsaved());
        // Log
        System.out.println("Save as");
    }

    @Override
    public void undo() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea == null) {
            return;
        }

        try {
            textArea.undo();
            System.out.println("Undo happened");
        } catch(RuntimeException exception) {
            System.out.println("Can not undo!");
        }
    }

    @Override
    public void redo() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea == null) {
            return;
        }

        try {
            textArea.redo();
            System.out.println("Redo happened");
        } catch(RuntimeException exception) {
            System.out.println("Can not redo!");
        }
    }

    @Override
    public void copy() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea == null) {
            return;
        }

        textArea.copy();
        System.out.println("Copy");
    }

    @Override
    public void cut() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea == null) {
            return;
        }

        textArea.cut();
        System.out.println("Cut");
    }

    @Override
    public void paste() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea == null) {
            return;
        }

        textArea.paste();
        System.out.println("Paste");
    }

    @Override
    public void close() {
        FileUtil.serialize(Model.getDataPath(), this.model);
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
        // Get selected tab
        if(!this.model.isSelected()) {
            System.out.println("No tab is selected!");
            return;
        }
        int index = this.model.getSelectedIndex();
        Tab selectedTab = this.model.getTabAt(index);
        // Save new content
        selectedTab.setCurrentContent(newContent);
        // Update title in view
        this.view.updateTitleAt(index, selectedTab.getTitle(), selectedTab.isUnsaved());
        //System.out.println("Content updated");
    }

    @Override
    public void updateFrameSize(int width, int height) {
        this.model.setWindowWidth(width);
        this.model.setWindowHeight(height);
        //System.out.println("Frame resized");
    }

    @Override
    public void updateSelectedTab(int selectedIndex) {
        if(this.model.getSelectedIndex() != selectedIndex) {
            this.model.setSelectedIndex(selectedIndex);
            System.out.println("Changed tab");
        }
    }
}
