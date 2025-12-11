package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatCalculator {
    private static final Map<UUID, Integer> farmingScores = new HashMap<>();
    private static final Map<UUID, Integer> lastScores = new HashMap<>();

    public static void addCropsPlanted(Player player) {
        addScore(player, 1);
    }

    public static void addCropsHarvested(Player player) {
        addScore(player, 1);
    }

    public static void addAnimalsBred(Player player) {
        addScore(player, 3);
    }

    private static void addScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = farmingScores.getOrDefault(id, 0);
        farmingScores.put(id, current + amount);
    }

    public static int getScore(Player player) {
        return farmingScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastScore(Player player) {
        return lastScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastScore(Player player, int score) {
        lastScores.put(player.getUUID(), score);
    }
}