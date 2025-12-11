package net.bigshot.Statch22;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForageEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();

            if (isLog(state)) {
                player.awardStat(Statch22.LOGS_CHOPPED_STAT.get(), 1);
                ForageStatCalculator.addLogsChopped(player);
                updateForagingStatImmediately(player);
            }

            else if (isForageable(state)) {
                player.awardStat(Statch22.FORAGE_HARVESTED_STAT.get(), 1);
                ForageStatCalculator.addForageHarvested(player);
                updateForagingStatImmediately(player);
            }

            else if (isRareForageable(state)) {
                player.awardStat(Statch22.FORAGE_HARVESTED_STAT.get(), 1);
                ForageStatCalculator.addRareForage(player);
                updateForagingStatImmediately(player);
            }
        }
    }

    private boolean isLog(BlockState state) {
        return state.is(Blocks.OAK_LOG) || state.is(Blocks.SPRUCE_LOG) ||
                state.is(Blocks.BIRCH_LOG) || state.is(Blocks.JUNGLE_LOG) ||
                state.is(Blocks.ACACIA_LOG) || state.is(Blocks.DARK_OAK_LOG) ||
                state.is(Blocks.MANGROVE_LOG) || state.is(Blocks.CHERRY_LOG) ||
                state.is(Blocks.CRIMSON_STEM) || state.is(Blocks.WARPED_STEM);
    }

    private boolean isForageable(BlockState state) {
        return state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS) ||
                state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN) ||
                state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.SUGAR_CANE) ||
                state.is(Blocks.CACTUS) || state.is(Blocks.BAMBOO) ||
                state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT) ||
                state.is(Blocks.SEA_PICKLE) || state.is(Blocks.GLOW_LICHEN) ||
                state.is(Blocks.VINE) || state.is(Blocks.WEEPING_VINES) ||
                state.is(Blocks.TWISTING_VINES) || state.is(Blocks.CAVE_VINES) ||
                state.is(Blocks.CAVE_VINES_PLANT) || state.is(Blocks.MOSS_CARPET) ||
                state.is(Blocks.MOSS_BLOCK) || state.is(Blocks.AZALEA) ||
                state.is(Blocks.FLOWERING_AZALEA) || state.is(Blocks.SPORE_BLOSSOM) ||
                state.is(Blocks.HANGING_ROOTS);
    }

    private boolean isRareForageable(BlockState state) {
        return state.is(Blocks.BROWN_MUSHROOM) || state.is(Blocks.RED_MUSHROOM) ||
                state.is(Blocks.CRIMSON_FUNGUS) || state.is(Blocks.WARPED_FUNGUS) ||
                state.is(Blocks.CHORUS_PLANT) || state.is(Blocks.CHORUS_FLOWER) ||
                state.is(Blocks.COCOA) || state.is(Blocks.BEE_NEST) ||
                state.is(Blocks.BEEHIVE) || state.is(Blocks.SHROOMLIGHT) ||
                state.is(Blocks.SCULK_VEIN) || state.is(Blocks.SCULK);
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