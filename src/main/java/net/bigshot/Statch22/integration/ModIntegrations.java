package net.bigshot.Statch22.integration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModIntegrations {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerIntegrations(IEventBus eventBus) {
        LOGGER.info("Statch22: Initializing mod integrations...");

        if (ModList.get().isLoaded("farmersdelight")) {
            LOGGER.info("Farmer's Delight detected, registering integration...");
            registerIntegration(eventBus, "farmersdelight", "FarmersDelightIntegration");
        }

        if (ModList.get().isLoaded("treechop")) {
            LOGGER.info("HT's TreeChop detected, registering integration...");
            registerIntegration(eventBus, "treechop", "TreeChopIntegration");
        }

        if (ModList.get().isLoaded("harvest_with_ease")) {
            LOGGER.info("Harvest with Ease detected, registering integration...");
            registerHarvestWithEase(eventBus);
        }

        LOGGER.info("Registering generic mod integration handler...");
        registerGenericHandler(eventBus);
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

    private static void registerIntegration(IEventBus eventBus, String modId, String className) {
        try {
            Class<?> integrationClass = Class.forName(
                    "net.bigshot.Statch22.integration." + modId + "." + className
            );
            Object integrationInstance = integrationClass.getDeclaredConstructor().newInstance();
            eventBus.register(integrationInstance);
            LOGGER.info("Successfully registered " + modId + " integration");
        } catch (ClassNotFoundException e) {
            LOGGER.warn(className + " integration class not found for " + modId);
        } catch (Exception e) {
            LOGGER.error("Failed to register " + modId + " integration: " + e.getMessage(), e);
        }
    }

    private static void registerGenericHandler(IEventBus eventBus) {
        try {
            Class<?> handlerClass = Class.forName(
                    "net.bigshot.Statch22.integration.GenericModHandler"
            );
            Object handlerInstance = handlerClass.getDeclaredConstructor().newInstance();
            eventBus.register(handlerInstance);
            LOGGER.info("Successfully registered generic mod handler");
        } catch (Exception e) {
            LOGGER.warn("Could not create generic mod handler: " + e.getMessage());
        }
    }
}