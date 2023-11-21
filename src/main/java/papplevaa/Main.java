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
// - if a text file is selected but the file chooser frame is closed the file gets selected anyway
