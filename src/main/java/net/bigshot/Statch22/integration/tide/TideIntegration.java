package net.bigshot.Statch22.integration.tide;

import net.bigshot.Statch22.Statch22;
import net.bigshot.Statch22.Statch22Config;
import net.bigshot.Statch22.statistics.FishingStatCalculator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = Statch22.MODID)
public class TideIntegration {

    private static Method getFishStrengthMethod = null;
    private static Class<?> tideFishClass = null;

    static {
        if (ModList.get().isLoaded("tide")) {
            try {
                tideFishClass = Class.forName("com.li64.tide.registries.items.TideFishItem");
                for (Method method : tideFishClass.getMethods()) {
                    if (method.getReturnType() == float.class &&
                            method.getParameterCount() == 0 &&
                            (method.getName().toLowerCase().contains("strength") ||
                                    method.getName().toLowerCase().contains("weight"))) {
                        getFishStrengthMethod = method;
                        break;
                    }
                }

                if (getFishStrengthMethod == null) {
                    try {
                        java.lang.reflect.Field strengthField = tideFishClass.getDeclaredField("strength");
                        strengthField.setAccessible(true);
                        getFishStrengthMethod = tideFishClass.getMethod("getStrength"); // Try common getter pattern
                    } catch (Exception e) {
                        Statch22.LOGGER.warn("Could not find strength method/field in TideFishItem");
                    }
                }
            } catch (ClassNotFoundException e) {
                Statch22.LOGGER.warn("Tide mod classes not found");
            }
        }
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        if (!ModList.get().isLoaded("tide")) {
            return;
        }

        Player player = event.getEntity();
        ItemStack caughtItem = event.getDrops().isEmpty() ? ItemStack.EMPTY : event.getDrops().get(0);

        if (caughtItem.isEmpty() || !isTideFish(caughtItem)) {
            return;
        }

        float fishStrength = getFishStrength(caughtItem);
        boolean isEpicRarity = isEpicRarity(caughtItem);

        if (player instanceof ServerPlayer serverPlayer) {
            awardTideFishStats(serverPlayer, fishStrength, isEpicRarity);
        }
    }

    private static boolean isTideFish(ItemStack stack) {
        if (stack.isEmpty() || !stack.getItemHolder().unwrapKey().isPresent()) {
            return false;
        }

        String namespace = stack.getItemHolder().unwrapKey().get().location().getNamespace();
        if (!namespace.equals("tide")) {
            return false;
        }

        if (tideFishClass != null && tideFishClass.isInstance(stack.getItem())) {
            return true;
        }

        return false;
    }

    private static float getFishStrength(ItemStack stack) {
        if (getFishStrengthMethod != null && tideFishClass != null && tideFishClass.isInstance(stack.getItem())) {
            try {
                Object result = getFishStrengthMethod.invoke(stack.getItem());
                if (result instanceof Float) {
                    return (Float) result;
                } else if (result instanceof Double) {
                    return ((Double) result).floatValue();
                }
            } catch (Exception e) {
                Statch22.LOGGER.error("Failed to get fish strength via reflection", e);
            }
        }

        String path = stack.getItemHolder().unwrapKey().get().location().getPath();

        if (path.contains("midas") || path.contains("voidseeker") || path.contains("starfish") ||
                path.contains("elytrout") || path.contains("bedrock") || path.contains("aquathorn") ||
                path.contains("windbass")) {
            return 7.5f;
        }

        if (path.contains("crystalline") || path.contains("abyss") || path.contains("purpur") ||
                path.contains("chorus") || path.contains("ender") || path.contains("inferno") ||
                path.contains("volcano") || path.contains("obsidian") || path.contains("wither") ||
                path.contains("soul")) {
            return 3.0f;
        }

        if (path.contains("deep") || path.contains("shadow") || path.contains("lapis") ||
                path.contains("luminescent") || path.contains("crystal") || path.contains("iron") ||
                path.contains("glow") || path.contains("angler") || path.contains("cave")) {
            return 1.5f;
        }

        return 0.5f;
    }

    private static boolean isEpicRarity(ItemStack stack) {
        return stack.getRarity() == Rarity.EPIC;
    }

    private static void awardTideFishStats(ServerPlayer player, float fishStrength, boolean isEpicRarity) {
        int strengthBonus = 0;

        if (isEpicRarity) {
            strengthBonus = 10;
        } else if (fishStrength >= 5.0f) {
            strengthBonus = 8;
        } else if (fishStrength >= 3.0f) {
            strengthBonus = 5;
        } else if (fishStrength >= 1.5f) {
            strengthBonus = 3;
        } else if (fishStrength >= 1.0f) {
            strengthBonus = 1;
        }

        if (strengthBonus > 0) {
            int lastBefore = FishingStatCalculator.getLastFishingScore(player);

            FishingStatCalculator.addFishCaught(player, strengthBonus);

            int currentAfter = FishingStatCalculator.getFishingScore(player);

            int statIncrease = currentAfter - lastBefore;
            if (statIncrease > 0) {
                player.awardStat(Stats.CUSTOM.get(Statch22.FISHING_STAT.get()), statIncrease);
                FishingStatCalculator.setLastFishingScore(player, currentAfter);
            }
        }
    }
}