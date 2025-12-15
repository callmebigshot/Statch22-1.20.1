package net.bigshot.Statch22;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantingEventHandler {
    private final Map<UUID, ItemStack> enchantingTableItems = new HashMap<>();
    private final Map<UUID, Integer> enchantingXPCosts = new HashMap<>();

    @SubscribeEvent
    public void onContainerOpen(PlayerContainerEvent.Open event) {
        if (event.getContainer() instanceof EnchantmentMenu) {
            enchantingXPCosts.put(event.getEntity().getUUID(), event.getEntity().experienceLevel);
        }
    }

    @SubscribeEvent
    public void onContainerClose(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof EnchantmentMenu enchantMenu) {
            UUID playerId = event.getEntity().getUUID();
            ItemStack afterItem = enchantMenu.getSlot(0).getItem();

            Integer beforeXP = enchantingXPCosts.get(playerId);
            int afterXP = event.getEntity().experienceLevel;

            if (beforeXP != null && beforeXP > afterXP) {
                int xpSpent = beforeXP - afterXP;
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(afterItem);

                if (!enchantments.isEmpty()) {
                    int enchantmentCount = enchantments.size();
                    int totalRarityValue = calculateTotalRarityValue(enchantments);

                    EnchantingStatCalculator.addEnchantingPoints((Player) event.getEntity(), xpSpent, enchantmentCount, totalRarityValue);
                    event.getEntity().awardStat(Stats.ENCHANT_ITEM, 1);
                    event.getEntity().awardStat(Statch22.ENCHANTMENTS_APPLIED_STAT.get(), enchantmentCount);
                    updateEnchantingStatImmediately((Player) event.getEntity());
                }
            }

            enchantingXPCosts.remove(playerId);
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack output = event.getOutput();

        if (!output.isEmpty()) {
            Map<Enchantment, Integer> leftEnchants = EnchantmentHelper.getEnchantments(left);
            Map<Enchantment, Integer> outputEnchants = EnchantmentHelper.getEnchantments(output);

            if (!outputEnchants.isEmpty()) {
                int newEnchantments = 0;
                int totalRarityValue = 0;

                for (Map.Entry<Enchantment, Integer> entry : outputEnchants.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    int level = entry.getValue();

                    Integer leftLevel = leftEnchants.get(enchantment);

                    if (leftLevel == null || level > leftLevel) {
                        newEnchantments++;
                        totalRarityValue += getRarityValue(enchantment);
                    }
                }

                if (newEnchantments > 0) {
                    int xpCost = Math.max(1, event.getCost() / 2);
                    EnchantingStatCalculator.addEnchantingPoints(player, xpCost, newEnchantments, totalRarityValue);
                    player.awardStat(Statch22.ENCHANTMENTS_APPLIED_STAT.get(), newEnchantments);
                    updateEnchantingStatImmediately(player);
                }
            }
        }
    }

    private int calculateTotalRarityValue(Map<Enchantment, Integer> enchantments) {
        int total = 0;
        for (Enchantment enchantment : enchantments.keySet()) {
            total += getRarityValue(enchantment);
        }
        return total;
    }

    private int getRarityValue(Enchantment enchantment) {
        if (enchantment.isTreasureOnly()) return 5;

        switch (enchantment.getRarity().toString()) {
            case "VERY_RARE": return 4;
            case "RARE": return 3;
            case "UNCOMMON": return 2;
            default: return 1;
        }
    }

    private void updateEnchantingStatImmediately(Player player) {
        int current = EnchantingStatCalculator.getEnchantingScore(player);
        int last = EnchantingStatCalculator.getLastEnchantingScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.ENCHANTING_STAT.get()), current - last);
            EnchantingStatCalculator.setLastEnchantingScore(player, current);
        }
    }
}