package person2;
import emergencies.EmergencyType;
import facilities.Location;
import missions.Mission;
import java.util.Set;

public interface Agent {
    String getId();
    String getName();

    Location getLocation();
    void setLocation(Location location);

    boolean isAvailable();
    void setAvailable(boolean available);
    boolean isOperational();
    boolean canPerform(Mission mission);
    boolean canHandle(EmergencyType type);

    void damage();
    void repair();

    Set<Capability> getCapabilities();
    void performDuty();
}