package papplevaa.notepad.controller;

import papplevaa.notepad.ui.*;
import papplevaa.notepad.model.*;
import papplevaa.notepad.util.*;

import java.io.*;

/**
 * The Controller class manages the communication between the View and Model in the Notepad application.
 * It implements the CallbackHandler interface to handle user actions and updates from the View.
 */
public class Controller implements CallbackHandler {
    /** The view associated with this controller. */
    private View view;
    /** The model associated with this controller. */
    private Model model;

    /**
     * Constructs a new Controller with the specified view and model.
     *
     * @param view  The view to associate with this controller.
     * @param model The model to associate with this controller.
     */
    public Controller(View view, Model model) {
        this.view = view;
        this.view.registerCallback(this);
        this.model = model;
    }

    /**
     * Loads the model from the serialized data file and updates the model accordingly.
     * Reads contents of files saved in the model and updates tab information.
     */
    public void loadModel() {
        Model savedModel = FileUtil.deserialize(Model.getDataPath(), Model.class);
        if(savedModel != null) {
            this.model = savedModel;

            // Read contents of files saved
            int numberOfTabs = this.model.getNumberOfTabs();
            for(int index = 0; index < numberOfTabs; index++) {
                Tab tab = this.model.getTabAt(index);
                String contentSaved = null;
                File filePath = tab.getFilePath();
                if(filePath != null) {
                    contentSaved = FileUtil.loadContent(filePath);
                    // contentSaved is null if loadContent fails
                    // in this case delete the path associated with the tab
                    if(contentSaved == null) {
                        tab.setFilePath(null);
                    }
                }
                tab.setLastSavedContent(contentSaved);
            }
        }
    }

    /**
     * Starts the Notepad application by initializing the view and running it.
     */
    public void start() {
        this.view.initialize(this.model);
        this.view.run();
        System.out.println("Start the app!");
    }

    /* ------ CallbackHandler interface methods ------ */
    /**
     * {@inheritDoc}
     * Creates a new, empty tab in the Notepad application.
     * Adds the new tab to both the model, and the view.
     * Sets it as the selected tab.
     */
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

    /**
     * {@inheritDoc}
     * Closes the currently active tab in the Notepad application.
     * Checks for unsaved changes, prompts the user to save if necessary,
     * and removes the tab from both the model and view.
     */
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
                // Return if save failed
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

    /**
     * {@inheritDoc}
     * Opens a file chooser dialog to select a file, creates a new tab with the file's content, and adds it
     * to both the model and view. Sets the newly opened tab as the selected tab.
     */
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
            File pathOfTabAtIndex = this.model.getTabAt(index).getFilePath();
            if(pathOfTabAtIndex != null && pathOfTabAtIndex.equals(filePath)) {
                this.view.changeSelectedTab(index);
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

    /**
     * {@inheritDoc}
     * Saves the content of the currently active tab. If the tab has not been saved before,
     * invokes the saveAs method.
     */
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

    /**
     * {@inheritDoc}
     * Opens a file chooser dialog to choose a location to save the content of the currently active tab.
     * Updates the tab's file path, saves the content.
     */
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

    /**
     * {@inheritDoc}
     * Undoes the last user action in the currently active tab using the UndoableTextArea.
     */
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

    /**
     * {@inheritDoc}
     * Redoes the previously undone user action in the currently active tab using the UndoableTextArea.
     */
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

    /**
     * {@inheritDoc}
     * Copies the selected content in the currently active tab using the UndoableTextArea.
     */
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

    /**
     * {@inheritDoc}
     * Cuts the selected content in the currently active tab using the UndoableTextArea.
     */
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

    /**
     * {@inheritDoc}
     * Pastes the copied or cut content in the currently active tab using the UndoableTextArea.
     */
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

    /**
     * {@inheritDoc}
     * Closes the Notepad application, serializes the model, and disposes of the application frame.
     */
    @Override
    public void close() {
        FileUtil.serialize(Model.getDataPath(), this.model);
        this.view.closeFrame();
        System.out.println("Close frame");
    }

    /**
     * {@inheritDoc}
     * Inverts the theme (dark mode to light mode or vice versa) in the Notepad application.
     * Updates both the model and view to reflect the theme change.
     */
    @Override
    public void invertTheme() {
        boolean isDarkMode = this.model.isDarkMode();
        this.model.setDarkMode(!isDarkMode);
        this.view.setDarkMode(!isDarkMode);
        System.out.println("Theme changed");
    }

    /**
     * {@inheritDoc}
     * Updates the model with the newly selected tab's index.
     */
    @Override
    public void updateSelectedTab(int selectedIndex) {
        if(this.model.getSelectedIndex() != selectedIndex) {
            this.model.setSelectedIndex(selectedIndex);
            System.out.println("Changed tab");
        }
    }

    /**
     * {@inheritDoc}
     * Updates the content of the currently active tab with the provided new content in the model.
     * Updates the title in the view.
     */
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

    /**
     * {@inheritDoc}
     * Updates the size of the Notepad application window with the provided width and height.
     * Updates the model with the new window size.
     */
    @Override
    public void updateFrameSize(int width, int height) {
        this.model.setWindowWidth(width);
        this.model.setWindowHeight(height);
        //System.out.println("Frame resized: " + width + "x" + height);
    }
}
