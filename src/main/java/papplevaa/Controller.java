package papplevaa;

import javax.swing.*;

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
        this.model.setActiveTabIndex(idx);
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
        int idx = this.model.getActiveTabIndex();
        this.model.removeTab(idx);
        //this.view.showDialogForUnsavedChanges();
        System.out.println("Close Tab");
    }

    @Override
    public void open() {
        System.out.println("Open");
    }

    @Override
    public void save() {
        System.out.println("Save");
    }

    @Override
    public void saveAs() {
        System.out.println("Save as");
    }

    @Override
    public void undo() {
        System.out.println("Undo");
    }

    @Override
    public void redo() {
        System.out.println("Redo");
    }

    @Override
    public void copy() {
        JTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.copy();
        }
        System.out.println("Copy");
    }

    @Override
    public void cut() {
        JTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.cut();
        }
        System.out.println("Cut");
    }

    @Override
    public void paste() {
        JTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.paste();
        }
        System.out.println("Paste");
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
    public void updateContent(String text) {
        this.model.getActiveTab().setCurrentContent(text);
        //System.out.println("Content updated to: " + text);
    }
}
