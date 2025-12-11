package net.bigshot.Statch22;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class EventHandler {

    @SubscribeEvent
    public void onCropPlanted(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = event.getEntity();
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState clickedBlockState = level.getBlockState(pos);
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        boolean isPlanting = false;

        if (clickedBlockState.is(Blocks.FARMLAND) && isPlantableSeed(heldItem)) {
            isPlanting = true;
        }
        else if (clickedBlockState.is(Blocks.SOUL_SAND) && heldItem.is(Items.NETHER_WART)) {
            isPlanting = true;
        }
        else if ((clickedBlockState.is(Blocks.SAND) || clickedBlockState.is(Blocks.GRASS_BLOCK))
                && heldItem.is(Items.SUGAR_CANE)) {
            isPlanting = true;
        }
        else if (clickedBlockState.is(Blocks.SAND) && heldItem.is(Items.CACTUS)) {
            isPlanting = true;
        }

        if (isPlanting) {
            player.awardStat(Statch22.CROPS_PLANTED_STAT.get(), 1);
            StatCalculator.addCropsPlanted(player);
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        BlockState state = event.getState();

        if (isMatureCrop(state)) {
            player.awardStat(Statch22.CROPS_HARVESTED_STAT.get(), 1);
            StatCalculator.addCropsHarvested(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickHarvest(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = event.getEntity();
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!isMatureCrop(state)) {
            return;
        }

        if (heldItem.isEmpty()) {
            if (state.is(Blocks.WHEAT) || state.is(Blocks.BEETROOTS)) {
                player.awardStat(Statch22.CROPS_HARVESTED_STAT.get(), 1);
                StatCalculator.addCropsHarvested(player);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onAnimalBred(net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event) {
        if (event.getParentA() != null && event.getParentA().getLastHurtByMob() instanceof Player player) {
            StatCalculator.addAnimalsBred(player);
        }
    }
    
    @SubscribeEvent
    public void onUseItem(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        LevelAccessor level = event.getLevel();

        if (isPlantableSeed(item) && level instanceof ServerLevel) {
            player.awardStat(Statch22.CROPS_PLANTED_STAT.get(), 1);
            StatCalculator.addCropsPlanted(player);
        }
    }

    private boolean isCropBlock(net.minecraft.world.level.block.Block block) {
        return block instanceof CropBlock ||
                block == Blocks.BEETROOTS ||
                block == Blocks.NETHER_WART ||
                block == Blocks.MELON_STEM ||
                block == Blocks.PUMPKIN_STEM;
    }

    private boolean isPlantableSeed(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        return itemStack.is(Items.WHEAT_SEEDS) ||
                itemStack.is(Items.CARROT) ||
                itemStack.is(Items.POTATO) ||
                itemStack.is(Items.BEETROOT_SEEDS) ||
                itemStack.is(Items.MELON_SEEDS) ||
                itemStack.is(Items.PUMPKIN_SEEDS) ||
                itemStack.is(Items.NETHER_WART) ||
                itemStack.is(Items.SUGAR_CANE) ||
                itemStack.is(Items.CACTUS) ||
                itemStack.is(Items.BAMBOO);
    }

    private boolean isMatureCrop(BlockState state) {
        if (state.getBlock() instanceof CropBlock) {
            return ((CropBlock) state.getBlock()).isMaxAge(state);
        }

        if (state.is(Blocks.BEETROOTS)) {
            return state.getValue(BlockStateProperties.AGE_3) == 3;
        }

        if (state.is(Blocks.NETHER_WART)) {
            return state.getValue(NetherWartBlock.AGE) == 3;
        }

        if (state.hasProperty(BlockStateProperties.AGE_7)) {
            return state.getValue(BlockStateProperties.AGE_7) == 7;
        }

        return false;
    }
}