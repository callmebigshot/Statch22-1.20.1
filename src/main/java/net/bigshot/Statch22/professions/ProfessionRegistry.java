package net.bigshot.Statch22;

import java.util.*;

public class ProfessionRegistry {
    public static final int PROFESSION_NONE = 0;

    public static final Map<String, Map<Integer, List<Profession>>> PROFESSIONS_BY_SKILL = new HashMap<>();

    static {
        initMiningProfessions();
        initFarmingProfessions();
        initCombatProfessions();
        initFishingProfessions();
        initForagingProfessions();
        initBuildingProfessions();
    }

    private static void initMiningProfessions() {
        Map<Integer, List<Profession>> miningProfessions = new HashMap<>();

        miningProfessions.put(5, Arrays.asList(
                new Profession(1, "Miner", "Mining speed increased by 10%"),
                new Profession(2, "Geologist", "Gem drops increased by 50%")
        ));

        miningProfessions.put(10, Arrays.asList(
                new Profession(3, "Blacksmith", "Metal yields increased by 100%", Arrays.asList(1)),
                new Profession(4, "Excavator", "Mining speed increased by 25%", Arrays.asList(1)),
                new Profession(5, "Prospector", "All ore drops increased by 75%", Arrays.asList(2)),
                new Profession(6, "Gemologist", "Gem drops doubled (100% increase)", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("mining", miningProfessions);
    }

    private static void initFarmingProfessions() {
        Map<Integer, List<Profession>> farmingProfessions = new HashMap<>();

        farmingProfessions.put(5, Arrays.asList(
                new Profession(1, "Tiller", "Crops grow 15% faster"),
                new Profession(2, "Rancher", "Animal products yield 20% more")
        ));

        farmingProfessions.put(10, Arrays.asList(
                new Profession(3, "Agriculturist", "Crops grow 30% faster", Arrays.asList(1)),
                new Profession(4, "Artisan", "Artisan goods worth 40% more", Arrays.asList(1)),
                new Profession(5, "Coop Master", "Chickens produce eggs 50% faster", Arrays.asList(2)),
                new Profession(6, "Shepherd", "Sheep regrow wool 50% faster", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("farming", farmingProfessions);
    }

    private static void initCombatProfessions() {
        Map<Integer, List<Profession>> combatProfessions = new HashMap<>();

        combatProfessions.put(5, Arrays.asList(
                new Profession(1, "Fighter", "+15% damage to all enemies"),
                new Profession(2, "Scout", "+15% movement speed")
        ));

        combatProfessions.put(10, Arrays.asList(
                new Profession(3, "Brute", "+30% damage, -10% defense", Arrays.asList(1)),
                new Profession(4, "Defender", "+30% defense, -10% damage", Arrays.asList(1)),
                new Profession(5, "Acrobat", "+30% movement speed", Arrays.asList(2)),
                new Profession(6, "Desperado", "Critical hits deal 2.5x damage", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("combat", combatProfessions);
    }

    private static void initFishingProfessions() {
        Map<Integer, List<Profession>> fishingProfessions = new HashMap<>();

        fishingProfessions.put(5, Arrays.asList(
                new Profession(1, "Fisher", "Fish sell for 25% more"),
                new Profession(2, "Trapper", "Crab pots produce 30% faster")
        ));

        fishingProfessions.put(10, Arrays.asList(
                new Profession(3, "Angler", "Fish sell for 50% more", Arrays.asList(1)),
                new Profession(4, "Pirate", "Treasure find chance doubled", Arrays.asList(1)),
                new Profession(5, "Mariner", "Crab pots never produce junk", Arrays.asList(2)),
                new Profession(6, "Luremaster", "Fishing speed increased by 25%", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("fishing", fishingProfessions);
    }

    private static void initForagingProfessions() {
        Map<Integer, List<Profession>> forageProfessions = new HashMap<>();

        forageProfessions.put(5, Arrays.asList(
                new Profession(1, "Forester", "Trees drop 30% more wood"),
                new Profession(2, "Gatherer", "Chance for double forage items")
        ));

        forageProfessions.put(10, Arrays.asList(
                new Profession(3, "Lumberjack", "Trees drop 60% more wood", Arrays.asList(1)),
                new Profession(4, "Tapper", "Tree saps produce 40% faster", Arrays.asList(1)),
                new Profession(5, "Botanist", "All forage is highest quality", Arrays.asList(2)),
                new Profession(6, "Tracker", "See forage spots through walls", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("forage", forageProfessions);
    }

    private static void initBuildingProfessions() {
        Map<Integer, List<Profession>> buildingProfessions = new HashMap<>();

        buildingProfessions.put(5, Arrays.asList(
                new Profession(1, "Carpenter", "Buildings cost 15% less materials"),
                new Profession(2, "Stonemason", "Stone structures are 25% stronger")
        ));

        buildingProfessions.put(10, Arrays.asList(
                new Profession(3, "Architect", "Buildings cost 30% less materials", Arrays.asList(1)),
                new Profession(4, "Engineer", "Redstone devices work 30% faster", Arrays.asList(1)),
                new Profession(5, "Sculptor", "Stone tools last 60% longer", Arrays.asList(2)),
                new Profession(6, "Fortifier", "Structures take 50% less damage", Arrays.asList(2))
        ));

        PROFESSIONS_BY_SKILL.put("building", buildingProfessions);
    }

    public static List<Profession> getAvailableProfessions(String skillName, int level, int currentProfession) {
        Map<Integer, List<Profession>> skillProfessions = PROFESSIONS_BY_SKILL.get(skillName);
        if (skillProfessions == null) return new ArrayList<>();

        List<Profession> professions = skillProfessions.get(level);
        if (professions == null) return new ArrayList<>();

        if (level == 10) {
            List<Profession> available = new ArrayList<>();
            for (Profession profession : professions) {
                if (profession.getRequires().contains(currentProfession)) {
                    available.add(profession);
                }
            }
            return available;
        }

        return professions;
    }

    public static Profession getProfession(String skillName, int professionId) {
        Map<Integer, List<Profession>> skillProfessions = PROFESSIONS_BY_SKILL.get(skillName);
        if (skillProfessions == null) return null;

        for (List<Profession> professionList : skillProfessions.values()) {
            for (Profession profession : professionList) {
                if (profession.getId() == professionId) {
                    return profession;
                }
            }
        }
        return null;
    }

    public static class Profession {
        private final int id;
        private final String name;
        private final String description;
        private final List<Integer> requires;

        public Profession(int id, String name, String description) {
            this(id, name, description, new ArrayList<>());
        }

        public Profession(int id, String name, String description, List<Integer> requires) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.requires = requires;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<Integer> getRequires() { return requires; }
    }
}