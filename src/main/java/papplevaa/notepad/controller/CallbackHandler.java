package papplevaa.notepad.controller;

/**
 * The CallbackHandler interface defines methods that the controller uses to handle user actions and updates from the View.
 * Implementing classes, typically the controller, should provide concrete implementations for each method to respond
 * to specific user interactions.
 */
public interface CallbackHandler {
    /**
     * Signals the creation of a new tab in the application.
     */
    void newTab();

    /**
     * Signals the request to close the currently active tab in the application.
     */
    void closeTab();

    /**
     * Signals the request to open a file and create a new tab for its content in the application.
     */
    void open();

    /**
     * Signals the request to save the content of the currently active tab.
     */
    void save();

    /**
     * Signals the request to save the content of the currently active tab with a new file name or location.
     */
    void saveAs();

    /**
     * Signals the request to undo the last user action in the currently active tab.
     */
    void undo();

    /**
     * Signals the request to redo the previously undone user action in the currently active tab.
     */
    void redo();

    /**
     * Signals the request to copy the selected content in the currently active tab.
     */
    void copy();

    /**
     * Signals the request to cut the selected content in the currently active tab.
     */
    void cut();

    /**
     * Signals the request to paste the copied or cut content in the currently active tab.
     */
    void paste();

    /**
     * Signals the request to close the Notepad application.
     */
    void close();

    /**
     * Signals the request to invert the theme in the application.
     */
    void invertTheme();

    /**
     * Signals the update of the selected tab in the application.
     *
     * @param selectedIndex The index of the newly selected tab.
     */
    void updateSelectedTab(int selectedIndex);

    /**
     * Signals the update of the content in the currently active tab with new content.
     *
     * @param newContent The new content to update in the active tab.
     */
    void updateContent(String newContent);

    /**
     * Signals the update of the size of the application window.
     *
     * @param width  The new width of the application window.
     * @param height The new height of the application window.
     */
    void updateFrameSize(int width, int height);
}
