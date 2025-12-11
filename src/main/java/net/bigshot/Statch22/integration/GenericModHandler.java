package net.bigshot.Statch22.integration;

import net.bigshot.Statch22.CombatStatCalculator;
import net.bigshot.Statch22.StatCalculator;
import net.bigshot.Statch22.Statch22;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class GenericModHandler {

    @SubscribeEvent
    public void onGenericModdedCropHarvest(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
            if (blockId != null && !blockId.getNamespace().equals("minecraft")) {
                if (isModdedCrop(blockId)) {
                    player.awardStat(Statch22.CROPS_HARVESTED_STAT.get(), 1);
                    StatCalculator.addCropsHarvested(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGenericModdedCropPlant(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack heldItem = player.getItemInHand(event.getHand());
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(heldItem.getItem());
            if (itemId != null && !itemId.getNamespace().equals("minecraft")) {
                if (isModdedSeed(itemId)) {
                    player.awardStat(Statch22.CROPS_PLANTED_STAT.get(), 1);
                    StatCalculator.addCropsPlanted(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGenericModdedEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            LivingEntity killed = event.getEntity();
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(killed.getType());
            if (entityId != null && !entityId.getNamespace().equals("minecraft")) {
                int weight = calculateDynamicWeight(killed);
                if (weight > 0) {
                    CombatStatCalculator.addMobKill(player, weight);
                    updateCombatStatImmediately(player);
                }
            }
        }
    }

    private boolean isModdedCrop(ResourceLocation blockId) {
        String path = blockId.getPath();
        return path.contains("crop") ||
                path.contains("plant") ||
                path.contains("bush") ||
                path.contains("stem") ||
                path.contains("vine") ||
                path.contains("berry") ||
                path.contains("fruit") ||
                path.contains("mushroom") ||
                path.contains("fungus") ||
                path.contains("sprout") ||
                path.contains("shoot");
    }

    private boolean isModdedSeed(ResourceLocation itemId) {
        String path = itemId.getPath();
        return path.contains("seed") ||
                path.contains("sapling") ||
                path.contains("spore") ||
                path.contains("propagule") ||
                path.contains("bulb") ||
                path.contains("tubers") ||
                path.contains("roots") ||
                path.contains("cutting");
    }

    private int calculateDynamicWeight(LivingEntity entity) {
        float health = entity.getMaxHealth();
        if (health >= 500) return 150;
        if (health >= 400) return 120;
        if (health >= 300) return 100;
        if (health >= 200) return 80;
        if (health >= 100) return 40;
        if (health >= 50) return 15;
        if (health >= 25) return 8;
        if (health >= 10) return 4;
        return 2;
    }

    private void updateCombatStatImmediately(Player player) {
        int current = CombatStatCalculator.getCombatScore(player);
        int last = CombatStatCalculator.getLastCombatScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.COMBAT_STAT.get()), current - last);
            CombatStatCalculator.setLastCombatScore(player, current);
        }
    }

    @SubscribeEvent
    public void onGenericModdedForageHarvest(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
            if (blockId != null && !blockId.getNamespace().equals("minecraft")) {
                if (isModdedForageable(blockId)) {
                }
            }
        }
    }

    private boolean isModdedForageable(ResourceLocation blockId) {
        String path = blockId.getPath();
        return path.contains("berry") ||
                path.contains("mushroom") ||
                path.contains("fungus") ||
                path.contains("vine") ||
                path.contains("moss") ||
                path.contains("lichen") ||
                path.contains("root") ||
                path.contains("flower") ||
                path.contains("grass") ||
                path.contains("fern") ||
                path.contains("kelp") ||
                path.contains("seaweed") ||
                path.contains("algae") ||
                path.contains("coral") ||
                path.contains("fruit") ||
                path.contains("nut") ||
                path.contains("cone");
    }

    private boolean isModdedLog(ResourceLocation blockId) {
        String path = blockId.getPath();
        return path.contains("log") ||
                path.contains("wood") ||
                path.contains("stem") ||
                path.contains("hyphae") ||
                path.contains("stalk") ||
                path.contains("trunk");
    }
}