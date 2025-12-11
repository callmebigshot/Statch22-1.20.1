package net.bigshot.Statch22.integration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModIntegrations {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerIntegrations(IEventBus eventBus) {
        LOGGER.info("Statch22: Initializing mod integrations...");

        if (ModList.get().isLoaded("harvest_with_ease")) {
            LOGGER.info("Harvest with Ease detected, registering integration...");
            registerHarvestWithEase(eventBus);
        }

        if (ModList.get().isLoaded("treechop")) {
            LOGGER.info("TreeChop detected, registering integration...");
            registerTreeChop(eventBus);
        }

        if (ModList.get().isLoaded("sereneseasons")) {
            LOGGER.info("Serene Seasons detected, registering integration...");
            registerSereneSeasons(eventBus);
        }

        if (ModList.get().isLoaded("pamhc2crops")) {
            LOGGER.info("Pam's HarvestCraft 2 Crops detected, registering integration...");
            registerPamsHarvestCraft(eventBus);
        }

        if (ModList.get().isLoaded("farmingforblockheads")) {
            LOGGER.info("Farming for Blockheads detected, registering integration...");
            registerFarmingForBlockheads(eventBus);
        }

        if (ModList.get().isLoaded("mysticalagriculture")) {
            LOGGER.info("Mystical Agriculture detected, registering integration...");
            registerMysticalAgriculture(eventBus);
        }

        if (ModList.get().isLoaded("iceandfire")) {
            LOGGER.info("Ice and Fire detected, registering integration...");
            registerIceAndFire(eventBus);
        }

        if (ModList.get().isLoaded("alexsmobs")) {
            LOGGER.info("Alex's Mobs detected, registering integration...");
            registerAlexMobs(eventBus);
        }

        if (ModList.get().isLoaded("twilightforest")) {
            LOGGER.info("Twilight Forest detected, registering integration...");
            registerTwilightForest(eventBus);
        }

        if (ModList.get().isLoaded("mutantmonsters")) {
            LOGGER.info("Mutant Monsters detected, registering integration...");
            registerMutantMonsters(eventBus);
        }

        if (ModList.get().isLoaded("biomesoplenty")) {
            LOGGER.info("Biomes O' Plenty detected, registering integration...");
            registerBiomesOPlenty(eventBus);
        }

        if (ModList.get().isLoaded("terralith")) {
            LOGGER.info("Terralith detected, registering integration...");
            registerTerralith(eventBus);
        }

        LOGGER.info("Registering generic mod integration handler...");
        registerGenericModHandler(eventBus);
    }

    private static void registerHarvestWithEase(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.harvestwithease.HarvestWithEaseIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Harvest with Ease integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Harvest with Ease integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Harvest with Ease integration: " + e.getMessage(), e);
        }
    }

    private static void registerTreeChop(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.treechop.TreeChopIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered TreeChop integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("TreeChop integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register TreeChop integration: " + e.getMessage(), e);
        }
    }

    private static void registerSereneSeasons(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.sereneseasons.SereneSeasonsIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Serene Seasons integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Serene Seasons integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Serene Seasons integration: " + e.getMessage(), e);
        }
    }

    private static void registerPamsHarvestCraft(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.pamsharvestcraft.PamsHarvestCraftIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Pam's HarvestCraft integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Pam's HarvestCraft integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Pam's HarvestCraft integration: " + e.getMessage(), e);
        }
    }

    private static void registerFarmingForBlockheads(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.farmingforblockheads.FarmingForBlockheadsIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Farming for Blockheads integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Farming for Blockheads integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Farming for Blockheads integration: " + e.getMessage(), e);
        }
    }

    private static void registerMysticalAgriculture(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.mysticalagriculture.MysticalAgricultureIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Mystical Agriculture integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Mystical Agriculture integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Mystical Agriculture integration: " + e.getMessage(), e);
        }
    }

    private static void registerIceAndFire(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.iceandfire.IceAndFireIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Ice and Fire integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Ice and Fire integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Ice and Fire integration: " + e.getMessage(), e);
        }
    }

    private static void registerAlexMobs(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.alexsmobs.AlexMobsIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Alex's Mobs integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Alex's Mobs integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Alex's Mobs integration: " + e.getMessage(), e);
        }
    }

    private static void registerTwilightForest(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.twilightforest.TwilightForestIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Twilight Forest integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Twilight Forest integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Twilight Forest integration: " + e.getMessage(), e);
        }
    }

    private static void registerMutantMonsters(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.mutantmonsters.MutantMonstersIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Mutant Monsters integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Mutant Monsters integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Mutant Monsters integration: " + e.getMessage(), e);
        }
    }

    private static void registerBiomesOPlenty(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.biomesoplenty.BiomesOPlentyIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Biomes O' Plenty integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Biomes O' Plenty integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Biomes O' Plenty integration: " + e.getMessage(), e);
        }
    }

    private static void registerTerralith(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.terralith.TerralithIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered Terralith integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Terralith integration class not found.");
        } catch (Exception e) {
            LOGGER.error("Failed to register Terralith integration: " + e.getMessage(), e);
        }
    }

    private static void registerGenericModHandler(IEventBus eventBus) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration.generic.GenericModIntegration"
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered generic mod integration handler");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Generic mod integration class not found, creating default...");
            registerGenericModHandlerViaReflection(eventBus);
        } catch (Exception e) {
            LOGGER.error("Failed to register generic mod integration: " + e.getMessage(), e);
        }
    }

    private static void registerGenericModHandlerViaReflection(IEventBus eventBus) {
        try {
            Class<?> handlerClass = Class.forName(
                    "net.bigshot.Statch22.integration.GenericModHandler"
            );
            Object handlerInstance = handlerClass.getDeclaredConstructor().newInstance();
            eventBus.register(handlerInstance);
            LOGGER.info("Created generic mod handler via reflection");
        } catch (Exception e) {
            LOGGER.warn("Could not create generic mod handler: " + e.getMessage());
        }
    }
}