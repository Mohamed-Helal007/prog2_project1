/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class ResearchMission extends Mission {

    private Laboratory laboratory;

    public ResearchMission(String id, String name, Laboratory laboratory, int duration,
            String requiredSkill, int batteryNeeded, int[] resourceCost,
            String[] requiredEquipment) {
        super(id, name, duration, new Capability[]{Capability.RESEARCH},
                requiredSkill, batteryNeeded, resourceCost, requiredEquipment,
                new Facility[]{laboratory}, false, false, 1);
        this.laboratory = laboratory;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public String getType() {
        return "Research";
    }
}
