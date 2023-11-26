package papplevaa.notepad.util;

/**
 * Enumeration representing the options available in a confirmation dialog.
 * These options include choices to save changes, not save changes, or cancel an operation.
 */
public enum ConfirmDialogOptions {
    /**
     * Option indicating that changes should be saved.
     */
    SAVE(0),
    /**
     * Option indicating that changes should not be saved.
     */
    DO_NOT_SAVE(1),
    /**
     * Option indicating that the operation should be canceled.
     */
    CANCEL(2);

    /** The integer value associated with the actual Option. */
    private final int value;

    /**
     * Constructs a ConfirmDialogOptions enum with the specified integer value.
     *
     * @param value The integer value associated with the enum option.
     */
    ConfirmDialogOptions(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value associated with the enum option.
     *
     * @return The integer value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets the ConfirmDialogOptions enum corresponding to the specified integer value.
     * If no matching option is found, the default option is {@code CANCEL}.
     *
     * @param value The integer value to match.
     * @return The ConfirmDialogOptions enum corresponding to the specified value.
     */
    public static ConfirmDialogOptions getByValue(int value) {
        for(ConfirmDialogOptions option : ConfirmDialogOptions.values()) {
            if(option.getValue() == value)  {
                return option;
            }
        }
        return CANCEL;
    }
}
