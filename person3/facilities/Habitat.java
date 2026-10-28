package person3.facilities;

public class Habitat extends Facility {
    private final LifeSupportSystem support;
    private int oxygenLevel;

    public Habitat(String name, Location location, int capacity, int oxygenLevel) {
        super(name,capacity,location);
        this.support = new LifeSupportSystem();
        this.oxygenLevel = oxygenLevel;
    }

    public LifeSupportSystem getSupport() {
        return support;
    }

    public int getOxygenLevel() {
        return oxygenLevel;
    }

    public boolean loseOxygen(int used) {
        if(oxygenLevel >= used) {
            oxygenLevel -= used;
            return true;
        } else {
            oxygenLevel = 0;
            return false;
        }
    }
}
