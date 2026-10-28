package person3.facilities;

public class LifeSupportSystem {
    private boolean Working;
    public LifeSupportSystem() {
        Working = true;
    }
    public boolean isWorking() {
        return Working;
    }
    public void fail() {
        Working = false;
    }
    public void restore() {
        Working = true;
    }
    
}
