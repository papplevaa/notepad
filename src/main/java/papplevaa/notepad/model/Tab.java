package papplevaa.notepad.model;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a tab in a notepad application that holds information about a document.
 * Each tab can have a file path, title, last saved content, and current content.
 * Implements the Serializable interface to support serialization.
 */
public class Tab implements Serializable {
    /** The file path associated with the tab. */
    private File filePath;
    /** The title of the tab. */
    private String title;
    /** The content of the tab when it was last saved. */
    private transient String lastSaved;
    /** The current content of the tab. */
    private String current;

    /**
     * Constructs a new Tab with default values.
     * File path is set to null, title is set to "Untitled",
     * lastSaved and current content are initially empty.
     */
    public Tab() {
        this.filePath = null;
        this.title = "Untitled";
        this.lastSaved = "";
        this.current = this.lastSaved;
    }

    /**
     * Constructs a new Tab with specified values.
     *
     * @param title The title of the tab.
     * @param lastSavedContent The content of the tab when it was last saved.
     * @param filePath The file path associated with the tab.
     */
    public Tab(String title, String lastSavedContent, File filePath) {
        this.filePath = filePath;
        this.title = title;
        this.lastSaved = lastSavedContent;
        this.current = this.lastSaved;
    }

    /**
     * Gets the title of the tab.
     *
     * @return The title of the tab.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the tab.
     *
     * @param name The new title for the tab.
     */
    public void setTitle(String name) {
        this.title = name;
    }

    /**
     * Gets the file path associated with the tab.
     *
     * @return The file path of the tab.
     */
    public File getFilePath() {
        return this.filePath;
    }

    /**
     * Sets the file path associated with the tab.
     *
     * @param filePath The new file path for the tab.
     */
    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the content of the tab when it was last saved.
     *
     * @return The last saved content of the tab.
     */
    public String getLastSavedContent() {
        return this.lastSaved;
    }

    /**
     * Sets the content of the tab when it was last saved.
     *
     * @param content The new last saved content for the tab.
     */
    public void setLastSavedContent(String content) {
        this.lastSaved = Objects.requireNonNullElse(content, "");
    }

    /**
     * Gets the current content of the tab.
     *
     * @return The current content of the tab.
     */
    public String getCurrentContent() {
        return this.current;
    }

    /**
     * Sets the current content of the tab.
     *
     * @param content The new current content for the tab.
     */
    public void setCurrentContent(String content) {
        this.current = content;
    }

    /**
     * Checks if there are unsaved changes in the tab.
     *
     * @return True if there are unsaved changes, false otherwise.
     */
    public boolean isUnsaved() {
        return !this.current.equals(this.lastSaved);
    }

    /**
     * Commits the current changes to the last saved state.
     * Updates the last saved content to match the current content.
     */
    public void commitChanges() {
        this.lastSaved = this.current;
    }
}
