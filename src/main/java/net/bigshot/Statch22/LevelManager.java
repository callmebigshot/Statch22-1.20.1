package net.bigshot.Statch22;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    private static final int[] XP_TABLE = {0, 100, 380, 770, 1300, 2150, 3300, 4800, 6900, 10000, 15000, 30000};
    private static final Map<String, Integer> SKILL_IDS = new HashMap<>();

    static {
        SKILL_IDS.put("building", 0);
        SKILL_IDS.put("combat", 1);
        SKILL_IDS.put("farming", 2);
        SKILL_IDS.put("fishing", 3);
        SKILL_IDS.put("forage", 4);
        SKILL_IDS.put("mining", 5);
    }

    public static int getLevelFromStat(int statValue) {
        for (int level = 11; level >= 1; level--) {
            if (statValue >= XP_TABLE[level]) {
                return level;
            }
        }
        return 0;
    }

    public static int getXPForLevel(int level) {
        if (level < 1 || level > 11) return 0;
        return XP_TABLE[level];
    }

    public static int getXPForNextLevel(int currentStat) {
        int currentLevel = getLevelFromStat(currentStat);
        if (currentLevel >= 11) return 0;
        return XP_TABLE[currentLevel + 1] - currentStat;
    }

    public static float getProgressToNextLevel(int currentStat) {
        int currentLevel = getLevelFromStat(currentStat);
        if (currentLevel >= 11) return 1.0f;

        int currentLevelXP = XP_TABLE[currentLevel];
        int nextLevelXP = XP_TABLE[currentLevel + 1];
        int xpInLevel = currentStat - currentLevelXP;
        int xpNeeded = nextLevelXP - currentLevelXP;

        return (float) xpInLevel / xpNeeded;
    }

    public static int getSkillId(String skillName) {
        return SKILL_IDS.getOrDefault(skillName.toLowerCase(), -1);
    }

    public static String getSkillName(int skillId) {
        for (Map.Entry<String, Integer> entry : SKILL_IDS.entrySet()) {
            if (entry.getValue() == skillId) {
                return entry.getKey();
            }
        }
        return "unknown";
    }
}