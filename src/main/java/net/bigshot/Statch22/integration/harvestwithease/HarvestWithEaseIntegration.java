package net.bigshot.Statch22.integration.harvestwithease;

import it.crystalnest.harvest_with_ease.api.event.HarvestEvents;
import net.bigshot.Statch22.Statch22;
import net.bigshot.Statch22.StatCalculator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.HashSet;
import java.util.Set;

public class HarvestWithEaseIntegration {
    private static final Set<String> REPLANT_CROPS = new HashSet<>();

    static {
        REPLANT_CROPS.add("minecraft:wheat");
        REPLANT_CROPS.add("minecraft:carrots");
        REPLANT_CROPS.add("minecraft:potatoes");
        REPLANT_CROPS.add("minecraft:beetroots");
        REPLANT_CROPS.add("farmersdelight:cabbages");
        REPLANT_CROPS.add("farmersdelight:onions");
        REPLANT_CROPS.add("farmersdelight:rice");
        REPLANT_CROPS.add("vinery:grapevine");
    }

    @SubscribeEvent
    public void onHarvestWithEase(HarvestEvents.AfterHarvestEvent event) {
        ServerPlayer player = event.getEntity();
        BlockState crop = event.getCrop();
        ResourceLocation cropId = ForgeRegistries.BLOCKS.getKey(crop.getBlock());

        player.awardStat(Statch22.CROPS_HARVESTED_STAT.get(), 1);
        StatCalculator.addCropsHarvested(player);

        if (cropId != null && REPLANT_CROPS.contains(cropId.toString())) {
            player.awardStat(Statch22.CROPS_PLANTED_STAT.get(), 1);
            StatCalculator.addCropsPlanted(player);
        }
    }
}