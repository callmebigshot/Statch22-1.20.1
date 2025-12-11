package net.bigshot.Statch22.integration.treechop;

import net.bigshot.Statch22.ForageStatCalculator;
import net.bigshot.Statch22.Statch22;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TreeChopIntegration {

    @SubscribeEvent
    public void onTreeChop(Event event) {
        String className = event.getClass().getName();
        if (className.contains("ChoppingEvent") || className.contains("treechop")) {
            handleTreeChopEvent(event);
        }
    }

    private void handleTreeChopEvent(Event event) {
        try {
            java.lang.reflect.Method getPlayerMethod = event.getClass().getMethod("getPlayer");
            java.lang.reflect.Method getNumChopsMethod = event.getClass().getMethod("getNumChops");

            Player player = (Player) getPlayerMethod.invoke(event);
            Integer logCount = (Integer) getNumChopsMethod.invoke(event);

            if (player instanceof ServerPlayer serverPlayer && logCount != null) {
                for (int i = 0; i < logCount; i++) {
                    serverPlayer.awardStat(Statch22.LOGS_CHOPPED_STAT.get(), 1);
                    ForageStatCalculator.addLogsChopped(serverPlayer);
                }
                updateForagingStatImmediately(serverPlayer);
            }
        } catch (Exception e) {
        }
    }

    @SubscribeEvent
    public void onBlockBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            if (isLikelyTreeChopAction(event)) {
            }
        }
    }

    private boolean isLikelyTreeChopAction(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
        return net.minecraftforge.fml.ModList.get().isLoaded("treechop") &&
                event.getState().getBlock().toString().toLowerCase().contains("log");
    }

    private void updateForagingStatImmediately(Player player) {
        int current = ForageStatCalculator.getForageScore(player);
        int last = ForageStatCalculator.getLastForageScore(player);
        if (current > last) {
            player.awardStat(Stats.CUSTOM.get(Statch22.FORAGING_STAT.get()), current - last);
            ForageStatCalculator.setLastForageScore(player, current);
        }
    }
}