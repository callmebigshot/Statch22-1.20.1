package net.bigshot.Statch22;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MiningEventHandler {

    @SubscribeEvent
    public void onBlockMined(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BlockState state = event.getState();
            Block block = state.getBlock();
            BlockPos pos = event.getPos();

            int weight = getMiningWeight(block, pos.getY());
            if (weight > 0) {
                if (isOre(block)) {
                    player.awardStat(Statch22.ORES_MINED_STAT.get(), 1);
                } else {
                    player.awardStat(Statch22.STONE_MINED_STAT.get(), 1);
                }

                MiningStatCalculator.addOreMined(player, weight);
                updateMiningStatImmediately(player);
            }
        }
    }

    private boolean isOre(Block block) {
        return block.defaultBlockState().is(tag("forge:ores")) ||
                block.defaultBlockState().is(tag("forge:ores/coal")) ||
                block.defaultBlockState().is(tag("forge:ores/copper")) ||
                block.defaultBlockState().is(tag("forge:ores/iron")) ||
                block.defaultBlockState().is(tag("forge:ores/gold")) ||
                block.defaultBlockState().is(tag("forge:ores/diamond")) ||
                block.defaultBlockState().is(tag("forge:ores/emerald")) ||
                block.defaultBlockState().is(tag("forge:ores/lapis")) ||
                block.defaultBlockState().is(tag("forge:ores/redstone")) ||
                block.defaultBlockState().is(tag("forge:ores/quartz")) ||
                block.defaultBlockState().is(tag("forge:ores/netherite_scrap")) ||
                block.defaultBlockState().is(tag("forge:ores/ancient_debris"));
    }

    private int getMiningWeight(Block block, int yLevel) {
        if (block.defaultBlockState().is(tag("forge:ores/netherite_scrap")) ||
                block.defaultBlockState().is(tag("forge:ores/ancient_debris"))) {
            return 7;
        }

        if (block.defaultBlockState().is(tag("forge:ores/diamond")) ||
                block.defaultBlockState().is(tag("forge:ores/emerald"))) {
            return 5;
        }

        if (block.defaultBlockState().is(tag("forge:ores/gold")) ||
                block.defaultBlockState().is(tag("forge:ores/redstone"))) {
            return 4;
        }

        if (block.defaultBlockState().is(tag("forge:ores/iron")) ||
                block.defaultBlockState().is(tag("forge:ores/lapis")) ||
                block.defaultBlockState().is(tag("forge:ores/quartz"))) {
            return 3;
        }

        if (block.defaultBlockState().is(tag("forge:ores/coal")) ||
                block.defaultBlockState().is(tag("forge:ores/copper"))) {
            return 2;
        }

        if (block.defaultBlockState().is(tag("forge:ores"))) {
            int baseWeight = 3;
            if (yLevel < 0) baseWeight += 1;
            if (yLevel < -40) baseWeight += 1;
            return baseWeight;
        }

        if (block.defaultBlockState().is(tag("forge:stone")) ||
                block.defaultBlockState().is(tag("forge:cobblestone")) ||
                block.defaultBlockState().is(BlockTags.BASE_STONE_OVERWORLD) ||
                block.defaultBlockState().is(BlockTags.BASE_STONE_NETHER)) {
            return 1;
        }

        if (block == Blocks.END_STONE) {
            return 2;
        }

        return 0;
    }

    private TagKey<Block> tag(String path) {
        return BlockTags.create(ResourceLocation.parse(path));
    }

    private void updateMiningStatImmediately(Player player) {
        int current = MiningStatCalculator.getMiningScore(player);
        int last = MiningStatCalculator.getLastMiningScore(player);
        if (current > last) {
            player.awardStat(net.minecraft.stats.Stats.CUSTOM.get(Statch22.MINING_STAT.get()), current - last);
            MiningStatCalculator.setLastMiningScore(player, current);
        }
    }
}