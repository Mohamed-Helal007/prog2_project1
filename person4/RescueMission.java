/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class RescueMission extends Mission {

    private Agent strandedEntity;

    public RescueMission(String id, String name, Agent strandedEntity, int duration,
            int batteryNeeded, int[] resourceCost) {
        super(id, name, duration,
                new Capability[]{Capability.SURFACE_TRAVEL, Capability.COMMUNICATION},
                null, batteryNeeded, resourceCost, new String[0],
                new Facility[0], true, true, 2);
        this.strandedEntity = strandedEntity;
    }

    public Agent getStrandedEntity() {
        return strandedEntity;
    }

    public String getType() {
        return "Rescue";
    }
}
