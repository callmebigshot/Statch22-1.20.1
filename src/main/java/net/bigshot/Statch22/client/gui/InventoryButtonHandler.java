package net.bigshot.Statch22.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "statch22", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryButtonHandler {
    private static final ResourceLocation INVENTORY_TAB_UNSELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/unselected1.png");
    private static final ResourceLocation INVENTORY_TAB_SELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/selected1.png");
    private static final ResourceLocation STATS_TAB_UNSELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/unselected2.png");
    private static final ResourceLocation STATS_TAB_SELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/selected2.png");

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen) {
            InventoryScreen screen = (InventoryScreen) event.getScreen();
            int tabX = screen.getGuiLeft();
            int tabY = screen.getGuiTop() - 28;

            ImageButton statsTab = new ImageButton(
                    tabX + 26, tabY, 26, 32,
                    0, 0, 0,
                    STATS_TAB_UNSELECTED,
                    26, 32,
                    button -> {
                        Minecraft.getInstance().setScreen(new StatsScreen());
                    },
                    net.minecraft.network.chat.Component.translatable("gui.statch22.tab.stats")
            ) {
                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    if (this.visible) {
                        ResourceLocation texture = STATS_TAB_UNSELECTED;
                        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height - 3, this.width, this.height);
                        guiGraphics.renderItem(Items.ENCHANTED_BOOK.getDefaultInstance(), this.getX() + 5, this.getY() + 8);
                    }
                }
            };

            event.addListener(statsTab);

            ImageButton inventoryTab = new ImageButton(
                    tabX, tabY, 26, 32,
                    0, 0, 0,
                    INVENTORY_TAB_SELECTED,
                    26, 32,
                    button -> {},
                    net.minecraft.network.chat.Component.translatable("container.inventory")
            ) {
                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    if (this.visible) {
                        ResourceLocation texture = INVENTORY_TAB_SELECTED;
                        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
                        guiGraphics.renderItem(Items.BUNDLE.getDefaultInstance(), this.getX() + 5, this.getY() + 8);
                    }
                }
            };

            event.addListener(inventoryTab);
        }
    }
}