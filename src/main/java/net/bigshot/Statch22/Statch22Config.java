package net.bigshot.Statch22;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Statch22Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_FARMING;
    public static final ForgeConfigSpec.IntValue FARMING_BASE_POINTS;
    public static final ForgeConfigSpec.IntValue FARMING_BREEDING_BONUS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_COMBAT;
    public static final ForgeConfigSpec.IntValue COMBAT_KILL_BASE;
    public static final ForgeConfigSpec.IntValue COMBAT_PLAYER_KILL_BONUS;
    public static final ForgeConfigSpec.DoubleValue COMBAT_DAMAGE_MULTIPLIER;

    public static final ForgeConfigSpec.BooleanValue ENABLE_FORAGING;
    public static final ForgeConfigSpec.IntValue FORAGING_LOG_POINTS;
    public static final ForgeConfigSpec.IntValue FORAGING_RARE_POINTS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_MINING;
    public static final ForgeConfigSpec.IntValue MINING_COAL_POINTS;
    public static final ForgeConfigSpec.IntValue MINING_IRON_POINTS;
    public static final ForgeConfigSpec.IntValue MINING_DIAMOND_POINTS;
    public static final ForgeConfigSpec.IntValue MINING_NETHERITE_POINTS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_FISHING;
    public static final ForgeConfigSpec.IntValue FISHING_BASE_POINTS;
    public static final ForgeConfigSpec.IntValue FISHING_LEGENDARY_BONUS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_BUILDING;
    public static final ForgeConfigSpec.IntValue BUILDING_BASE_POINTS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_ENCHANTING;
    public static final ForgeConfigSpec.IntValue ENCHANTING_BASE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue ENCHANTING_XP_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue ENCHANTING_COUNT_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue ENCHANTING_RARITY_MULTIPLIER;

    static {
        BUILDER.push("Farming");
        ENABLE_FARMING = BUILDER.define("enableFarming", true);
        FARMING_BASE_POINTS = BUILDER.defineInRange("farmingBasePoints", 1, 0, 100);
        FARMING_BREEDING_BONUS = BUILDER.defineInRange("farmingBreedingBonus", 3, 0, 100);
        BUILDER.pop();

        BUILDER.push("Combat");
        ENABLE_COMBAT = BUILDER.define("enableCombat", true);
        COMBAT_KILL_BASE = BUILDER.defineInRange("combatKillBase", 1, 0, 100);
        COMBAT_PLAYER_KILL_BONUS = BUILDER.defineInRange("combatPlayerKillBonus", 10, 0, 100);
        COMBAT_DAMAGE_MULTIPLIER = BUILDER.defineInRange("combatDamageMultiplier", 0.01, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("Foraging");
        ENABLE_FORAGING = BUILDER.define("enableForaging", true);
        FORAGING_LOG_POINTS = BUILDER.defineInRange("foragingLogPoints", 1, 0, 100);
        FORAGING_RARE_POINTS = BUILDER.defineInRange("foragingRarePoints", 5, 0, 100);
        BUILDER.pop();

        BUILDER.push("Mining");
        ENABLE_MINING = BUILDER.define("enableMining", true);
        MINING_COAL_POINTS = BUILDER.defineInRange("miningCoalPoints", 2, 0, 100);
        MINING_IRON_POINTS = BUILDER.defineInRange("miningIronPoints", 3, 0, 100);
        MINING_DIAMOND_POINTS = BUILDER.defineInRange("miningDiamondPoints", 5, 0, 100);
        MINING_NETHERITE_POINTS = BUILDER.defineInRange("miningNetheritePoints", 7, 0, 100);
        BUILDER.pop();

        BUILDER.push("Fishing");
        ENABLE_FISHING = BUILDER.define("enableFishing", true);
        FISHING_BASE_POINTS = BUILDER.defineInRange("fishingBasePoints", 1, 0, 100);
        FISHING_LEGENDARY_BONUS = BUILDER.defineInRange("fishingLegendaryBonus", 10, 0, 100);
        BUILDER.pop();

        BUILDER.push("Building");
        ENABLE_BUILDING = BUILDER.define("enableBuilding", true);
        BUILDING_BASE_POINTS = BUILDER.defineInRange("buildingBasePoints", 1, 0, 100);
        BUILDER.pop();

        BUILDER.push("Enchanting");
        ENABLE_ENCHANTING = BUILDER.define("enableEnchanting", true);
        ENCHANTING_BASE_MULTIPLIER = BUILDER.defineInRange("enchantingBaseMultiplier", 1, 1, 10);
        ENCHANTING_XP_MULTIPLIER = BUILDER.defineInRange("enchantingXPMultiplier", 2, 1, 20);
        ENCHANTING_COUNT_MULTIPLIER = BUILDER.defineInRange("enchantingCountMultiplier", 5, 1, 20);
        ENCHANTING_RARITY_MULTIPLIER = BUILDER.defineInRange("enchantingRarityMultiplier", 3, 1, 20);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "statch22-skills.toml");
    }
}