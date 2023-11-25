package papplevaa.notepad.util;

public enum ConfirmDialogOptions {
    SAVE(0),
    NO_ACTION(1),
    CANCEL(2);

    private final int value;

    ConfirmDialogOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ConfirmDialogOptions getByValue(int value) {
        for(ConfirmDialogOptions option : ConfirmDialogOptions.values()) {
            if(option.getValue() == value)  {
                return option;
            }
        }
        return NO_ACTION;
    }
}
