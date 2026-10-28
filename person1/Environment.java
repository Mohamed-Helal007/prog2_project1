package person1;

public class Environment {

    private EnvironmentCondition condition ;

    public Environment(){
        condition=EnvironmentCondition.NORMAL;

    }

    public void setCondition(EnvironmentCondition condition){

        if(condition==null)
                return ;

        this.condition=condition;
    }

    public EnvironmentCondition getCondition(){
        return condition;
    }

    public boolean isDustStorm(){
        return condition == EnvironmentCondition.DUST_STORM;
    }

    public boolean isCommsDisrupted(){
        return condition == EnvironmentCondition.COMM_DISRUPT;
    }

    public boolean allowsSurfaceOperations(){
        return !isDustStorm();
    }

    public boolean allowsRemoteControl(){
        return !isCommsDisrupted();
    }

    
}
