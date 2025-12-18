package net.bigshot.Statch22;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class Statch22Commands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("statch22")
                .then(Commands.literal("levels")
                        .executes(context -> showLevels(context.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> showLevels(EntityArgument.getPlayer(context, "player")))
                        )
                )
                .then(Commands.literal("test")
                        .executes(context -> testStats(context.getSource().getPlayerOrException()))
                )
                .then(Commands.literal("testcap")
                        .executes(context -> testCapability(context.getSource().getPlayerOrException()))
                )
                .then(Commands.literal("debugprof")
                        .executes(context -> debugProfession(context.getSource().getPlayerOrException()))
                )
                .then(Commands.literal("levelup")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("building");
                                    builder.suggest("combat");
                                    builder.suggest("farming");
                                    builder.suggest("fishing");
                                    builder.suggest("forage");
                                    builder.suggest("mining");
                                    builder.suggest("all");
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 11))
                                        .executes(context -> levelUpSkill(
                                                context.getSource().getPlayerOrException(),
                                                StringArgumentType.getString(context, "skill"),
                                                IntegerArgumentType.getInteger(context, "level")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("professions")
                        .executes(context -> showProfessions(context.getSource().getPlayerOrException()))
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("farming");
                                    builder.suggest("mining");
                                    builder.suggest("combat");
                                    builder.suggest("fishing");
                                    builder.suggest("forage");
                                    builder.suggest("building");
                                    return builder.buildFuture();
                                })
                                .executes(context -> showProfessionsForSkill(
                                        context.getSource().getPlayerOrException(),
                                        StringArgumentType.getString(context, "skill")
                                ))
                        )
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .then(Commands.argument("profession", IntegerArgumentType.integer())
                                        .executes(context -> chooseProfession(
                                                context.getSource().getPlayerOrException(),
                                                StringArgumentType.getString(context, "skill"),
                                                IntegerArgumentType.getInteger(context, "profession")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("pending")
                        .executes(context -> showPendingLevelUps(context.getSource().getPlayerOrException()))
                )
        );
    }

    private static int showLevels(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6=== Statch22 Skill Levels ==="));

        int miningStat = getDirectStat(player, Statch22.MINING_STAT.get());
        int farmingStat = getDirectStat(player, Statch22.FARMING_STAT.get());
        int combatStat = getDirectStat(player, Statch22.COMBAT_STAT.get());
        int fishingStat = getDirectStat(player, Statch22.FISHING_STAT.get());
        int foragingStat = getDirectStat(player, Statch22.FORAGING_STAT.get());
        int buildingStat = getDirectStat(player, Statch22.BUILDING_STAT.get());

        player.sendSystemMessage(Component.literal("§7Raw Stats:"));
        player.sendSystemMessage(Component.literal(String.format("  §eMining: §6%d", miningStat)));
        player.sendSystemMessage(Component.literal(String.format("  §eFarming: §6%d", farmingStat)));
        player.sendSystemMessage(Component.literal(String.format("  §eCombat: §6%d", combatStat)));
        player.sendSystemMessage(Component.literal(String.format("  §eFishing: §6%d", fishingStat)));
        player.sendSystemMessage(Component.literal(String.format("  §eForaging: §6%d", foragingStat)));
        player.sendSystemMessage(Component.literal(String.format("  §eBuilding: §6%d", buildingStat)));

        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal("§7Levels with Capability:"));

        showSkillLevelWithCapability(player, "Mining", miningStat, 5);
        showSkillLevelWithCapability(player, "Farming", farmingStat, 2);
        showSkillLevelWithCapability(player, "Combat", combatStat, 1);
        showSkillLevelWithCapability(player, "Fishing", fishingStat, 3);
        showSkillLevelWithCapability(player, "Foraging", foragingStat, 4);
        showSkillLevelWithCapability(player, "Building", buildingStat, 0);

        return 1;
    }

    private static void showSkillLevelWithCapability(ServerPlayer player, String skillName, int stat, int skillId) {
        int level = LevelManager.getLevelFromStat(stat);
        float progress = LevelManager.getProgressToNextLevel(stat);
        String progressBar = getProgressBar(progress);

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            int professionId = skillData.getProfession(skillId);
            String professionStr = "";

            try {
                Class<?> professionClass = Class.forName("net.bigshot.Statch22.professions.ProfessionRegistry");
                Object professionObj = professionClass.getMethod("getProfession", String.class, int.class)
                        .invoke(null, skillName.toLowerCase(), professionId);
                if (professionObj != null) {
                    String professionName = (String) professionClass.getMethod("getName").invoke(professionObj);
                    professionStr = " §8[§a" + professionName + "§8]";
                }
            } catch (Exception e) {
            }

            int currentLevelXP = LevelManager.getXPForLevel(level);
            int nextLevelXP = level < 11 ? LevelManager.getXPForLevel(level + 1) : stat;

            player.sendSystemMessage(Component.literal(
                    String.format("  §e%s: §7Level §6%d §7(§a%d§7/§c%d§7 XP) §8[%s]%s",
                            skillName,
                            level,
                            stat - currentLevelXP,
                            nextLevelXP - currentLevelXP,
                            progressBar,
                            professionStr
                    )
            ));
        });
    }

    private static int getDirectStat(ServerPlayer player, ResourceLocation statLocation) {
        try {
            Stat<ResourceLocation> stat = Stats.CUSTOM.get(statLocation);
            return player.getStats().getValue(stat);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int testStats(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6=== Stat Test ==="));

        ResourceLocation[] stats = {
                Statch22.MINING_STAT.get(),
                Statch22.FARMING_STAT.get(),
                Statch22.COMBAT_STAT.get(),
                Statch22.FISHING_STAT.get(),
                Statch22.FORAGING_STAT.get(),
                Statch22.BUILDING_STAT.get()
        };

        String[] names = {"Mining", "Farming", "Combat", "Fishing", "Foraging", "Building"};

        for (int i = 0; i < stats.length; i++) {
            try {
                Stat<ResourceLocation> stat = Stats.CUSTOM.get(stats[i]);
                int value = player.getStats().getValue(stat);
                player.sendSystemMessage(Component.literal(
                        String.format("§e%s: §7Stat ID=%s, Value=§a%d",
                                names[i], stats[i].toString(), value)
                ));
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal(
                        String.format("§c%s: §7Error - %s", names[i], e.getMessage())
                ));
            }
        }

        return 1;
    }

    private static int testCapability(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6=== Capability Test ==="));

        var capability = player.getCapability(PlayerSkillData.CAPABILITY);
        boolean isPresent = capability.isPresent();

        player.sendSystemMessage(Component.literal("§7Capability present: " + (isPresent ? "§aYES" : "§cNO")));

        if (isPresent) {
            capability.ifPresent(skillData -> {
                player.sendSystemMessage(Component.literal("§a✓ Capability accessible"));

                for (int i = 0; i < 6; i++) {
                    player.sendSystemMessage(Component.literal(
                            String.format("  Skill %d: Level=%d, Profession=%d",
                                    i, skillData.getLevel(i), skillData.getProfession(i))
                    ));
                }
            });
        }

        return 1;
    }

    private static int debugProfession(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6=== Profession Debug ==="));

        player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
            String[] skillNames = {"building", "combat", "farming", "fishing", "forage", "mining"};
            for (int i = 0; i < 6; i++) {
                int profession = skillData.getProfession(i);
                int level = skillData.getLevel(i);
                player.sendSystemMessage(Component.literal(
                        String.format("§e%s: §7Level=%d, Profession=%d",
                                skillNames[i], level, profession)
                ));
            }
        });

        return 1;
    }

    private static int levelUpSkill(ServerPlayer player, String skillName, int targetLevel) {
        if (skillName.equalsIgnoreCase("all")) {
            return levelUpAllSkills(player, targetLevel);
        }

        int skillId = LevelManager.getSkillId(skillName);
        if (skillId == -1) {
            player.sendSystemMessage(Component.literal("§cUnknown skill: " + skillName));
            return 0;
        }

        if (targetLevel < 1 || targetLevel > 11) {
            player.sendSystemMessage(Component.literal("§cLevel must be between 1 and 11"));
            return 0;
        }

        return player.getCapability(PlayerSkillData.CAPABILITY).map(skillData -> {
            ResourceLocation statLocation = getStatForSkill(skillName);
            Stat<ResourceLocation> stat = Stats.CUSTOM.get(statLocation);
            int currentStat = player.getStats().getValue(stat);
            int currentLevel = LevelManager.getLevelFromStat(currentStat);

            if (targetLevel <= currentLevel) {
                player.sendSystemMessage(Component.literal(
                        String.format("§cYou are already level %d or higher in %s!",
                                targetLevel, skillName)
                ));
                return 0;
            }

            int requiredStat = LevelManager.getXPForLevel(targetLevel);
            int statDifference = requiredStat - currentStat;

            if (statDifference > 0) {
                player.awardStat(stat, statDifference);
            }

            skillData.setLevel(skillId, targetLevel);

            for (int level = currentLevel + 1; level <= targetLevel; level++) {
                sendLevelUpMessage(player, skillName, level);

                if (level == 5 || level == 10 || level == 11) {
                    sendProfessionMessage(player, skillName, level);
                }
            }

            player.sendSystemMessage(Component.literal(
                    String.format("§aSet %s to level §6%d§a! (Stat: §e%d§a)",
                            skillName, targetLevel, requiredStat)
            ));

            return 1;
        }).orElseGet(() -> {
            player.sendSystemMessage(Component.literal("§cCapability not available!"));
            return 0;
        });
    }

    private static int levelUpAllSkills(ServerPlayer player, int targetLevel) {
        if (targetLevel < 1 || targetLevel > 11) {
            player.sendSystemMessage(Component.literal("§cLevel must be between 1 and 11"));
            return 0;
        }

        String[] skills = {"mining", "building", "combat", "fishing", "farming", "forage"};
        int count = 0;

        for (String skill : skills) {
            int skillId = LevelManager.getSkillId(skill);
            if (skillId == -1) continue;

            ResourceLocation statLocation = getStatForSkill(skill);
            Stat<ResourceLocation> stat = Stats.CUSTOM.get(statLocation);
            int currentStat = player.getStats().getValue(stat);
            int requiredStat = LevelManager.getXPForLevel(targetLevel);
            int statDifference = requiredStat - currentStat;

            if (statDifference > 0) {
                player.awardStat(stat, statDifference);
                player.getCapability(PlayerSkillData.CAPABILITY).ifPresent(skillData -> {
                    skillData.setLevel(skillId, targetLevel);
                });
                count++;
            }
        }

        player.sendSystemMessage(Component.literal(
                String.format("§aSet §6%d §askills to level §6%d", count, targetLevel)
        ));
        return count;
    }

    private static void sendLevelUpMessage(ServerPlayer player, String skillName, int newLevel) {
        String formattedSkill = skillName.substring(0, 1).toUpperCase() + skillName.substring(1);
        player.sendSystemMessage(
                Component.literal("§6★ " + formattedSkill + " Level Up! §7(Level " + newLevel + ")")
        );
    }

    private static void sendProfessionMessage(ServerPlayer player, String skillName, int level) {
        if (level == 11) {
            player.sendSystemMessage(
                    Component.literal("§6★ MASTERY ACHIEVED! §eYou've mastered " + skillName + "!")
            );
        } else {
            player.sendSystemMessage(
                    Component.literal("§bSleep to choose your level " + level + " " + skillName + " specialty!")
            );
        }
    }

    private static int showProfessions(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6=== Available Professions ==="));
        player.sendSystemMessage(Component.literal("§7Use §e/statch22 professions <skill> §7to see options"));
        player.sendSystemMessage(Component.literal("§7Skills: farming, mining, combat, fishing, forage, building"));
        return 1;
    }

    private static int showProfessionsForSkill(ServerPlayer player, String skillName) {
        int skillId = LevelManager.getSkillId(skillName);
        if (skillId == -1) {
            player.sendSystemMessage(Component.literal("§cUnknown skill: " + skillName));
            return 0;
        }

        return player.getCapability(PlayerSkillData.CAPABILITY).map(skillData -> {
            int stat = getDirectStat(player, getStatForSkill(skillName));
            int level = LevelManager.getLevelFromStat(stat);
            int currentProfession = skillData.getProfession(skillId);

            player.sendSystemMessage(Component.literal(
                    String.format("§6=== %s Professions (Level %d) ===",
                            skillName.substring(0, 1).toUpperCase() + skillName.substring(1),
                            level
                    )
            ));

            if (level < 5) {
                player.sendSystemMessage(Component.literal("§7Reach level 5 to choose your first profession!"));
                return 1;
            }

            try {
                Class<?> professionClass = Class.forName("net.bigshot.Statch22.professions.ProfessionRegistry");
                Object availableObj = professionClass.getMethod("getAvailableProfessions", String.class, int.class, int.class)
                        .invoke(null, skillName, level >= 10 ? (level == 11 ? 11 : 10) : 5, currentProfession);

                if (availableObj instanceof List<?> available && available.isEmpty()) {
                    player.sendSystemMessage(Component.literal("§7No professions available at your current level/progression."));
                    return 1;
                }

                if (availableObj instanceof List<?> available) {
                    for (Object professionObj : available) {
                        int professionId = (int) professionClass.getMethod("getId").invoke(professionObj);
                        String professionName = (String) professionClass.getMethod("getName").invoke(professionObj);
                        String professionDesc = (String) professionClass.getMethod("getDescription").invoke(professionObj);

                        boolean isCurrent = currentProfession == professionId;
                        String currentMarker = isCurrent ? " §a[SELECTED]" : "";
                        player.sendSystemMessage(Component.literal(
                                String.format("§e%d. §b%s§7: %s%s",
                                        professionId,
                                        professionName,
                                        professionDesc,
                                        currentMarker
                                )
                        ));
                    }
                }
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal("§7Profession system not available."));
                return 1;
            }

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("§7To choose: §e/statch22 professions " + skillName + " <number>"));

            return 1;
        }).orElseGet(() -> {
            player.sendSystemMessage(Component.literal("§cCapability not available!"));
            return 0;
        });
    }

    private static int chooseProfession(ServerPlayer player, String skillName, int professionId) {
        int skillId = LevelManager.getSkillId(skillName);
        if (skillId == -1) {
            player.sendSystemMessage(Component.literal("§cUnknown skill: " + skillName));
            return 0;
        }

        return player.getCapability(PlayerSkillData.CAPABILITY).map(skillData -> {
            int stat = getDirectStat(player, getStatForSkill(skillName));
            int level = LevelManager.getLevelFromStat(stat);
            int currentProfession = skillData.getProfession(skillId);

            if (professionId > 2 && professionId <= 4) {
                if (currentProfession != 1 && currentProfession != 2) {
                    player.sendSystemMessage(Component.literal("§cYou must choose a level 5 profession first!"));
                    return 0;
                }

                if (currentProfession == 1 && (professionId != 3 && professionId != 4)) {
                    player.sendSystemMessage(Component.literal("§cAs a Miner, you must choose Blacksmith (3) or Excavator (4)!"));
                    return 0;
                }

                if (currentProfession == 2 && (professionId != 5 && professionId != 6)) {
                    player.sendSystemMessage(Component.literal("§cAs a Geologist, you must choose Prospector (5) or Gemologist (6)!"));
                    return 0;
                }
            }

            if (level < 5) {
                player.sendSystemMessage(Component.literal("§cYou must reach level 5 to choose a profession!"));
                return 0;
            }

            if (level < 10 && professionId > 2) {
                player.sendSystemMessage(Component.literal("§cThat profession is only available at level 10!"));
                return 0;
            }

            if (level == 11 && professionId <= 6) {
                player.sendSystemMessage(Component.literal("§cMastery level 11 has no further professions!"));
                return 0;
            }

            if (currentProfession >= 3 && currentProfession <= 6) {
                player.sendSystemMessage(Component.literal("§cYou have already chosen your level 10 profession!"));
                return 0;
            }

            if (professionId <= 2 && currentProfession != 0 && currentProfession <= 2) {
                player.sendSystemMessage(Component.literal("§cYou have already chosen your level 5 profession!"));
                return 0;
            }

            try {
                Class<?> professionClass = Class.forName("net.bigshot.Statch22.professions.ProfessionRegistry");
                Object availableObj = professionClass.getMethod("getAvailableProfessions", String.class, int.class, int.class)
                        .invoke(null, skillName, level >= 10 ? (level == 11 ? 11 : 10) : 5, currentProfession);

                Object selected = null;
                if (availableObj instanceof List<?> available) {
                    for (Object professionObj : available) {
                        int profId = (int) professionClass.getMethod("getId").invoke(professionObj);
                        if (profId == professionId) {
                            selected = professionObj;
                            break;
                        }
                    }
                }

                if (selected == null) {
                    player.sendSystemMessage(Component.literal("§cThat profession is not available!"));
                    return 0;
                }

                skillData.setProfession(skillId, professionId);

                try {
                    Class<?> sleepClass = Class.forName("net.bigshot.Statch22.professions.SleepProfessionHandler");
                    sleepClass.getMethod("clearPendingSelection", ServerPlayer.class, String.class)
                            .invoke(null, player, skillName);
                } catch (Exception e) {
                }

                String professionName = (String) professionClass.getMethod("getName").invoke(selected);
                String professionDesc = (String) professionClass.getMethod("getDescription").invoke(selected);

                player.sendSystemMessage(Component.literal(
                        String.format("§aYou are now a §6%s §a%s!",
                                professionName,
                                skillName.substring(0, 1).toUpperCase() + skillName.substring(1)
                        )
                ));
                player.sendSystemMessage(Component.literal("§7" + professionDesc));

                return 1;
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal("§cProfession system error!"));
                return 0;
            }
        }).orElseGet(() -> {
            player.sendSystemMessage(Component.literal("§cCapability not available!"));
            return 0;
        });
    }

    private static int showPendingLevelUps(ServerPlayer player) {
        return player.getCapability(PlayerSkillData.CAPABILITY).map(skillData -> {
            List<Integer> pending = skillData.getPendingLevelUps();

            if (pending.isEmpty()) {
                player.sendSystemMessage(Component.literal("§7No pending level ups."));
                return 1;
            }

            player.sendSystemMessage(Component.literal("§6=== Pending Level Ups ==="));

            for (int skillId : pending) {
                String skillName = LevelManager.getSkillName(skillId);
                String formattedSkill = skillName.substring(0, 1).toUpperCase() + skillName.substring(1);
                player.sendSystemMessage(Component.literal("§e• " + formattedSkill + " Level Up!"));
            }

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("§7Sleep in a bed to process these level ups."));

            return 1;
        }).orElseGet(() -> {
            player.sendSystemMessage(Component.literal("§cCapability not available!"));
            return 0;
        });
    }

    private static String getProgressBar(float progress) {
        int bars = 10;
        int filled = (int) (progress * bars);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bars; i++) {
            if (i < filled) {
                sb.append("§a█");
            } else {
                sb.append("§7█");
            }
        }

        return sb.toString();
    }

    private static ResourceLocation getStatForSkill(String skillName) {
        switch (skillName.toLowerCase()) {
            case "mining": return Statch22.MINING_STAT.get();
            case "farming": return Statch22.FARMING_STAT.get();
            case "combat": return Statch22.COMBAT_STAT.get();
            case "fishing": return Statch22.FISHING_STAT.get();
            case "forage": return Statch22.FORAGING_STAT.get();
            case "building": return Statch22.BUILDING_STAT.get();
            default: return Statch22.MINING_STAT.get();
        }
    }
}