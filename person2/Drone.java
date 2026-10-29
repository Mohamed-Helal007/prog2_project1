package person2;
import person1.*;
import person3.facilities.Location;
import java.util.EnumSet;

public class Drone extends Robot {
    private final Environment environment;

    public Drone(String id, String name, Location location, Environment environment) {
        this(id, name, location, environment, MAX_BATTERY);
    }
 
    public Drone(String id, String name, Location location, Environment environment, int initialBattery) {
        super(id, name, location, initialBattery, EnumSet.of(Capability.AERIAL_RECON));
        this.environment = environment;
    }

    public boolean canPerform(Mission mission) {
        if (environment.isCommsDisrupted()) return false;
        return super.canPerform(mission);
    }

    public void performTask() {
        System.out.println(getName() + " (Drone) is performing aerial reconnaissance.");
    }
}