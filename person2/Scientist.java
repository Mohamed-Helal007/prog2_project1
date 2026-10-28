package person2;
import person3.facilities.Location;
import java.util.Collection;
import java.util.EnumSet;

public class Scientist extends CrewMember {
    public Scientist(String id, String name, Location location, Collection<String> skills) {
        super(id, name, location, skills, EnumSet.of(Capability.RESEARCH));
    }

    public void performDuty() {
        System.out.println(getName() + " (Scientist) is conducting research.");
    }
}