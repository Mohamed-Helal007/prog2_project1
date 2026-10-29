/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public abstract class Mission {

    private String id;
    private String name;
    private int duration;
    private int elapsed;
    private MissionState state;
    private String failureReason;
    private Capability[] requiredCapabilities;
    private String requiredSkill;
    private int batteryNeeded;
    private int[] resourceCost;
    private String[] requiredEquipment;
    private Facility[] targetFacilities;
    private Agent[] assignedAgents;
    private boolean needsSurface;
    private boolean needsComms;
    private int minAgents;
    
    public abstract String getType();

    public Mission(String id, String name, int duration, Capability[] requiredCapabilities,
            String requiredSkill, int batteryNeeded, int[] resourceCost,
            String[] requiredEquipment, Facility[] targetFacilities,
            boolean needsSurface, boolean needsComms, int minAgents) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.elapsed = 0;
        this.state = null;
        this.failureReason = null;
        this.requiredCapabilities = requiredCapabilities;
        this.requiredSkill = requiredSkill;
        this.batteryNeeded = batteryNeeded;
        this.resourceCost = resourceCost;
        this.requiredEquipment = requiredEquipment;
        this.targetFacilities = targetFacilities;
        this.assignedAgents = new Agent[0];
        this.needsSurface = needsSurface;
        this.needsComms = needsComms;
        this.minAgents = minAgents;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getElapsed() {
        return elapsed;
    }

    public MissionState getState() {
        return state;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Capability[] getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public String getRequiredSkill() {
        return requiredSkill;
    }

    public int getBatteryNeeded() {
        return batteryNeeded;
    }

    public int[] getResourceCost() {
        return resourceCost;
    }

    public int getCost(ResourceType type) {
        if (type.ordinal() < resourceCost.length) {
            return resourceCost[type.ordinal()];
        }
        return 0;
    }

    public String[] getRequiredEquipment() {
        return requiredEquipment;
    }

    public Facility[] getTargetFacilities() {
        return targetFacilities;
    }

    public Agent[] getAssignedAgents() {
        return assignedAgents;
    }

    public boolean needsSurface() {
        return needsSurface;
    }

    public boolean needsComms() {
        return needsComms;
    }
    
    public boolean targetsUnavailable() {
        for (int i = 0; i < targetFacilities.length; i++) {
            if (!targetFacilities[i].isOperational()) {
                return true;
            }
        }
        return false;
    }

    public void onStart() {
    }

    public void onComplete() {
    }

    public boolean covers(Agent[] agents) {
        if (agents == null || agents.length < minAgents) {
            return false;
        }
        for (int i = 0; i < requiredCapabilities.length; i++) {
            boolean provided = false;
            for (int j = 0; j < agents.length; j++) {
                if (agents[j].hasCapability(requiredCapabilities[i])) {
                    provided = true;
                }
            }
            if (!provided) {
                return false;
            }
        }
        return true;
    }

    public boolean hasRequiredEquipment(Agent[] agents) {
        for (int i = 0; i < requiredEquipment.length; i++) {
            boolean carried = false;
            for (int j = 0; j < agents.length; j++) {
                if (agents[j].hasEquipment(requiredEquipment[i])) {
                    carried = true;
                }
            }
            if (!carried) {
                return false;
            }
        }
        return true;
    }

    public void assign(Agent[] agents) {
        if (state == null) {
            assignedAgents = agents;
            state = MissionState.ASSIGNED;
        }
    }

    public void start() {
        if (state == MissionState.ASSIGNED) {
            state = MissionState.IN_PROGRESS;
            onStart();
        }
    }

    public void advance() {
        if (state == MissionState.IN_PROGRESS) {
            elapsed++;
            if (elapsed >= duration) {
                state = MissionState.COMPLETED;
            }
        }
    }

    public void fail(String reason) {
        if (state != MissionState.COMPLETED && state != MissionState.FAILED) {
            state = MissionState.FAILED;
            failureReason = reason;
        }
    }

    public boolean isFinished() {
        return state == MissionState.COMPLETED || state == MissionState.FAILED;
    }

}
