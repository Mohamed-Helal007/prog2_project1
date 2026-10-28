package person2;
import facilities.Equipment;
import facilities.Location;
import java.util.EnumSet;

public class RepairRobot extends Robot {
    public RepairRobot(String id, String name, Location location) {
        this(id, name, location, MAX_BATTERY);
    }
 
    public RepairRobot(String id, String name, Location location, int initialBattery) {
        super(id, name, location, initialBattery, EnumSet.of(Capability.REPAIR));
    }

    public boolean repairEquipment(Equipment equipment) {
        if (!isOperational())
            return false;
        equipment.repair();
        System.out.println(getName() + " repaired " + equipment.getName() + ".");
        return true;
    }

    public void performTask() {
        System.out.println(getName() + " (Repair Robot) is repairing equipment.");
    }
}