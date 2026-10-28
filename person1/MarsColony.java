package person1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarsColony {

    private final List<CrewMember> crew;
    private final List<Robot> robot;
    private final List<Facility> facilities;
    private final Environment environment;
    private final ResourcePool resourcepool;
    private final MissionManager missionManager;
    private final List<Emergency> emergencyLog;
    private int simulationTime;

    public MarsColony(

            List<CrewMember> crew,
            List <Robot> robot,
            List<Facility> facilities,
            Environment environment,
            ResourcePool resourcepool,
            MissionManager missionmanager) {

        if (crew == null || robot==null || facilities == null
                || environment == null || resourcepool==null
                || missionmanager == null) {
            throw new IllegalArgumentException(
                    "Colony parts cannot be null.");
        }

        this.crew = crew;
        this.robot=robot;
        this.facilities = facilities;
        this.environment=environment;
        this.resourcepool = resourcepool;
        this.missionManager = missionmanager;

        emergencyLog = new ArrayList<>();
        simulationTime=0;

    }
    public void addcrew(CrewMember member) {

        if (member==null)
            throw new IllegalArgumentException("Crew member cannot be null.");

        crew.add(member);

    }



    public void addrobot(Robot robot) {
        if (robot == null)
            throw new IllegalArgumentException("Robot cannot be null.");
        this.robot.add(robot);
    }

    public void addfacility(Facility facility){
        if(facility == null)
            throw new IllegalArgumentException("Facility cannot be null.");
        facilities.add(facility);
    }


    public List<Agent> getAllAgents() {
        List<Agent> agents =new ArrayList<>();
        agents.addAll(crew);
        agents.addAll( robot );
        return agents;
    }
    


    public List<Agent> getAvailableAgents() {
        List<Agent> availableAgents = new ArrayList<>();

        for (Agent agent : getAllAgents()) {
            if (agent.isAvailable()&&agent.isOperational())
                availableAgents.add(agent);
        }
        return availableAgents;

    }

    public OperationResult submitMission(Mission mission, List<Agent> agents) {
        if (mission==null)
            return OperationResult.fail("Mission cannot be null.");

        if (agents == null)
            return OperationResult.fail("Agent list cannot be null.");

        return missionManager.submit(mission, agents);
    }



    public void triggerEmergency(Emergency emergency) {
        if (emergency == null)
            throw new IllegalArgumentException("Emergency cannot be null.");

        emergencyLog.add(emergency);
        emergency.handle(this);
    }

    public int getTotal(ResourceType resourceType) {
        return resourcepool.getTotal(resourceType);
    }
    public List<CrewMember> getCrew() {
        return Collections.unmodifiableList(crew);
    }


    public List<Robot> getRobots() {
        return Collections.unmodifiableList(robot);
    }

    public List<Facility> getFacilities() {
        return Collections.unmodifiableList( facilities );
    }



    public List<Emergency> getEmergencyLog() {
        return Collections.unmodifiableList(emergencyLog);
    }

    public Environment getEnvironment() {
        return environment;
    }
    public ResourcePool getResourcePool() {
        return resourcepool;
    }

    public MissionManager getMissionManager() {
        return missionManager;
    }


    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        if (simulationTime<0) {
            throw new IllegalArgumentException("Time cannot be negative.");
        }
        this.simulationTime = simulationTime;
    }

    public String generateReport() {
        return ColonyReport.build(this);
    }

}