package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatStatCalculator {
    private static final Map<UUID, Integer> combatScores = new HashMap<>();
    private static final Map<UUID, Integer> lastCombatScores = new HashMap<>();

    public static void addMobKill(Player player, int weight) {
        addCombatScore(player, weight);
    }

    public static void addPlayerKill(Player player) {
        addCombatScore(player, 10);
    }

    public static void addDamageDealt(Player player, int points) {
        addCombatScore(player, points);
    }

    public static void addDamageBlocked(Player player, int points) {
        addCombatScore(player, points);
    }

    public static void addDamageResisted(Player player, int points) {
        addCombatScore(player, points);
    }

    private static void addCombatScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = combatScores.getOrDefault(id, 0);
        combatScores.put(id, current + amount);
    }

    public static int getCombatScore(Player player) {
        return combatScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastCombatScore(Player player) {
        return lastCombatScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastCombatScore(Player player, int score) {
        lastCombatScores.put(player.getUUID(), score);
    }
}