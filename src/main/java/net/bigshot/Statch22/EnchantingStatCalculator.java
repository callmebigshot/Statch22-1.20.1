package net.bigshot.Statch22;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantingStatCalculator {
    private static final Map<UUID, Integer> enchantingScores = new HashMap<>();
    private static final Map<UUID, Integer> lastEnchantingScores = new HashMap<>();

    public static void addEnchantingPoints(Player player, int xpSpent, int enchantmentCount, int totalRarityValue) {
        if (Statch22Config.ENABLE_ENCHANTING.get()) {
            int points = (xpSpent * Statch22Config.ENCHANTING_XP_MULTIPLIER.get()) +
                    (enchantmentCount * Statch22Config.ENCHANTING_COUNT_MULTIPLIER.get()) +
                    (totalRarityValue * Statch22Config.ENCHANTING_RARITY_MULTIPLIER.get());

            points = Math.max(points, Statch22Config.ENCHANTING_BASE_MULTIPLIER.get());
            addEnchantingScore(player, points);
        }
    }

    private static void addEnchantingScore(Player player, int amount) {
        UUID id = player.getUUID();
        int current = enchantingScores.getOrDefault(id, 0);
        enchantingScores.put(id, current + amount);
    }

    public static int getEnchantingScore(Player player) {
        return enchantingScores.getOrDefault(player.getUUID(), 0);
    }

    public static int getLastEnchantingScore(Player player) {
        return lastEnchantingScores.getOrDefault(player.getUUID(), 0);
    }

    public static void setLastEnchantingScore(Player player, int score) {
        lastEnchantingScores.put(player.getUUID(), score);
    }
}