package papplevaa;

public class Main {
    public static void main(String[] args) {
        View SwingGUI = new View();
        Model model = new Model();
        Controller controller = new Controller(SwingGUI, model);
    }
}


// TODO
// - exception thrown when copying from intellij to textarea
