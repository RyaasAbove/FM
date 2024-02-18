package net.Ryaas.firstmod.util;

import java.util.HashMap;
import java.util.UUID;

public class IndicatorOnOff {
    private static final HashMap<UUID, Boolean> particleToggle = new HashMap<>();

    public static void toggleParticleVisibility(UUID playerID) {
        particleToggle.put(playerID, !particleToggle.getOrDefault(playerID, false));
    }

    public static boolean isParticleVisible(UUID playerID) {
        return particleToggle.getOrDefault(playerID, false);
    }
}
