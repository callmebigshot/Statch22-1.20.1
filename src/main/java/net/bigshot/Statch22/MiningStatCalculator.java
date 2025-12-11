package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningStatCalculator {
    private static final Map<UUID, Integer> miningScores = new HashMap<>();
    private static final Map<UUID, Integer> lastMiningScores = new HashMap<>();

    public static void addOreMined(Player player, int points) {
        if (Statch22Config.ENABLE_MINING.get()) {
            addMiningScore(player, points);
        }
    }

    public static void addStoneMined(Player player, int points) {
        if (Statch22Config.ENABLE_MINING.get()) {
            addMiningScore(player, points);
        }
    }

    private static void addMiningScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = miningScores.getOrDefault(id, 0);
        miningScores.put(id, current + amount);
    }

    public static int getMiningScore(Player player) {
        return miningScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastMiningScore(Player player) {
        return lastMiningScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastMiningScore(Player player, int score) {
        lastMiningScores.put(player.getUUID(), score);
    }
}