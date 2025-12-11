package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingStatCalculator {
    private static final Map<UUID, Integer> fishingScores = new HashMap<>();
    private static final Map<UUID, Integer> lastFishingScores = new HashMap<>();

    public static void addFishCaught(Player player, int points) {
        if (Statch22Config.ENABLE_FISHING.get()) {
            addFishingScore(player, points);
        }
    }

    private static void addFishingScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = fishingScores.getOrDefault(id, 0);
        fishingScores.put(id, current + amount);
    }

    public static int getFishingScore(Player player) {
        return fishingScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastFishingScore(Player player) {
        return lastFishingScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastFishingScore(Player player, int score) {
        lastFishingScores.put(player.getUUID(), score);
    }
}