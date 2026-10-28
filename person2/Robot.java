package person2;
import core.MarsColony;
import core.Tickable;
import emergencies.EmergencyType;
import facilities.Location;
import missions.Mission;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Robot implements Agent, Tickable {
    public static final int MAX_BATTERY = 100;
    public static final int DRAIN_WORKING = 5;
    public static final int DRAIN_IDLE = 1;
    
    private final String id;
    private final String name;
    private int battery;
    private boolean damaged = false;
    private Location location;
    private boolean available = true;
    private final Set<Capability> capabilities;

    protected Robot(String id, String name, Location location, int initialBattery, Set<Capability> capabilities) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.battery = Math.max(0, Math.min(MAX_BATTERY, initialBattery));
        this.capabilities = EnumSet.copyOf(capabilities);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBattery() {
        return battery;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Capability> getCapabilities() {
        return Collections.unmodifiableSet(capabilities);
    }

    public boolean isOperational() {
        return !damaged && battery > 0;
    }

    public boolean isAvailable() {
        return available && isOperational();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean canHandle(EmergencyType type) {
        return isAvailable() && capabilities.contains(Capability.requiredFor(type));
    }

    public boolean canPerform(Mission mission) {
        if (!isAvailable() || battery < mission.getBatteryNeeded())
            return false;
        return contributesTo(mission);
    }

    private boolean contributesTo(Mission mission) {
        Set<Capability> required = mission.getRequiredCapabilities();
        if (required.isEmpty())
            return true;
        for (Capability c : required) {
            if (capabilities.contains(c))
                return true;
        }
        return false;
    }

    public void onTick(MarsColony colony) {
        drainBattery(available ? DRAIN_IDLE : DRAIN_WORKING);
    }
 
    public void drainBattery(int amount) {
        if (amount > 0) battery = Math.max(0, battery - amount);
    }
 
    public void recharge(int amount) {
        if (amount > 0) battery = Math.min(MAX_BATTERY, battery + amount);
    }

    public void damage() {
        damaged = true;
    }

    public void repair() {
        damaged = false;
    }

    public String toString() {
        return name + "[" + id + "], battery = " + battery + ", damaged = " + damaged + ", available = " + isAvailable() + " at " + location.getName();
    }

    public abstract void performTask();
    
    public void performDuty() {
        performTask();
    }
}