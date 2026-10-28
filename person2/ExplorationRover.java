package person2;
import person3.facilities.Equipment;
import person3.facilities.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ExplorationRover extends Robot {
    private final List<Equipment> cargo = new ArrayList<>();
 
    public ExplorationRover(String id, String name, Location location) {
        this(id, name, location, MAX_BATTERY);
    }
 
    public ExplorationRover(String id, String name, Location location, int initialBattery) {
        super(id, name, location, initialBattery, EnumSet.of(Capability.SURFACE_TRAVEL, Capability.CARRY_EQUIPMENT));
    }

    public void load(Equipment equipment) {
        cargo.add(equipment);
    }

    public boolean carries(String equipmentName) {
        for (Equipment e : cargo) {
            if (e.getName().equals(equipmentName) && e.isWorking())
                return true;
        }
        return false;
    }

    public List<Equipment> getCargo() {
        return Collections.unmodifiableList(cargo);
    }

    public void performTask() {
        System.out.println(getName() + " (Exploration Rover) is exploring the surface.");
    }
}