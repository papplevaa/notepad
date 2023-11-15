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
}
