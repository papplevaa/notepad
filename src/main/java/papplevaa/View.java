package papplevaa;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class View {
    private CallbackHandler callback;
    private JFrame frame;
    private JTabbedPane tabbedPane;

    // returns null if tabbedPane has no panes
    public JTextArea getSelectedTextArea() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if(scrollPane == null)
            return null;
        return (JTextArea) scrollPane.getViewport().getView();
    }
    
    public void registerCallback(CallbackHandler callback) {
        this.callback = callback;
    }

    public void activeTabUpdated(int activeTab) {
        this.tabbedPane.setSelectedIndex(activeTab);
    }

    public void tabAdded(String name, String content) {
        JTextArea textArea = setupCustomizedJTextArea(content);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.tabbedPane.add(name, scrollPane);

        // This makes it, so you can immediately write in the text area
        textArea.requestFocus();
    }

    public void tabRemoved(int tabIndex) {
        this.tabbedPane.remove(tabIndex);
    }

    public void updateUIManager(boolean darkMode) {
        try {
            if(darkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch(Exception ex) {
            System.out.println("Failed to initialize LaF");
        }
        SwingUtilities.updateComponentTreeUI(this.frame);
    }

    private void fireContentChanged() {
        JTextArea textArea = this.getSelectedTextArea();
        if(textArea != null) {
            this.callback.updateContent(textArea.getText());
        }
    }

    public void closeFrame() {
        this.frame.dispose();
    }








    public void initialize(Model model) {
        /* ------ Frame ------ */
        this.frame = new JFrame("Notepad");
        // Set custom close operation here
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                callback.closeWindow();
            }
        });
        this.frame.setSize(new Dimension(model.getWindowWidth(), model.getWindowHeight()));
        this.frame.setMinimumSize(new Dimension(model.getMinimumWindowWidth(), model.getMinimumWindowHeight()));

        /* ------ Menubar ------ */
        this.initMenu();

        /* ------ Tabbed Pane ------ */
        this.tabbedPane = new JTabbedPane();
        this.frame.add(tabbedPane);

        /* ------ Init LaF ------ */
        this.updateUIManager(model.isDarkMode());

        /* Set frame visible */
        this.frame.setVisible(true);
    }

    public void initMenu() {
        /* ------ Menubar ------ */
        JMenuBar menuBar = new JMenuBar();
        this.frame.setJMenuBar(menuBar);

        /* --- File Menu --- */
        JMenu menu = new JMenu("File");
        // Add ALT + F as accelerator?
        menuBar.add(menu);

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
        menuBar.add(menu);

        // Undo menu item
        menuItem = new JMenuItem("Undo");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK
        ));
        menuItem.addActionListener(event -> callback.undo());
        menu.add(menuItem);

        // Redo menu item
        menuItem = new JMenuItem("Redo");
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

        /* ------ Theme Button ------ */
        JButton button = new JButton("Change Theme");
        button.addActionListener(event -> callback.invertTheme());
        menuBar.add(button);
    }

    private JTextArea setupCustomizedJTextArea(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fireContentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireContentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text documents do not fire these kind of events.
            }
        });

        textArea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        callback.copy();
                    }
                }
        );

        textArea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        callback.cut();
                    }
                }
        );

        textArea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        callback.paste();
                    }
                }
        );

        return textArea;
    }
}
