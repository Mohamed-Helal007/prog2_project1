package person2;
import core.MarsColony;
import core.Tickable;
import emergencies.EmergencyType;
import facilities.Location;
import missions.Mission;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public abstract class CrewMember implements Agent, Tickable {
    public static final int MAX_HEALTH = 100;
    public static final int MAX_FATIGUE = 100;
    public static final int CRITICAL_HEALTH = 20;
    public static final int MAX_FATIGUE_FOR_MISSION = 80;
    public static final int FATIGUE_WORKING = 5;
    public static final int FATIGUE_RECOVERY = 3;
    public static final int DAMAGE_AMOUNT = 25;

    private final String id;
    private final String name;
    private int health = MAX_HEALTH;
    private int fatigue = 0;
    private Location location;
    private boolean available = true;
    private final Set<String> skills;
    private final Set<Capability> capabilities;

    protected CrewMember(String id, String name, Location Location, Collection<String> skills, Set<Capability> capabilities) {
        this.id = id;
        this.name = name;
        this.location = Location;
        this.skills = new HashSet<>(skills);
        this.capabilities = capabilities.isEmpty() ? EnumSet.noneOf(Capability.class) : EnumSet.copyOf(capabilities);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getFatigue() {
        return fatigue;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean hasSkill(String skill) {
        return skills.contains(skill);
    }

    public Set<Capability> getCapabilities() {
        return Collections.unmodifiableSet(capabilities);
    }

    public boolean isOperational() {
        return health > 0 && fatigue < MAX_FATIGUE;
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
        if (!isAvailable() || fatigue >= MAX_FATIGUE_FOR_MISSION || health <= CRITICAL_HEALTH)
            return false;
        String skill = mission.getRequiredSkill();
        if (skill != null && !hasSkill(skill))
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
        if (available)
            fatigue = Math.max(0, fatigue - FATIGUE_RECOVERY);
        else
            fatigue = Math.min(MAX_FATIGUE, fatigue + FATIGUE_WORKING);
    }

    public void treat(int amount) {
        if (amount > 0)
            health = Math.min(MAX_HEALTH, health + amount);
    }

    public void damage() {
        health = Math.max(0, health - DAMAGE_AMOUNT);
    }

    public void repair() {
        health = MAX_HEALTH;
    }

    public String toString() {
        return name + "[" + id + "], health = " + health + ", fatigue = " + fatigue + ", available = " + isAvailable() + " at " + location.getName();
    }

    public abstract void performDuty();
}