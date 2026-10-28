package person3.facilities;

import java.util.ArrayList;
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
    public boolean canSupply(Map<ResourceType,Integer> requirments){
        for(Map.Entry<ResourceType,Integer> requirment:requirments.entrySet()){
            ResourceType type = requirment.getKey();
            int required = requirment.getValue();
            if(required<0||getTotal(type)<required){
                return false;
            }

        }
        return true;
    }
    public boolean useSupply(Map<ResourceType,Integer> requirments){
        if(!canSupply(requirments)){
            return false;
        }
        for(Map.Entry<ResourceType,Integer> requirment:requirments.entrySet()){
            ResourceType type = requirment.getKey();
            int required = requirment.getValue();
            for(Facility facility:facilities){
                if(facility.ISOPERATIONAL()){
                    int available = facility.getResourceQuantity(type);
                    if(available>=required){
                        facility.useResource(type,required);
                        break;
                    }else{
                        facility.useResource(type,available);
                        required -= available;
                    }
                }
            }
        }
        return true;
    }


    
}
