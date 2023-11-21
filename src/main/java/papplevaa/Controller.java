package papplevaa;

import java.io.*;

public class Controller implements CallbackHandler {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.view.registerCallback(this);
        try {
            this.deserializeModel();
        } catch (Exception exception) {
            this.model = model;
            exception.printStackTrace();
            System.out.println("Could not load data from previous session");
        }
        this.view.initialize(this.model);
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
        if(textArea != null) {
            textArea.undo();
            System.out.println("Undo");
        }
    }

    @Override
    public void redo() {
        // Tell the selected text area to make the change
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.redo();
            System.out.println("Redo");
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
            try {
                this.serializeModel();
            } catch(IOException exception) {
                System.out.println("Could not save data for next session");
            }
            this.view.closeFrame();
            System.out.println("Close frame");
        }
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
    public void changeTab(int selectedIndex) {
        if(this.model.getSelectedIndex() != selectedIndex) {
            this.model.setSelectedIndex(selectedIndex);
            System.out.println("Changed tab");
        }
    }

    private void serializeModel() throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(Model.getDataPath()));
        stream.writeObject(this.model);
        stream.close();
    }

    private void deserializeModel() throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(Model.getDataPath()));
        this.model = (Model) stream.readObject();
        stream.close();
    }
}
