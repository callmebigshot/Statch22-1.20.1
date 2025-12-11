package net.bigshot.Statch22;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class FishingEventHandler {

    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack caughtStack = event.getDrops().get(0);
            Item caughtItem = caughtStack.getItem();

            int bonusPoints = getTideFishBonus(caughtItem);

            FishingStatCalculator.addFishCaught(serverPlayer, 1 + bonusPoints);
            updateFishingStatImmediately(serverPlayer);
        }
    }

    private int getTideFishBonus(Item caughtItem) {
        if (!ModList.get().isLoaded("tide")) {
            return 0;
        }

        try {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(caughtItem);
            if (itemId == null || !itemId.getNamespace().equals("tide")) {
                return 0;
            }

            if (caughtItem.getRarity(caughtItem.getDefaultInstance()) == Rarity.EPIC) {
                return 10;
            }

            String fullId = itemId.toString();
            java.util.Map<String, Integer> specialFishBonus = java.util.Map.of(
                    "tide:midas_fish", 8,
                    "tide:voidseeker", 8,
                    "tide:shooting_starfish", 8,
                    "tide:blazing_swordfish", 5,
                    "tide:bedrock_tetra", 5,
                    "tide:elytrout", 5,
                    "tide:aquathorn", 10,
                    "tide:windbass", 10
            );

            return specialFishBonus.getOrDefault(fullId, 0);

        } catch (Exception e) {
            return 0;
        }
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