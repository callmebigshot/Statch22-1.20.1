package net.bigshot.Statch22;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class FarmingQualityEventHandler {

    @SubscribeEvent
    public void onQualityItemPickup(EntityItemPickupEvent event) {
        if (!ModList.get().isLoaded("quality_food")) {
            return;
        }

        ItemStack stack = event.getItem().getItem();
        Player player = event.getEntity();

        if (!isCropItem(stack)) {
            return;
        }

        String quality = getQualityViaReflection(stack);

        if (quality != null && player instanceof ServerPlayer serverPlayer) {
            awardQualityStats(serverPlayer, quality);
        }
    }

    private String getQualityViaReflection(ItemStack stack) {
        try {
            Class<?> qualityUtilsClass = Class.forName("de.cadentem.quality_food.util.QualityUtils");
            java.lang.reflect.Method getQualityMethod = qualityUtilsClass.getMethod("getQuality", ItemStack.class);

            Object qualityObj = getQualityMethod.invoke(null, stack);
            if (qualityObj != null) {
                return qualityObj.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    private boolean isCropItem(ItemStack stack) {
        String itemName = stack.getItem().getDescriptionId().toLowerCase();
        String modId = stack.getItem().getCreatorModId(stack);

        boolean isVanillaCrop = itemName.contains("wheat") ||
                itemName.contains("carrot") ||
                itemName.contains("potato") ||
                itemName.contains("beetroot") ||
                itemName.contains("berry") ||
                itemName.contains("melon") ||
                itemName.contains("pumpkin") ||
                itemName.contains("crop") ||
                itemName.contains("seed");

        if (isVanillaCrop) return true;

        if (modId != null) {
            switch (modId) {
                case "farmersdelight":
                    return isFarmersDelightCrop(itemName);
                case "vinery":
                    return isVineryCrop(itemName);
                case "quality_food":
                    return true;
            }
        }

        return false;
    }

    private boolean isFarmersDelightCrop(String itemName) {
        return itemName.contains("cabbage") ||
                itemName.contains("tomato") ||
                itemName.contains("onion") ||
                itemName.contains("rice") ||
                itemName.contains("wild") ||
                itemName.contains("canvas") ||
                itemName.contains("straw");
    }

    private boolean isVineryCrop(String itemName) {
        return itemName.contains("grape") ||
                itemName.contains("cherry") ||
                itemName.contains("apple") ||
                itemName.contains("taiga") ||
                itemName.contains("jungle") ||
                itemName.contains("savanna") ||
                itemName.contains("meadow");
    }

    private void awardQualityStats(ServerPlayer player, String quality) {
        quality = quality.toUpperCase();

        switch (quality) {
            case "SILVER":
                player.awardStat(Statch22.SILVER_CROPS_HARVESTED_STAT.get(), 1);
                StatCalculator.addCropsHarvested(player, Statch22Config.FARMING_BASE_POINTS.get() + 2);
                break;
            case "GOLD":
                player.awardStat(Statch22.GOLD_CROPS_HARVESTED_STAT.get(), 1);
                StatCalculator.addCropsHarvested(player, Statch22Config.FARMING_BASE_POINTS.get() + 4);
                break;
            case "DIAMOND":
                player.awardStat(Statch22.DIAMOND_CROPS_HARVESTED_STAT.get(), 1);
                StatCalculator.addCropsHarvested(player, Statch22Config.FARMING_BASE_POINTS.get() + 7);
                break;
        }

        updateFarmingStatImmediately(player);
    }

    private void updateFarmingStatImmediately(Player player) {
        int current = StatCalculator.getScore(player);
        int last = StatCalculator.getLastScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.FARMING_STAT.get()), current - last);
            StatCalculator.setLastScore(player, current);
        }
    }
}