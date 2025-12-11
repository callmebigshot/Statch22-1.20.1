package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuildingStatCalculator {
    private static final Map<UUID, Integer> buildingScores = new HashMap<>();
    private static final Map<UUID, Integer> lastBuildingScores = new HashMap<>();

    public static void addBlockPlaced(Player player) {
        if (Statch22Config.ENABLE_BUILDING.get()) {
            addBuildingScore(player, Statch22Config.BUILDING_BASE_POINTS.get());
        }
    }

    public static void addComplexStructure(Player player) {
        if (Statch22Config.ENABLE_BUILDING.get()) {
            addBuildingScore(player, 5);
        }
    }

    private static void addBuildingScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = buildingScores.getOrDefault(id, 0);
        buildingScores.put(id, current + amount);
    }

    public static int getBuildingScore(Player player) {
        return buildingScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastBuildingScore(Player player) {
        return lastBuildingScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastBuildingScore(Player player, int score) {
        lastBuildingScores.put(player.getUUID(), score);
    }
}