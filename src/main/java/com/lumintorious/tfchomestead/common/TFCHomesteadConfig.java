package com.lumintorious.tfchomestead.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public class TFCHomesteadConfig {
    public static CommonImpl COMMON = register(ModConfig.Type.COMMON, CommonImpl::new);
    public static ServerImpl SERVER = register(ModConfig.Type.SERVER, ServerImpl::new);

    public static void init() {}

    private static <C> C register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, C> factory)
    {
        Pair<C, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
        ModLoadingContext.get().registerConfig(type, specPair.getRight());
        return specPair.getLeft();
    }

    /**
     * Synced between logical server and client
     */
    public static class ServerImpl {
        public final ForgeConfigSpec.BooleanValue enableRideableConstantSpeed;

        ServerImpl(ForgeConfigSpec.Builder builder) {
            enableRideableConstantSpeed = builder
                .comment("If enabled, rideable animals will not be slowed by plants/snow/mud when ridden.")
                .define("enableRideableConstantSpeed", true);
        }
    }

    /**
     * Expected on both logical sides but is not synced
     */
    public static class CommonImpl {
        public final ForgeConfigSpec.BooleanValue enableMoreLootForDomesticatedAnimals;
        public final ForgeConfigSpec.BooleanValue enableVillagerSpawns;
        public final ForgeConfigSpec.BooleanValue enableAgedDrinks;

        CommonImpl(ForgeConfigSpec.Builder builder) {
            enableMoreLootForDomesticatedAnimals = builder
                .comment("If enabled, animals who are familiarized will yield more meat/h" +
                        "ides (up to 100% more at 100 familiarity)")
                .define("enableMoreLootForDomesticatedAnimals", true);

            enableVillagerSpawns = builder
                    .comment("If enabled, villager huts will spawn in the world")
                    .define("enableVillagerSpawns", true);

            enableAgedDrinks = builder
                    .comment("If enabled, aging of drinks for effects is possible.")
                    .define("enableAgedDrinks", true);
        }
    }
}
