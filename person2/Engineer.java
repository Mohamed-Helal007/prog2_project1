package person2;
import facilities.Location;
import java.util.Collection;
import java.util.EnumSet;

public class Engineer extends CrewMember {
    public Engineer(String id, String name, Location location, Collection<String> skills) {
        super(id, name, location, skills, EnumSet.of(Capability.REPAIR, Capability.FIRE_RESPONSE));
    }

    public void performDuty() {
        System.out.println(getName() + " (Engineer) is repairing equipment.");
    }
}