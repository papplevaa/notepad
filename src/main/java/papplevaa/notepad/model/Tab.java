package papplevaa.notepad.model;

import java.io.File;
import java.io.Serializable;

public class Tab implements Serializable {
    private File filePath;
    private String name;
    private String lastSaved;
    private String current;

    public Tab() {
        this.filePath = null;
        this.name = "Untitled";
        this.lastSaved = "";
        this.current = this.lastSaved;
    }

    public Tab(String name, String lastSavedContent, File filePath) {
        this.filePath = filePath;
        this.name = name;
        this.lastSaved = lastSavedContent;
        this.current = this.lastSaved;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFilePath() {
        return this.filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    public String getLastSavedContent() {
        return this.lastSaved;
    }

    public String getCurrentContent() {
        return this.current;
    }

    public void setCurrentContent(String content) {
        this.current = content;
    }

    public void commitChanges() {
        this.lastSaved = this.current;
    }
}
