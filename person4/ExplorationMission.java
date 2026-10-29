/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class ExplorationMission extends Mission {

    private String region;
    private Location[] previousLocations;

    public ExplorationMission(String id, String name, String region, int duration,
            String requiredSkill, int batteryNeeded, int[] resourceCost,
            String[] requiredEquipment) {
        super(id, name, duration,
                new Capability[]{Capability.SURFACE_TRAVEL, Capability.CARRY_EQUIPMENT, Capability.RESEARCH},
                requiredSkill, batteryNeeded, resourceCost, requiredEquipment,
                new Facility[0], true, false, 2);
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return "Exploration";
    }

    public void onStart() {
        Agent[] agents = getAssignedAgents();
        previousLocations = new Location[agents.length];
        for (int i = 0; i < agents.length; i++) {
            previousLocations[i] = agents[i].getLocation();
            agents[i].setLocation(SurfaceLocation.getInstance());
        }
    }

    public void onComplete() {
        Agent[] agents = getAssignedAgents();
        for (int i = 0; i < agents.length; i++) {
            agents[i].setLocation(previousLocations[i]);
        }
    }
}
