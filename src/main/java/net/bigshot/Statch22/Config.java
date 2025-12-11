package net.bigshot.Statch22;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue maxSeedsTracked;
    public static final ForgeConfigSpec.BooleanValue trackModSeeds;

    static {
        BUILDER.comment("Seed Tracking Config");
        maxSeedsTracked = BUILDER.defineInRange("maxSeedsTracked", 1000, 1, 100000);
        trackModSeeds = BUILDER.define("trackModSeeds", true);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
