package person2;
import person3.emergencies.EmergencyType;

public enum Capability {
    SURFACE_TRAVEL,
    CARRY_EQUIPMENT,
    RESEARCH,
    REPAIR,
    MEDICAL,
    FIRE_RESPONSE,
    AERIAL_RECON,
    COMMUNICATIONS;

    public static Capability requiredFor(EmergencyType emergency) {
        switch (emergency) {
            case FIRE: return FIRE_RESPONSE;
            case OXYGEN_LEAK: return REPAIR;
            case ROVER_BREAKDOWN: return REPAIR;
            case MEDICAL: return MEDICAL;
            default: throw new IllegalArgumentException("Unknown emergency type: " + emergency);
        }
    }
}