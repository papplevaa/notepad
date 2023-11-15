package papplevaa;

public class View {
    private CallbackHandler callback;

    // Feel free to create separate update functions for each property separately
    public void activeTabUpdated(int activeTab) {
        // For each publicly available property of the model ...
        // Call update functions, which reflect the new state in the Swing components
    }

    public void registerCallback(CallbackHandler callback) {
        this.callback = callback;
    }

    public void testHelloWorld() { // can delete later, just for testing
        this.callback.helloWorld();
    }
}
