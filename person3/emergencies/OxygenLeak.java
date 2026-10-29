package person3.emergencies;

import java.util.Map;
import java.util.Set;

import person1.MarsColony;
import person3.facilities.*;
import person2.*;
public class OxygenLeak extends Emergency{
    private final Habitat habitat;
    private final int leakAmount;
    public OxygenLeak(Habitat habitat, int leakAmount) {
        super(EmergencyType.OXYGEN_LEAK, "Oxygen leak");
        if (habitat == null) {
            throw new IllegalArgumentException("Habitat cannot be null");
        }
        if (leakAmount <= 0) {
            throw new IllegalArgumentException("Leak amount must be positive");
        }

        this.habitat = habitat;
        this.leakAmount = leakAmount;
    }
    @Override
    protected Set<Capability> getRequiredCapabilities() {
        return Set.of(Capability.REPAIR);
    }
    @Override
    protected Map<ResourceType, Integer> getResponseCost() {
        return Map.of(ResourceType.SPARE_PARTS, 1);
    }

    @Override
    protected void onDetected(MarsColony colony) {
        habitat.loseOxygen(leakAmount);
    }

    @Override
    protected void onResolved(MarsColony colony) {
    }

    @Override
    protected void onUnresolved(MarsColony colony) {
        habitat.loseOxygen(leakAmount);
    }


}
