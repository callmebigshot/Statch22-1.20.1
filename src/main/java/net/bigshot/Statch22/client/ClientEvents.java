package net.bigshot.Statch22.client;

import net.bigshot.Statch22.client.gui.StatsScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getKey() == GLFW.GLFW_KEY_K && event.getAction() == GLFW.GLFW_PRESS) {
            if (Minecraft.getInstance().screen == null) {
                Minecraft.getInstance().setScreen(new StatsScreen());
            }
        }
    }
}