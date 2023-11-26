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

/**
 * The graphical user interface class for a notepad application.
 * Manages the main frame, tabbed panes, menus, and their interactions.
 */
public class View {
    /** The callback handler for communication with the controller. */
    private CallbackHandler callback;
    /** The main frame of the Notepad application. */
    private JFrame frame;
    /** The tabbed pane containing individual tabs for text file. */
    private JTabbedPane tabbedPane;

    /**
     * Constructs a new View, initializing the main frame and tabbed pane.
     */
    public View() {
        this.frame = new JFrame("Notepad");
        this.tabbedPane = new JTabbedPane();
    }

    /**
     * Registers the callback handler for communication with the controller.
     *
     * @param callback The callback handler to register.
     */
    public void registerCallback(CallbackHandler callback) {
        this.callback = callback;
    }

    /**
     * Initializes the view with the provided model, setting up the main frame,
     * menus, and the tabbed pane with tabs from the model.
     *
     * @param model The model containing data to initialize the view.
     */
    public void initialize(Model model) {
        this.initFrame(model);
        this.initMenu();
        this.initTabbedPane(model);
        this.setDarkMode(model.isDarkMode());
    }

    /**
     * Runs the view on the event dispatch thread, making the frame visible and focusing on the selected text area.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            this.frame.setVisible(true);
            UndoableTextArea selectedTextArea = this.getSelectedTextArea();
            if(selectedTextArea != null) {
                selectedTextArea.requestFocus();
            }
        });
    }

    /**
     * Retrieves the selected text area from the currently active tab.
     *
     * @return The selected text area or null if no tab is selected.
     */
    public UndoableTextArea getSelectedTextArea() {
        UndoableTextArea result = null;
        JScrollPane scrollPane = (JScrollPane) this.tabbedPane.getSelectedComponent();
        if (scrollPane != null) {
            result = (UndoableTextArea) scrollPane.getViewport().getView();
        }
        return result;
    }

    /**
     * Updates the title of the tab at the specified index, adding a star if unsaved.
     *
     * @param index   The index of the tab.
     * @param title   The new title for the tab.
     * @param starred True if the tab is starred, false otherwise.
     */
    public void updateTitleAt(int index, String title, boolean starred) {
        if(starred) {
            this.tabbedPane.setTitleAt(index, "*" + title);
        } else {
            this.tabbedPane.setTitleAt(index, title);
        }
    }

    /**
     * Changes the selected tab to the one at the specified index and focuses on its text area.
     *
     * @param activeTab The index of the tab to be selected.
     */
    public void changeSelectedTab(int activeTab) {
        this.tabbedPane.setSelectedIndex(activeTab);
        this.getSelectedTextArea().requestFocus();
    }

    /**
     * Adds a new tab with the given name and content to the tabbed pane.
     *
     * @param name    The name of the new tab.
     * @param content The initial current content of the new tab.
     */
    public void addTab(String name, String content) {
        UndoableTextArea textArea = new UndoableTextArea(content);
        this.setupCustomizedTextArea(textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.tabbedPane.add(name, scrollPane);
    }

    /**
     * Removes the tab at the specified index from the tabbed pane.
     *
     * @param tabIndex The index of the tab to be removed.
     */
    public void removeTab(int tabIndex) {
        this.tabbedPane.remove(tabIndex);
    }

    /**
     * Sets the dark mode for the entire application, updating the look and feel accordingly.
     *
     * @param darkMode True to enable dark mode, false to disable.
     */
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

    /**
     * Closes the main frame of the application.
     */
    public void closeFrame() {
        this.frame.dispose();
    }

    /**
     * Opens a file chooser dialog for choosing a file based on the specified dialog type.
     *
     * @param dialogType The type of the file chooser dialog (SAVE or OPEN).
     * @return The selected file or null if the dialog is canceled.
     */
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

    /**
     * Displays a confirmation dialog asking the user if they want to save changes.
     *
     * @return The user's choice from the confirmation dialog.
     */
    public ConfirmDialogOptions showConfirmDialog() {
        int result = JOptionPane.showConfirmDialog(this.frame, "Do you want to save changes?", "Notepad", JOptionPane.YES_NO_CANCEL_OPTION);
        return ConfirmDialogOptions.getByValue(result);
    }

    /* ---------------------- *
     * INITIALIZATION METHODS *
     * ---------------------- */

    /**
     * Initializes the main frame with custom close operation, size, and adds a component listener for resizing.
     *
     * @param model The model containing data to set the initial frame size.
     */
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

    /**
     * Initializes the menu bar, creating the File and Edit menus, adding menu items and a button for changing the theme.
     */
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

    /**
     * Sets up the document listener and key bindings for a customized text area.
     *
     * @param textArea The text area to set up.
     */
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

    /**
     * Initializes the tabbed pane with custom properties and creates tabs based on the model.
     *
     * @param model The model containing data to create tabs.
     */
    private void initTabbedPane(Model model) {
        this.tabbedPane.putClientProperty("JTabbedPane.tabClosable", true);
        this.tabbedPane.putClientProperty( "JTabbedPane.tabCloseCallback", (IntConsumer) tabIndex -> callback.closeTab());
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.createTabsFromModel(model);
        this.tabbedPane.setSelectedIndex(model.getSelectedIndex());
        this.tabbedPane.addChangeListener(event -> callback.updateSelectedTab(this.tabbedPane.getSelectedIndex()));
        this.frame.add(tabbedPane);
    }

    /**
     * Creates tabs in the tabbed pane based on the tabs in the provided model.
     *
     * @param model The model containing data to create tabs.
     */
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
