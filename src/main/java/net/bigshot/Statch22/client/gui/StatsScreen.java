package net.bigshot.Statch22.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bigshot.Statch22.Statch22;
import net.bigshot.Statch22.client.ClientSkillData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class StatsScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Statch22.MODID, "textures/gui/stats_gui.png");
    private static final ResourceLocation INVENTORY_TAB_UNSELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/unselected1.png");
    private static final ResourceLocation INVENTORY_TAB_SELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/selected1.png");
    private static final ResourceLocation STATS_TAB_UNSELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/unselected2.png");
    private static final ResourceLocation STATS_TAB_SELECTED = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/selected2.png");
    private static final ResourceLocation LEVEL_POINTS = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/level_points.png");
    private static final ResourceLocation MASTERY_POINT = ResourceLocation.fromNamespaceAndPath("statch22", "textures/gui/mastery_point.png");

    private int leftPos;
    private int topPos;
    private final int imageWidth = 176;
    private final int imageHeight = 166;

    public StatsScreen() {
        super(Component.translatable("gui.statch22.stats"));
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        int tabX = this.leftPos;
        int tabY = this.topPos - 28;

        guiGraphics.blit(INVENTORY_TAB_UNSELECTED, tabX, tabY, 0, 0, 26, 29, 26, 32);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        guiGraphics.blit(STATS_TAB_SELECTED, tabX + 26, tabY, 0, 0, 26, 32, 26, 32);

        guiGraphics.renderItem(Items.ENCHANTED_BOOK.getDefaultInstance(), tabX + 26 + 5, tabY + 8);
        guiGraphics.renderItem(Items.BUNDLE.getDefaultInstance(), tabX + 5, tabY + 8);

        guiGraphics.drawString(this.font, "Skills", this.leftPos + 6, this.topPos + 6, 0xFFFFFF, false);

        int baseX = this.leftPos + 19;
        int baseY = this.topPos + 16;

        String[] skills = {"Mining", "Building", "Combat", "Fishing", "Farming", "Foraging"};
        int[] skillIds = {5, 0, 1, 3, 2, 4};

        for (int i = 0; i < skills.length; i++) {
            guiGraphics.drawString(this.font, skills[i], baseX, baseY + (i * 12), 0x404040, false);
        }

        guiGraphics.drawString(this.font, "Mastery", this.leftPos + 17, this.topPos + 103, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, "Professions", this.leftPos + 67, this.topPos + 103, 0xFFFFFF, false);

        int[] rowYs = {17, 29, 41, 53, 65, 77};
        int[] masteryX = {this.leftPos + 31, this.leftPos + 45, this.leftPos + 45, this.leftPos + 31, this.leftPos + 17, this.leftPos + 17};
        int[] masteryY = {this.topPos + 116, this.topPos + 123, this.topPos + 137, this.topPos + 144, this.topPos + 137, this.topPos + 123};

        RenderSystem.setShaderTexture(0, LEVEL_POINTS);

        for (int row = 0; row < 6; row++) {
            int skillId = skillIds[row];
            int level = ClientSkillData.getSkillLevel(skillId);
            int y = this.topPos + rowYs[row];
            renderLevelRow(guiGraphics, this.leftPos, y, level);

            if (level == 11) {
                RenderSystem.setShaderTexture(0, MASTERY_POINT);
                guiGraphics.blit(MASTERY_POINT, masteryX[row], masteryY[row], 0, 0, 9, 9, 9, 9);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.addRenderableWidget(new ImageButton(
                tabX, tabY, 26, 32,
                0, 0, 0,
                INVENTORY_TAB_UNSELECTED,
                26, 32,
                button -> {
                    Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
                },
                Component.translatable("container.inventory")
        ) {
            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            }
        });

        this.addRenderableWidget(new ImageButton(
                tabX + 26, tabY, 26, 32,
                0, 0, 0,
                STATS_TAB_SELECTED,
                26, 32,
                button -> {},
                Component.translatable("gui.statch22.tab.stats")
        ) {
            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            }
        });
    }

    private void renderLevelRow(GuiGraphics guiGraphics, int x, int y, int level) {
        int[] pointX = {65, 74, 83, 92, 101, 113, 122, 131, 140, 149};
        for (int i = 0; i < level && i < 10; i++) {
            boolean big = (i == 4 || i == 9);
            guiGraphics.blit(LEVEL_POINTS, x + pointX[i], y, 0, big ? 9 : 0, big ? 9 : 6, 6, 16, 16);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }
}