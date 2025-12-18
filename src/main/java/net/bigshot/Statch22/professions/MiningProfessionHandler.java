package net.bigshot.Statch22.professions;

import net.bigshot.Statch22.PlayerSkillData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class MiningProfessionHandler {

    private static final List<net.minecraft.world.item.Item> GEMS = List.of(
            Items.DIAMOND,
            Items.EMERALD,
            Items.LAPIS_LAZULI,
            Items.QUARTZ,
            Items.AMETHYST_SHARD,
            Items.DIAMOND,
            Items.EMERALD
    );

    private static final List<net.minecraft.world.item.Item> METALS = List.of(
            Items.RAW_IRON,
            Items.RAW_GOLD,
            Items.RAW_COPPER,
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.COPPER_INGOT,
            Items.RAW_IRON_BLOCK,
            Items.RAW_GOLD_BLOCK,
            Items.RAW_COPPER_BLOCK
    );

    private static final List<net.minecraft.world.item.Item> ALL_ORES = List.of(
            Items.DIAMOND,
            Items.EMERALD,
            Items.LAPIS_LAZULI,
            Items.QUARTZ,
            Items.AMETHYST_SHARD,
            Items.DIAMOND,
            Items.DIAMOND,
            Items.RAW_IRON,
            Items.RAW_GOLD,
            Items.RAW_COPPER,
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.COPPER_INGOT,
            Items.COAL,
            Items.REDSTONE,
            Items.NETHERITE_SCRAP,
            Items.NETHERITE_INGOT
    );

    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide) return;
        if (event.isCanceled()) return;

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            int miningProfession = skillData.getProfession(5);

            if (miningProfession == 0) return;

            List<ItemStack> drops = Block.getDrops(
                    event.getState(),
                    (net.minecraft.server.level.ServerLevel) player.level(),
                    event.getPos(),
                    event.getLevel().getBlockEntity(event.getPos()),
                    player,
                    player.getMainHandItem()
            );

            if (drops.isEmpty()) return;

            boolean modified = false;
            Map<net.minecraft.world.item.Item, Integer> newCounts = new HashMap<>();

            for (ItemStack drop : drops) {
                net.minecraft.world.item.Item item = drop.getItem();
                int count = drop.getCount();
                int newCount = count;

                switch (miningProfession) {
                    case 2:
                        if (GEMS.contains(item)) {
                            newCount = (int) Math.floor(count * 1.5f);
                        }
                        break;
                    case 3:
                        if (ALL_ORES.contains(item)) {
                            newCount = (int) Math.floor(count * 1.75f);
                        }
                        break;
                    case 4:
                        break;
                    case 6:
                        if (GEMS.contains(item)) {
                            newCount = count * 2;
                        }
                        break;
                }

                if (newCount > count) {
                    newCounts.put(item, newCount);
                    modified = true;
                }
            }

            if (modified) {
                event.setCanceled(true);

                for (Map.Entry<net.minecraft.world.item.Item, Integer> entry : newCounts.entrySet()) {
                    ItemStack newStack = new ItemStack(entry.getKey(), entry.getValue());
                    Block.popResource(player.level(), event.getPos(), newStack);
                }

                for (ItemStack drop : drops) {
                    if (!newCounts.containsKey(drop.getItem())) {
                        Block.popResource(player.level(), event.getPos(), drop.copy());
                    }
                }
            }
        });
    }
}