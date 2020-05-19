package task1;

// STATE
public class State {
    public int transmissions, successful_transmissions;

    public void reset() {
        transmissions = 0;
        successful_transmissions = 0;
    }
}