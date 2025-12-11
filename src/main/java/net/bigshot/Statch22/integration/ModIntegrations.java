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
        } else {
            LOGGER.info("Harvest with Ease not found, skipping integration");
        }
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
}