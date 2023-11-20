package papplevaa;


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
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.undo();
        }
        System.out.println("Undo");
    }

    @Override
    public void redo() {
        UndoableTextArea textArea = this.view.getSelectedTextArea();
        if(textArea != null) {
            textArea.redo();
        }
        System.out.println("Redo");
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
        this.model.getSelectedTab().setCurrentContent(newContent);
        System.out.println("Content updated");
    }

    @Override
    public void updateSelectedIndex(int selectedIndex) {
        this.model.setSelectedIndex(selectedIndex);
        System.out.println("Changed tab");
    }
}
