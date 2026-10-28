package person1;

public class Simulation {

    private final MarsColony colony;
    private int time;

    public Simulation(MarsColony colony){

        if (colony==null) 
            throw new IllegalArgumentException("Colony cannot be null.");

        this.colony = colony;
        time=colony.getSimulationTime();
    }

    public void advanceTime(int units){
        if(units < 0)
            throw new IllegalArgumentException(
                    "Time units cannot be negative.");



        for (int i=0;i<units;i++){
            advanceOneUnit();
        }
    }
    private void advanceOneUnit(){
        time = colony.getSimulationTime()+1;
        colony.setSimulationTime(time);

        colony.getMissionManager().onTick(colony);

        for (CrewMember member : colony.getCrew()){
            member.onTick(colony);
        }


        for (Robot robot : colony.getRobots()){
            robot.onTick(colony);
        }
    }

    public int getTime() {
        return time;
    }

}