package net.bigshot.Statch22.professions;

import net.bigshot.Statch22.PlayerSkillData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MiningSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            int miningProfession = skillData.getProfession(5);

            float multiplier = 1.0f;

            switch (miningProfession) {
                case 1:
                    multiplier = 1.10f;
                    break;
                case 4:
                    multiplier = 1.25f;
                    break;
            }

            if (multiplier != 1.0f) {
                event.setNewSpeed(event.getNewSpeed() * multiplier);
            }
        });
    }
}