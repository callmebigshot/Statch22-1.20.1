package net.bigshot.Statch22;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class FishingQualityEventHandler {

    @SubscribeEvent
    public void onQualityFishPickup(EntityItemPickupEvent event) {
        if (!ModList.get().isLoaded("quality_food")) {
            return;
        }

        ItemStack stack = event.getItem().getItem();
        Player player = event.getEntity();

        if (!isFishItem(stack)) {
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
            return null;
        }
        return null;
    }

    private boolean isFishItem(ItemStack stack) {
        if (stack.is(forgeTag("raw_fish")) ||
                stack.is(forgeTag("cooked_fish")) ||
                stack.is(forgeTag("fish"))) {
            return true;
        }

        if (stack.is(ItemTags.FISHES)) {
            return true;
        }

        return false;
    }

    private TagKey<net.minecraft.world.item.Item> forgeTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }

    private void awardQualityStats(ServerPlayer player, String quality) {
        quality = quality.toUpperCase();

        switch (quality) {
            case "SILVER":
                player.awardStat(Statch22.SILVER_CROPS_HARVESTED_STAT.get(), 1);
                FishingStatCalculator.addFishCaught(player, Statch22Config.FISHING_BASE_POINTS.get() + 2);
                break;
            case "GOLD":
                player.awardStat(Statch22.GOLD_CROPS_HARVESTED_STAT.get(), 1);
                FishingStatCalculator.addFishCaught(player, Statch22Config.FISHING_BASE_POINTS.get() + 4);
                break;
            case "DIAMOND":
                player.awardStat(Statch22.DIAMOND_CROPS_HARVESTED_STAT.get(), 1);
                FishingStatCalculator.addFishCaught(player, Statch22Config.FISHING_BASE_POINTS.get() + 7);
                break;
        }

        updateFishingStatImmediately(player);
    }

    private void updateFishingStatImmediately(Player player) {
        int current = FishingStatCalculator.getFishingScore(player);
        int last = FishingStatCalculator.getLastFishingScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.FISHING_STAT.get()), current - last);
            FishingStatCalculator.setLastFishingScore(player, current);
        }
    }
}