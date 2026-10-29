/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class MissionManager implements Tickable {

    private Mission activeMission;
    private Mission[] history;
    private int historyCount;
    private ResourcePool resourcePool;
    private Environment environment;

    public MissionManager(ResourcePool resourcePool, Environment environment) {
        this.resourcePool = resourcePool;
        this.environment = environment;
        this.activeMission = null;
        this.history = new Mission[20];
        this.historyCount = 0;
    }

    public OperationResult submit(Mission mission, Agent[] agents) {
        if (mission.getState() != null) {
            return OperationResult.fail("Mission was already submitted");
        }
        if (activeMission != null) {
            return reject(mission, "Another mission is already active");
        }
        if (mission.needsSurface() && !environment.allowsSurfaceOperations()) {
            return reject(mission, "Surface operations are not allowed during a dust storm");
        }
        if (mission.needsComms() && environment.isCommsDisrupted()) {
            return reject(mission, "Communication is disrupted");
        }
        if (mission.targetsUnavailable()) {
            return reject(mission, "Target facility is not operational");
        }
        if (!mission.covers(agents)) {
            return reject(mission, "Agent does not possess the required capability");
        }
        for (int i = 0; i < agents.length; i++) {
            if (!agents[i].isAvailable() || !agents[i].isOperational()) {
                return reject(mission, "Agent " + agents[i].getName() + " is unavailable");
            }
            if (!agents[i].canPerform(mission)) {
                return reject(mission, "Agent does not possess the required capability");
            }
        }
        if (!mission.hasRequiredEquipment(agents)) {
            return reject(mission, "Required equipment is missing");
        }
        if (!resourcePool.canSupply(mission.getResourceCost())) {
            return reject(mission, "Insufficient resources");
        }
        OperationResult consumed = resourcePool.consume(mission.getResourceCost());
        if (!consumed.isSuccess()) {
            return reject(mission, consumed.getReason());
        }
        for (int i = 0; i < agents.length; i++) {
            agents[i].setAvailable(false);
        }
        mission.assign(agents);
        activeMission = mission;
        return OperationResult.ok();
    }

    public void onTick(MarsColony colony) {
        if (activeMission == null) {
            return;
        }
        String reason = findFailure(activeMission);
        if (reason != null) {
            activeMission.fail(reason);
            release(activeMission);
            return;
        }
        if (activeMission.getState() == MissionState.ASSIGNED) {
            activeMission.start();
            return;
        }
        activeMission.advance();
        if (activeMission.isFinished()) {
            release(activeMission);
        }
    }

    public void failActiveMission(String reason) {
        if (activeMission != null) {
            activeMission.fail(reason);
            release(activeMission);
        }
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    public Mission[] getHistory() {
        Mission[] copy = new Mission[historyCount];
        for (int i = 0; i < historyCount; i++) {
            copy[i] = history[i];
        }
        return copy;
    }

    private OperationResult reject(Mission mission, String reason) {
        mission.fail(reason);
        addToHistory(mission);
        return OperationResult.fail(reason);
    }

    private String findFailure(Mission mission) {
        if (mission.needsSurface() && !environment.allowsSurfaceOperations()) {
            return "Surface operations are not allowed during a dust storm";
        }
        if (mission.needsComms() && environment.isCommsDisrupted()) {
            return "Communication is disrupted";
        }
        Agent[] agents = mission.getAssignedAgents();
        for (int i = 0; i < agents.length; i++) {
            if (!agents[i].isOperational()) {
                return "Agent " + agents[i].getName() + " is damaged or has insufficient battery";
            }
        }
        if (mission.targetsUnavailable()) {
            return "Target facility or equipment failure";
        }
        return null;
    }

    private void release(Mission mission) {
        if (mission.getState() == MissionState.COMPLETED) {
            mission.onComplete();
        }
        Agent[] agents = mission.getAssignedAgents();
        for (int i = 0; i < agents.length; i++) {
            agents[i].setAvailable(true);
        }
        activeMission = null;
        addToHistory(mission);
    }

    private void addToHistory(Mission mission) {
        if (historyCount == history.length) {
            Mission[] bigger = new Mission[history.length * 2];
            for (int i = 0; i < history.length; i++) {
                bigger[i] = history[i];
            }
            history = bigger;
        }
        history[historyCount] = mission;
        historyCount++;
    }
}
