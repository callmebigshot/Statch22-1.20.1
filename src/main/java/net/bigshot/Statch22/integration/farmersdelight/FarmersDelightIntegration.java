package net.bigshot.Statch22.integration.farmersdelight;

import net.bigshot.Statch22.ForageStatCalculator;
import net.bigshot.Statch22.StatCalculator;
import net.bigshot.Statch22.Statch22;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.HashSet;
import java.util.Set;

public class FarmersDelightIntegration {
    private static final Set<String> WILD_CROPS = new HashSet<>();
    private static final Set<String> CROPS = new HashSet<>();

    static {
        WILD_CROPS.add("farmersdelight:wild_onions");
        WILD_CROPS.add("farmersdelight:wild_carrots");
        WILD_CROPS.add("farmersdelight:wild_potatoes");
        WILD_CROPS.add("farmersdelight:wild_cabbages");
        WILD_CROPS.add("farmersdelight:wild_tomatoes");
        WILD_CROPS.add("farmersdelight:wild_rice");
        WILD_CROPS.add("farmersdelight:wild_beetroots");

        CROPS.add("farmersdelight:cabbage_crop");
        CROPS.add("farmersdelight:tomato_crop");
        CROPS.add("farmersdelight:onion_crop");
        CROPS.add("farmersdelight:rice_crop");
        CROPS.add("farmersdelight:rice_panicles");
    }

    @SubscribeEvent
    public void onCropHarvest(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());

            if (blockId != null) {
                String blockIdStr = blockId.toString();

                if (WILD_CROPS.contains(blockIdStr)) {
                    player.awardStat(Statch22.FORAGE_HARVESTED_STAT.get(), 1);
                    ForageStatCalculator.addForageHarvested(player);
                    updateForagingStatImmediately(player);
                }
                else if (CROPS.contains(blockIdStr)) {
                    player.awardStat(Statch22.CROPS_HARVESTED_STAT.get(), 1);
                    StatCalculator.addCropsHarvested(player);
                    updateFarmingStatImmediately(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCropPlant(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
            if (itemId != null && itemId.getNamespace().equals("farmersdelight")) {
                String path = itemId.getPath();
                if (path.contains("seeds") ||
                        path.equals("cabbage_seeds") ||
                        path.equals("tomato_seeds") ||
                        path.equals("onion") ||
                        path.equals("rice")) {
                    player.awardStat(Statch22.CROPS_PLANTED_STAT.get(), 1);
                    StatCalculator.addCropsPlanted(player);
                    updateFarmingStatImmediately(player);
                }
            }
        }
    }

    private void updateForagingStatImmediately(Player player) {
        int current = ForageStatCalculator.getForageScore(player);
        int last = ForageStatCalculator.getLastForageScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.FORAGING_STAT.get()), current - last);
            ForageStatCalculator.setLastForageScore(player, current);
        }
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