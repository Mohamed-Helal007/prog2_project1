/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class RepairMission extends Mission {

    private Facility targetFacility;
    private Equipment targetEquipment;

    public RepairMission(String id, String name, Facility targetFacility, Equipment targetEquipment,
            int duration, int batteryNeeded, int[] resourceCost) {
        super(id, name, duration, new Capability[]{Capability.REPAIR},
                null, batteryNeeded, resourceCost, new String[0],
                targetFacility == null ? new Facility[0] : new Facility[]{targetFacility},
                false, false, 2);
        this.targetFacility = targetFacility;
        this.targetEquipment = targetEquipment;
    }

    public Facility getTargetFacility() {
        return targetFacility;
    }

    public Equipment getTargetEquipment() {
        return targetEquipment;
    }

    public String getType() {
        return "Repair";
    }

    public boolean targetsUnavailable() {
        return false;
    }
}
