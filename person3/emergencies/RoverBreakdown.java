package person3.emergencies;
import person1.*;
import person2.*;
import person3.facilities.*;
import java.util.Map;
import java.util.Set;

public class RoverBreakdown extends Emergency {
    private final Robot rover;

    public RoverBreakdown(Robot rover) {
        super(EmergencyType.ROVER_BREAKDOWN, "Rover breakdown");

        if (rover == null) {
            throw new IllegalArgumentException("Rover cannot be null");
        }

        this.rover = rover;
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
        rover.damage();
        colony.getMissionManager()
              .failActiveMission("Rover broke down");
    }

    @Override
    protected void onResolved(MarsColony colony) {
        rover.repair();
    }

    @Override
    protected void onUnresolved(MarsColony colony) {

    }
}