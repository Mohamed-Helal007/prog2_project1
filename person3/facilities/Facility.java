package person3.facilities;
import java.util.Map;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;
public abstract class Facility implements Location {
    private String name;
    private int capacity;
    private Location location;
    private FacilityStatus status;
    private Map<ResourceType, Integer> stock;
    private List<Equipment> EquipmentsList;
    public Facility(String name, int capacity, Location location) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        status = FacilityStatus.OPERATIONAL;
        stock = new EnumMap<>(ResourceType.class);
        EquipmentsList = new ArrayList<>();
    }
    @Override
    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public Location getLocation() {
        return location;
    }
    public FacilityStatus getStatus() {
        return status;
    }
    public int getResourceQuantity(ResourceType type){
        return stock.getOrDefault(type, 0);
    }
    public void addResource(ResourceType type,int quantity){
        stock.put(type,quantity);
    }
    public boolean useResource(ResourceType type,int quantity){
        int currentQuantity = stock.getOrDefault(type, 0);
        if(currentQuantity >= quantity){
            stock.put(type, currentQuantity - quantity);
            return true;
        }
        return false;
    }
    public boolean ISOPERATIONAL(){
        return status == FacilityStatus.OPERATIONAL;
    }
    public void setStatus(FacilityStatus status) {
        this.status = status;
    }
    public void addEquipment(Equipment equip){
        EquipmentsList.add(equip);
    }
    public List<Equipment> getEquipmentsList() {
        return EquipmentsList;
    }


    
    
}
