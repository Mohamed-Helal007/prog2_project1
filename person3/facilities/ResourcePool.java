package person3.facilities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ResourcePool {
    private List<Facility> facilities;
    public ResourcePool(List<Facility> facilities) {
        this.facilities = new ArrayList<>(facilities);
    }
    public int getTotal(ResourceType type) {
        int total = 0;

        for (Facility facility : facilities) {
            if (facility.ISOPERATIONAL()) {
                total += facility.getResourceQuantity(type);
            }
        }
        return total;
    }
    


    
}
