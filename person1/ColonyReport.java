package person1;
import person2.*;
import person3.facilities.*;
import person3.emergencies.*;

public class ColonyReport{

    public static String build(MarsColony colony){
        String report = "MARS-OPS COLONY REPORT\n";

        report += "Time: " + colony.getSimulationTime() + "\n";
        report += "Environment: " + colony.getEnvironment().getCondition()+"\n\n";

        report += "RESOURCE TOTALS\n";
        for (ResourceType type : ResourceType.values()){
            report += type + ": " + colony.getTotal(type)+"\n";
        }

        report += "\nCREW\n";
        for (CrewMember member : colony.getCrew()){
            String location;
            if (member.getLocation()==null)
                location = "Unknown";
            else
                location = member.getLocation().getName();

            report += member.getName() + " | location: " + location + " | available: " + member.isAvailable() + " | health: " + member.getHealth() + "\n";
        }

        report += "\nROBOTS\n";
        for (Robot robot : colony.getRobots()){
            String location;
            if (robot.getLocation() == null)
                location = "Unknown";
            else
                location = robot.getLocation().getName();

            report += robot.getName() + " | location: " + location + " | available: " + robot.isAvailable() + " | battery: " + robot.getBatteryLevel()+"\n";
        }



        report += "\nFACILITIES\n";
        for (Facility facility : colony.getFacilities()){
            report += facility.getName() + " | status: " + facility.getStatus() + "\n";
        }

        report += "\nACTIVE MISSION\n";
        Mission activeMission = colony.getMissionManager().getActiveMission();

        if (activeMission==null)
            report += "No active mission.\n";
         else {
            report += activeMission.getName() + " | state: " + activeMission.getState() + " | progress: " + activeMission.getElapsed() + "/" + activeMission.getDuration() + "\n";

            if (activeMission.getFailureReason() != null && !activeMission.getFailureReason().isEmpty())
                report += "Reason: " + activeMission.getFailureReason() + "\n";

        }

        report += "\nMISSION HISTORY\n";
        if (colony.getMissionManager().getHistory().isEmpty())
            report += "No missions recorded.\n";
         else {
            for (Mission mission : colony.getMissionManager().getHistory()){
                report += mission.getName() + " | state: " + mission.getState();

                if (mission.getFailureReason()!=null && !mission.getFailureReason().isEmpty())
                    report += " | reason: " + mission.getFailureReason();

                report += "\n";
            }
        }

        report += "\nEMERGENCY OUTCOMES\n";
        if (colony.getEmergencyLog().isEmpty())
            report += "No emergencies recorded.\n";
         else {
            for (Emergency emergency : colony.getEmergencyLog()){
                report += emergency.getType() + " | state: " + emergency.getState();

                if (emergency.getReason() != null && !emergency.getReason().isEmpty())
                    report += " | reason: " + emergency.getReason();

                report += "\n";
            }
        }

        return report;
    }

}