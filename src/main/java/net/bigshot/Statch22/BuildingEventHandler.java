package net.bigshot.Statch22;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BuildingEventHandler {

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.awardStat(Statch22.BLOCKS_PLACED_STAT.get(), 1);
            BuildingStatCalculator.addBlockPlaced(player);
            updateBuildingStatImmediately(player);
        }
    }

    private void updateBuildingStatImmediately(Player player) {
        int current = BuildingStatCalculator.getBuildingScore(player);
        int last = BuildingStatCalculator.getLastBuildingScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.BUILDING_STAT.get()), current - last);
            BuildingStatCalculator.setLastBuildingScore(player, current);
        }
    }
}