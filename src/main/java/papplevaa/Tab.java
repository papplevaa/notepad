package papplevaa;

import java.io.File;

public class Tab {
    // Egyelore nincs ra szukseg
    File filePath;
    private String name;
    private String lastSaved;
    private String current;

    // Uj tab megnyitasahoz kell
    public Tab() {
        this.name = "Untitled";
        this.current = "";
        this.lastSaved = null;
    }

    // Letezo text file, tab megnyitasahoz kell
    public Tab(String name, String lastSavedContent, File filePath) {
        this.name = name;
        this.filePath = filePath;
        this.lastSaved = lastSavedContent;
        this.current = this.lastSaved;
    }

    // A tabbed pane egy tabjanak peldanyisitasahoz
    public String getName() {
        return this.name;
    }

    // Uj tab nyitasanal, elso mentesnel
    public void setName(String name) {
        this.name = name;
    }

    public File getFilePath() {
        return this.filePath;
    }

    // Uj text file nyitasanal, elso mentesnel
    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    // Egyelore nincs ra szukseg
    public String getLastSavedContent() {
        return this.lastSaved;
    }

    // A view példányosításánál van szerepe
    public String getCurrentContent() {
        return this.current;
    }

    // Viewból jön majd minden, ha a JTextArea mögötti Document változik
    public void setCurrentContent(String content) {
        this.current = content;
    }

    // Mentés esetén
    public void commitChanges() {
        this.lastSaved = this.current;
    }
}
