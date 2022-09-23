package com.lumintorious.tfchomestead.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class TFCHomesteadConfig {
    public static CommonImpl COMMON = register(ModConfig.Type.COMMON, CommonImpl::new);

    public static void init() {}

    private static <C> C register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> factory)
    {
        Pair<C, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
        ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair.getLeft();
    }

    public static class CommonImpl {
        public final ForgeConfigSpec.BooleanValue enableVillagerSpawns;
        public final ForgeConfigSpec.BooleanValue enableRideableConstantSpeed;
        public final ForgeConfigSpec.BooleanValue enableAgedDrinks;

        CommonImpl(ForgeConfigSpec.Builder builder) {
            enableVillagerSpawns = builder
                    .comment("If enabled, villager huts will spawn in the world")
                    .define("enableVillagerSpawns", true);

            enableRideableConstantSpeed = builder
                    .comment("If enabled, rideable animals will not be slowed by plants/snow/mud when ridden.")
                    .define("enableRideableConstantSpeed", true);

            enableAgedDrinks = builder
                    .comment("If enabled, aging of drinks for effects is possible.")
                    .define("enableAgedDrinks", true);
        }
    }
}
