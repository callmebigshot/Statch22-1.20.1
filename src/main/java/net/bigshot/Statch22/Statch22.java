package net.bigshot.Statch22;

import net.bigshot.Statch22.integration.ModIntegrations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(Statch22.MODID)
public class Statch22 {
    public static final String MODID = "statch22";

    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, MODID);

    public static final RegistryObject<ResourceLocation> CROPS_PLANTED_STAT =
            CUSTOM_STATS.register("crops_planted",
                    () -> new ResourceLocation(MODID, "crops_planted"));

    public static final RegistryObject<ResourceLocation> CROPS_HARVESTED_STAT =
            CUSTOM_STATS.register("crops_harvested",
                    () -> new ResourceLocation(MODID, "crops_harvested"));

    public static final RegistryObject<ResourceLocation> FARMING_STAT =
            CUSTOM_STATS.register("farming",
                    () -> new ResourceLocation(MODID, "farming"));

    public Statch22() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        CUSTOM_STATS.register(modEventBus);
        forgeEventBus.register(new EventHandler());
        ModIntegrations.registerIntegrations(forgeEventBus);
        forgeEventBus.addListener(this::onPlayerTick);
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            int currentScore = StatCalculator.getScore(event.player);
            int lastScore = StatCalculator.getLastScore(event.player);

            if (currentScore > lastScore) {
                int difference = currentScore - lastScore;
                event.player.awardStat(Stats.CUSTOM.get(FARMING_STAT.get()), difference);
                StatCalculator.setLastScore(event.player, currentScore);
            }
        }
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Stats.CUSTOM.get(CROPS_PLANTED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(CROPS_HARVESTED_STAT.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(FARMING_STAT.get(), StatFormatter.DEFAULT);
        });
    }
}