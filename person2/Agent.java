package person2;
import person3.emergencies.EmergencyType;
import person3.facilities.Location;
import person4.Mission;
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