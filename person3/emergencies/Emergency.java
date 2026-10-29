package person3.emergencies;
import person3.facilities.*;
import person2.*;
import person1.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Emergency {
    private EmergencyType type;
    private EmergencyState state;
    private String reason;
    public Emergency(EmergencyType type,String reason){
        if(type==null||reason==null||reason.isBlank()){
            throw new IllegalArgumentException("Emergency type and reason are required");
        }
        this.type = type;
        this.reason = reason;
        this.state = EmergencyState.PENDING;
    }
    public EmergencyType getType() {
        return type;
    }
    public String getReason() {
        return reason;
    }
    public EmergencyState getState() {
        return state;
    }
    public OperationResult handle(MarsColony colony){
        if(colony==null){
            return OperationResult.fail("Colony cannot be null");
        }
        if(state!=EmergencyState.PENDING){
            return OperationResult.fail("Emergency already handled");
        }
        onDetected(colony);
        Set<Capability> required = getRequiredCapabilities();
        List<Agent> responders = colony.getAvailableAgents();
        responders.removeIf(agent ->!agent.canHandle(type)|| !agent.getCapabilities().containsAll(required));
        if(responders.isEmpty()){
            state=EmergencyState.UNRESOLVED;
            onUnresolved(colony);
            return OperationResult.fail("No suitable responders are available");
        }
        ResourcePool pool=colony.getResourcePool();
        Map<ResourceType,Integer> cost=getResponseCost();
        if(!pool.canSupply(cost)){
            state=EmergencyState.UNRESOLVED;
            onUnresolved(colony);
            return OperationResult.fail("The colony lack resources");
        }
        for (Agent responder :responders){
            responder.setAvailable(false);
        }
        //hnst5dm el try w el finally 3shan lazm arg3 el agents tany available flw 3mlt return 3adi msh hyrg3hm lakn el try wl fianlly lazm ynfz el finally
        try{
            if(!pool.useSupply(cost))
            {
                state=EmergencyState.UNRESOLVED;
                onUnresolved(colony);
                return OperationResult.fail("could not consume the response resources");
            }
            onResolved(colony);
        state=EmergencyState.RESOLVED;
        return OperationResult.ok();
        }
        finally
        {
            for (Agent responder :responders)
                {
                    responder.setAvailable(true);
                }
        }
    }
    protected abstract void onDetected(MarsColony colony);
    protected abstract void onUnresolved(MarsColony colony);
    protected abstract void onResolved(MarsColony colony);
    protected abstract  Map<ResourceType ,Integer> getResponseCost();
    protected abstract Set<Capability> getRequiredCapabilities();

    

}
