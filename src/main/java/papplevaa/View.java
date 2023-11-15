package papplevaa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View {
    private CallbackHandler callback;
    private final JFrame frame;
    private final JMenuBar menuBar;

    public View() {
        /* ------ Frame ------ */
        this.frame = new JFrame("Notepad");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.frame.setMinimumSize(new Dimension(320, 240));
        this.frame.setSize(new Dimension(320, 240));

        /* ------ Menubar ------ */
        this.menuBar = new JMenuBar();

        /* --- File Menu --- */
        JMenu menu = new JMenu("File");
        // Add ALT + F as accelerator?
        this.menuBar.add(menu);

        // New Tab menu item
        JMenuItem menuItem = new JMenuItem("New Tab");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.newTab());
        menu.add(menuItem);

        // Close Tab menu item
        menuItem = new JMenuItem("Close Tab");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.closeTab());
        menu.add(menuItem);

        // Open menu item
        menuItem = new JMenuItem("Open");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.open());
        menu.add(menuItem);

        // Save menu item
        menuItem = new JMenuItem("Save");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.save());
        menu.add(menuItem);

        // Save As menu item
        menuItem = new JMenuItem("Save as");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.saveAs());
        menu.add(menuItem);

        /* --- Edit Menu --- */
        menu = new JMenu("Edit");
        // Add ALT + E as accelerator?
        this.menuBar.add(menu);

        // Undo menu item
        menuItem = new JMenuItem("Undo");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.undo());
        menu.add(menuItem);

        // Redo menu item
        menuItem = new JMenuItem("Undo");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.redo());
        menu.add(menuItem);

        // Separator
        menu.addSeparator();

        // Copy menu item
        menuItem = new JMenuItem("Copy");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.copy());
        menu.add(menuItem);

        // Cut menu item
        menuItem = new JMenuItem("Cut");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.cut());
        menu.add(menuItem);

        // Paste menu item
        menuItem = new JMenuItem("Paste");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.paste());
        menu.add(menuItem);


        this.frame.setJMenuBar(menuBar);

        this.frame.setVisible(true);
    }

    // Feel free to create separate update functions for each property separately
    public void activeTabUpdated(int activeTab) {
        // For each publicly available property of the model ...
        // Call update functions, which reflect the new state in the Swing components
    }

    public void closeFrame() {
        this.frame.dispose();
    }

    public void registerCallback(CallbackHandler callback) {
        this.callback = callback;

        // THIS MUST BE MOVED
        this.frame.addWindowListener(new MyWindowAdapter(callback));
    }

    public void testHelloWorld() { // can delete later, just for testing
        this.callback.helloWorld();
    }

    private static class MyWindowAdapter extends WindowAdapter {
        private CallbackHandler callback;

        public MyWindowAdapter(CallbackHandler callback) {
            super();
            this.callback = callback;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            callback.closeWindow();
        }
    }
}
