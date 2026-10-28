package person2;
import facilities.Location;
import java.util.Collection;
import java.util.EnumSet;

public class MedicalOfficer extends CrewMember {
    public static final int TREATMENT_AMOUNT = 25;

    public MedicalOfficer(String id, String name, Location location, Collection<String> skills) {
        super(id, name, location, skills, EnumSet.of(Capability.MEDICAL));
    }

    public void performDuty() {
        System.out.println(getName() + " (Medical Officer) is providing medical care.");
    }

    public boolean treatPatient(CrewMember patient) {
        if (!isOperational()) return false;
        patient.treat(TREATMENT_AMOUNT);
        System.out.println(getName() + " treated " + patient.getName() + " (health now " + patient.getHealth() + ").");
        return true;
    }
}