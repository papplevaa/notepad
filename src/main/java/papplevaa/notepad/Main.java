package papplevaa.notepad;

import papplevaa.notepad.controller.Controller;
import papplevaa.notepad.model.Model;
import papplevaa.notepad.ui.View;

/**
 * The entry point for the Notepad application. It initializes the Model, View, and Controller components,
 * loads the saved model data, and starts the application.
 */
public class Main {
    /**
     * The main method that serves as the entry point for the Notepad application.
     * Initializes the Model, View, and Controller components, loads the saved model data,
     * and starts the application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);
        controller.loadModel();
        controller.start();
    }
}
