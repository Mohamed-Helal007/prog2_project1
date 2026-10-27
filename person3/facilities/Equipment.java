package person3.facilities;

public class Equipment {
    private String name;
    private boolean working;
    public Equipment(String name, boolean working) {
        this.name = name;
        this.working = working;
    }
    public String getName() {
        return name;
    }
    public void damage() {
        working = false;
    }
    public void repair() {
        working = true;
    }
    public boolean isWorking() {
        return working;
    }
   

    
}
