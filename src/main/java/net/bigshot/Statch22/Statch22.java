package net.bigshot.Statch22;

import net.bigshot.Statch22.integration.ModIntegrations;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.bigshot.Statch22.FarmingQualityEventHandler;

@Mod(Statch22.MODID)
public class Statch22 {
    public static final String MODID = "statch22";

    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, MODID);

    public static final RegistryObject<ResourceLocation> CROPS_PLANTED_STAT =
            CUSTOM_STATS.register("crops_planted",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "crops_planted"));

    public static final RegistryObject<ResourceLocation> CROPS_HARVESTED_STAT =
            CUSTOM_STATS.register("crops_harvested",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "crops_harvested"));

    public static final RegistryObject<ResourceLocation> FARMING_STAT =
            CUSTOM_STATS.register("farming",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "farming"));

    public static final RegistryObject<ResourceLocation> COMBAT_STAT =
            CUSTOM_STATS.register("combat",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "combat"));

    public static final RegistryObject<ResourceLocation> FORAGE_HARVESTED_STAT =
            CUSTOM_STATS.register("forage_harvested",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "forage_harvested"));

    public static final RegistryObject<ResourceLocation> LOGS_CHOPPED_STAT =
            CUSTOM_STATS.register("logs_chopped",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "logs_chopped"));

    public static final RegistryObject<ResourceLocation> FORAGING_STAT =
            CUSTOM_STATS.register("foraging",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "foraging"));

    public static final RegistryObject<ResourceLocation> ORES_MINED_STAT =
            CUSTOM_STATS.register("ores_mined",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "ores_mined"));

    public static final RegistryObject<ResourceLocation> STONE_MINED_STAT =
            CUSTOM_STATS.register("stone_mined",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "stone_mined"));

    public static final RegistryObject<ResourceLocation> MINING_STAT =
            CUSTOM_STATS.register("mining",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "mining"));

    public static final RegistryObject<ResourceLocation> BLOCKS_PLACED_STAT =
            CUSTOM_STATS.register("blocks_placed",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "blocks_placed"));

    public static final RegistryObject<ResourceLocation> FISHING_STAT =
            CUSTOM_STATS.register("fishing",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "fishing"));

    public static final RegistryObject<ResourceLocation> BUILDING_STAT =
            CUSTOM_STATS.register("building",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "building"));

    public static final RegistryObject<ResourceLocation> SILVER_CROPS_HARVESTED_STAT =
            CUSTOM_STATS.register("silver_crops_harvested",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "silver_crops_harvested"));

    public static final RegistryObject<ResourceLocation> GOLD_CROPS_HARVESTED_STAT =
            CUSTOM_STATS.register("gold_crops_harvested",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "gold_crops_harvested"));

    public static final RegistryObject<ResourceLocation> DIAMOND_CROPS_HARVESTED_STAT =
            CUSTOM_STATS.register("diamond_crops_harvested",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "diamond_crops_harvested"));

    public static final RegistryObject<ResourceLocation> ENCHANTMENTS_APPLIED_STAT =
            CUSTOM_STATS.register("enchantments_applied",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "enchantments_applied"));

    public static final RegistryObject<ResourceLocation> ENCHANTING_STAT =
            CUSTOM_STATS.register("enchanting",
                    () -> ResourceLocation.fromNamespaceAndPath(MODID, "enchanting"));

    public Statch22() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        CUSTOM_STATS.register(modEventBus);
        forgeEventBus.register(new EventHandler());
        forgeEventBus.register(new CombatEventHandler());
        forgeEventBus.register(new ForageEventHandler());
        forgeEventBus.register(new MiningEventHandler());
        forgeEventBus.register(new FishingEventHandler());
        forgeEventBus.register(new BuildingEventHandler());
        forgeEventBus.register(new FarmingQualityEventHandler());
        forgeEventBus.register(new EnchantingEventHandler());
        ModIntegrations.registerIntegrations(forgeEventBus);
        modEventBus.addListener(this::onCommonSetup);

        Statch22Config.register();
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Stats.CUSTOM.get(CROPS_PLANTED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(CROPS_HARVESTED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(FARMING_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(COMBAT_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(FORAGE_HARVESTED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(LOGS_CHOPPED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(FORAGING_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(ORES_MINED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(STONE_MINED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(MINING_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(BLOCKS_PLACED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(FISHING_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(BUILDING_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(ENCHANTMENTS_APPLIED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(ENCHANTING_STAT.get(), StatFormatter.DEFAULT);
        });
    }
}