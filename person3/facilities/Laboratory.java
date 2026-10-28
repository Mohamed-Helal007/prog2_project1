package person3.facilities;

public class Laboratory extends Facility {
   private boolean onFire;
   public Laboratory(String name, Location location, int capacity) {
        super(name,capacity,location);
        onFire = false;
    }
    public boolean isOnFire() {
        return onFire;
    }
    public void setOnFire() {
        onFire = true;
        for (Equipment equip : getEquipmentsList()) {
            equip.damage();
        }
        setStatus(FacilityStatus.UNAVAILABLE);
    }
    public void extinguishFire() {
        onFire = false;
        if(getStatus() !=FacilityStatus.DESTROYED){
            setStatus(FacilityStatus.OPERATIONAL);
        }
        
    }
    
}
