package net.bigshot.Statch22.client;

import net.bigshot.Statch22.LevelManager;
import java.util.HashMap;
import java.util.Map;

public class ClientSkillData {
    private static final Map<Integer, Integer> skillLevels = new HashMap<>();

    public static void updateFromServer(Map<Integer, Integer> serverStats) {
        skillLevels.clear();
        for (Map.Entry<Integer, Integer> entry : serverStats.entrySet()) {
            skillLevels.put(entry.getKey(), LevelManager.getLevelFromStat(entry.getValue()));
        }
    }

    public static int getSkillLevel(int skillId) {
        return skillLevels.getOrDefault(skillId, 0);
    }
}