package papplevaa;

public class Controller implements CallbackHandler {
    private View view;
    private Model model;

    public Controller() {
        this.view = new View();
        this.view.registerCallback(this);
        this.model = new Model(view);

        // testing
        this.view.testHelloWorld();
    }

    @Override
    public void helloWorld() {
        System.out.println("Hello, World!");
    }

    @Override
    public void newTab() {
        System.out.println("New tab");
    }

    @Override
    public void closeTab() {
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
        System.out.println("Copy");
    }

    @Override
    public void cut() {
        System.out.println("Cut");
    }

    @Override
    public void paste() {
        System.out.println("Paste");
    }

    @Override
    public void closeWindow() {
        // Ask model if there are modified tabs
        // if true, show are you sure? dialog
        // else close window
        if(false) {
            System.out.println("Show dialog");
            //this.view.showDialog();
        } else {
            System.out.println("Close frame");
            this.view.closeFrame();
        }
    }
}
