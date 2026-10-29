package person3.emergencies;
import person1.*;
import person2.*;
import person3.facilities.*;
import java.util.Map;
import java.util.Set;

public class Medical extends Emergency{
    private final CrewMember patient;
    private final Facility facility;

    public Medical(CrewMember patient, Facility facility) {
        super(EmergencyType.MEDICAL, "Medical emergency");

        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (facility == null) {
            throw new IllegalArgumentException("Facility cannot be null");
        }

        this.patient = patient;
        this.facility = facility;
    }

    @Override
    protected Set<Capability> getRequiredCapabilities() {
        return Set.of(Capability.MEDICAL);
    }

    @Override
    protected Map<ResourceType, Integer> getResponseCost() {
        return Map.of(ResourceType.MEDICAL_SUPPLIES, 1);
    }

    @Override
    protected void onDetected(MarsColony colony) {
    }

    @Override
    protected void onResolved(MarsColony colony) {
        patient.treat(20);
    }

    @Override
    protected void onUnresolved(MarsColony colony) {
    }
    
}
