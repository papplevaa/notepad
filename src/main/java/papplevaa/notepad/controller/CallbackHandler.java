package papplevaa.notepad.controller;


// Commands coming from View to Controller come here
public interface CallbackHandler {
    void newTab();
    void closeTab();
    void open();
    void save();
    void saveAs();
    void undo();
    void redo();
    void copy();
    void cut();
    void paste();
    void close();
    void invertTheme();
    void updateContent(String newContent);
    void updateFrameSize(int width, int height);
    void updateSelectedTab(int selectedIndex);
}
