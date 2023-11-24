package papplevaa.notepad;

import papplevaa.notepad.controller.Controller;
import papplevaa.notepad.model.Model;
import papplevaa.notepad.ui.View;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);
        controller.loadModel();
        controller.start();
    }
}
