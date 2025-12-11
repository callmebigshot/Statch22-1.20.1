package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForageStatCalculator {
    private static final Map<UUID, Integer> forageScores = new HashMap<>();
    private static final Map<UUID, Integer> lastForageScores = new HashMap<>();

    public static void addForageHarvested(Player player) {
        if (Statch22Config.ENABLE_FORAGING.get()) {
            addForageScore(player, Statch22Config.FORAGING_LOG_POINTS.get());
        }
    }

    public static void addLogsChopped(Player player) {
        if (Statch22Config.ENABLE_FORAGING.get()) {
            addForageScore(player, Statch22Config.FORAGING_LOG_POINTS.get());
        }
    }

    public static void addRareForage(Player player) {
        if (Statch22Config.ENABLE_FORAGING.get()) {
            addForageScore(player, Statch22Config.FORAGING_RARE_POINTS.get());
        }
    }

    private static void addForageScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = forageScores.getOrDefault(id, 0);
        forageScores.put(id, current + amount);
    }

    public static int getForageScore(Player player) {
        return forageScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastForageScore(Player player) {
        return lastForageScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastForageScore(Player player, int score) {
        lastForageScores.put(player.getUUID(), score);
    }
}