package papplevaa.notepad.ui;

import papplevaa.notepad.model.*;
import papplevaa.notepad.controller.*;
import papplevaa.notepad.util.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.function.IntConsumer;

public class View {
    private CallbackHandler callback;
    private JFrame frame;
    private JTabbedPane tabbedPane;

    /* Methods for initialization */
    public View() {
        this.frame = new JFrame("Notepad");
        this.tabbedPane = new JTabbedPane();
    }

    public void registerCallback(CallbackHandler callback) {
        this.callback = callback;
    }

    public void initialize(Model model) {
        this.initFrame(model);
        this.initMenu();
        this.initTabbedPane(model);
        this.setDarkMode(model.isDarkMode());
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            this.frame.setVisible(true);
            UndoableTextArea selectedTextArea = this.getSelectedTextArea();
            if(selectedTextArea != null) {
                selectedTextArea.requestFocus();
            }
        });
    }

    /* Method to reach the selected text area */
    public UndoableTextArea getSelectedTextArea() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if(scrollPane == null)
            return null;
        return (UndoableTextArea) scrollPane.getViewport().getView();
    }

    /* Methods for updates */
    public void updateTitleAt(int index, String title, boolean starred) {
        if(starred) {
            this.tabbedPane.setTitleAt(index, "*" + title);
        } else {
            this.tabbedPane.setTitleAt(index, title);
        }
    }

    public void changeSelectedTab(int activeTab) {
        this.tabbedPane.setSelectedIndex(activeTab);
        this.getSelectedTextArea().requestFocus();
    }

    public void addTab(String name, String content) {
        UndoableTextArea textArea = new UndoableTextArea(content);
        this.setupCustomizedTextArea(textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.tabbedPane.add(name, scrollPane);
    }

    public void removeTab(int tabIndex) {
        this.tabbedPane.remove(tabIndex);
    }

    public void setDarkMode(boolean darkMode) {
        try {
            if(darkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch(UnsupportedLookAndFeelException exception) {
            System.out.println("Failed to load custom look and feel!");
        }
        SwingUtilities.updateComponentTreeUI(this.frame);
    }

    public void closeFrame() {
        this.frame.dispose();
    }

    /* Methods for showing dialogs */
    public File chooseFile(ChooseFileDialogType dialogType) {
        File selectedFile = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result =
            (dialogType == ChooseFileDialogType.SAVE)
                ? fileChooser.showSaveDialog(this.frame)
                : fileChooser.showOpenDialog(this.frame);
        if(result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        return selectedFile;
    }

    public ConfirmDialogOptions showConfirmDialog() {
        int result = JOptionPane.showConfirmDialog(this.frame, "Do you want to save changes?", "Notepad", JOptionPane.YES_NO_CANCEL_OPTION);
        return ConfirmDialogOptions.getByValue(result);
    }

    /* ---------------------- *
     * INITIALIZATION METHODS *
     * ---------------------- */
    private void initFrame(Model model) {
        // Set custom close operation here
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                callback.close();
            }
        });
        // Set window size here
        this.frame.setSize(new Dimension(model.getWindowWidth(), model.getWindowHeight()));
        this.frame.setMinimumSize(new Dimension(model.getMinimumWindowWidth(), model.getMinimumWindowHeight()));
        // Add to listener for window size
        this.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                callback.updateFrameSize(frame.getWidth(), frame.getHeight());
            }
        });
    }

    public void initMenu() {
        /* ------ Menubar ------ */
        JMenuBar menuBar = new JMenuBar();
        this.frame.setJMenuBar(menuBar);

        /* --- File Menu --- */
        JMenu menu = new JMenu("File");
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

    private void setupCustomizedTextArea(UndoableTextArea textArea) {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                callback.updateContent(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                callback.updateContent(textArea.getText());
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
    }

    private void initTabbedPane(Model model) {
        this.tabbedPane.putClientProperty("JTabbedPane.tabClosable", true);
        this.tabbedPane.putClientProperty( "JTabbedPane.tabCloseCallback", (IntConsumer) tabIndex -> callback.closeTab());
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.createTabsFromModel(model);
        this.tabbedPane.setSelectedIndex(model.getSelectedIndex());
        this.tabbedPane.addChangeListener(event -> callback.updateSelectedTab(this.tabbedPane.getSelectedIndex()));
        this.frame.add(tabbedPane);
    }

    private void createTabsFromModel(Model model) {
        int numberOfTabs = model.getNumberOfTabs();
        for(int index = 0; index < numberOfTabs; index++) {
            Tab tabAtIndex = model.getTabAt(index);
            UndoableTextArea textArea = new UndoableTextArea(tabAtIndex.getCurrentContent());
            JScrollPane scrollPane = new JScrollPane(textArea);
            // The documentListener should be added after creating the text area
            // Else its content will be instantly changed to the already opened tab
            this.setupCustomizedTextArea(textArea);
            this.tabbedPane.add(tabAtIndex.getTitle(), scrollPane);
            this.updateTitleAt(index, tabAtIndex.getTitle(), tabAtIndex.isUnsaved());
        }
    }
}
