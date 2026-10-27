package person3.facilities;

public class SurfaceLocation implements Location {
    public static final SurfaceLocation MARS = new SurfaceLocation();
    private SurfaceLocation() {
    }
    @Override 
    public String getName() {
        return "Martian Surface";
    }
    
}
