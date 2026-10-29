package person3.emergencies;
import person1.*;
import person2.*;
import person3.facilities.*;
import java.util.Map;
import java.util.Set;

public class Fire extends Emergency {
    private final Laboratory laboratory;

    public Fire(Laboratory laboratory) {
        super(EmergencyType.FIRE, "Fire in laboratory");

        if (laboratory == null) {
            throw new IllegalArgumentException("Laboratory cannot be null");
        }

        this.laboratory = laboratory;
    }

    @Override
    protected Set<Capability> getRequiredCapabilities() {
        return Set.of(Capability.FIRE_RESPONSE);
    }

    @Override
    protected Map<ResourceType, Integer> getResponseCost() {
        return Map.of(ResourceType.OXYGEN, 1);
    }

    @Override
    protected void onDetected(MarsColony colony) {
        laboratory.setOnFire();
    }

    @Override
    protected void onResolved(MarsColony colony) {
        laboratory.extinguishFire();
    }

    @Override
    protected void onUnresolved(MarsColony colony) {
        
    }
}
