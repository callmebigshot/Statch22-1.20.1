package net.bigshot.Statch22;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class SleepProfessionHandler {
    private static final Map<String, String> pendingSelections = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            for (int skillId = 0; skillId < 6; skillId++) {
                if (skillData.hasPendingLevelUp(skillId)) {
                    int stat = getStatForSkill(player, skillId);
                    int newLevel = LevelManager.getLevelFromStat(stat);

                    if (newLevel == 5 || newLevel == 10) {
                        String skillName = LevelManager.getSkillName(skillId);
                        offerProfessionChoice(player, skillName, newLevel, skillId);
                        return;
                    }
                }
            }
        });
    }

    private static void offerProfessionChoice(Player player, String skillName, int level, int skillId) {
        String playerKey = player.getStringUUID() + "_" + skillName;
        pendingSelections.put(playerKey, skillName);

        String formattedSkill = skillName.substring(0, 1).toUpperCase() + skillName.substring(1);

        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal("§6=== " + formattedSkill + " Level " + level + " Profession ==="));
        player.sendSystemMessage(Component.literal("§7Choose your specialty:"));

        if (level == 5) {
            player.sendSystemMessage(createClickableOption(1, "Miner", "Mining speed increased by 10%", skillName));
            player.sendSystemMessage(createClickableOption(2, "Geologist", "Gem drops increased by 50%", skillName));
        } else if (level == 10) {
            player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
                int currentProfession = skillData.getProfession(skillId);

                if (currentProfession == 1) {
                    player.sendSystemMessage(createClickableOption(3, "Blacksmith", "Metal yields increased by 100%", skillName));
                    player.sendSystemMessage(createClickableOption(4, "Excavator", "Mining speed increased by 25%", skillName));
                } else if (currentProfession == 2) {
                    player.sendSystemMessage(createClickableOption(5, "Prospector", "All ore drops increased by 75%", skillName));
                    player.sendSystemMessage(createClickableOption(6, "Gemologist", "Gem drops doubled (100% increase)", skillName));
                }
            });
        }

        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal("§7Click your choice above, or use:"));
        player.sendSystemMessage(Component.literal("§e/statch22 professions " + skillName + " <number>"));
    }

    private static Component createClickableOption(int id, String name, String description, String skillName) {
        return Component.literal("  §e[" + id + "] §b" + name + "§7: " + description)
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/statch22 professions " + skillName + " " + id
                        ))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§aClick to choose " + name)
                        ))
                );
    }

    private static int getStatForSkill(Player player, int skillId) {
        String skillName = LevelManager.getSkillName(skillId);
        if (player.level().isClientSide) return 0;

        net.minecraft.server.level.ServerPlayer serverPlayer = (net.minecraft.server.level.ServerPlayer) player;
        net.minecraft.stats.Stat<net.minecraft.resources.ResourceLocation> stat;
        net.minecraft.resources.ResourceLocation statLocation;

        switch (skillName) {
            case "mining":
                statLocation = Statch22.MINING_STAT.get();
                break;
            case "farming":
                statLocation = Statch22.FARMING_STAT.get();
                break;
            case "combat":
                statLocation = Statch22.COMBAT_STAT.get();
                break;
            case "fishing":
                statLocation = Statch22.FISHING_STAT.get();
                break;
            case "forage":
                statLocation = Statch22.FORAGING_STAT.get();
                break;
            case "building":
                statLocation = Statch22.BUILDING_STAT.get();
                break;
            default:
                return 0;
        }

        stat = net.minecraft.stats.Stats.CUSTOM.get(statLocation);
        return serverPlayer.getStats().getValue(stat);
    }

    public static void clearPendingSelection(Player player, String skillName) {
        String key = player.getStringUUID() + "_" + skillName;
        pendingSelections.remove(key);

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            int skillId = LevelManager.getSkillId(skillName);
            skillData.setPendingLevelUp(skillId, false);
        });
    }
}