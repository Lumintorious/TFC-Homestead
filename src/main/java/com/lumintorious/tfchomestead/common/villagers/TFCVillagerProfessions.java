package com.lumintorious.tfchomestead.common.villagers;

import com.google.common.collect.ImmutableSet;
import com.lumintorious.tfchomestead.TFCHomestead;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class TFCVillagerProfessions {
    public static DeferredRegister<VillagerProfession> PROFESSIONS =
        DeferredRegister.create(ForgeRegistries.PROFESSIONS, TFCHomestead.MOD_ID);

    public static DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, TFCHomestead.MOD_ID);

    public static final RegistryObject<PoiType> PRIMITIVE_POI = POI_TYPES.register(
        "primitive_poi",
        () -> new PoiType(
            "primitive_poi",
            Set.of(Blocks.CRAFTING_TABLE.defaultBlockState()),
            50,
            50
        )
    );

//    public static final RegistryObject <VillagerProfession> PRIMITIVE =
//        PROFESSIONS.register(
//            "primitive",
//            () -> new VillagerProfession(
//                "primitive",
//                PRIMITIVE_POI.get(),
//                ImmutableSet.of(),
//                ImmutableSet.of(),
//                SoundEvents.VILLAGER_WORK_ARMORER
//            )
//        );

    public static final RegistryObject <VillagerProfession> LUMBERJACK =
        PROFESSIONS.register(
            "lumberjack",
            () -> new VillagerProfession(
                    "lumberjack",
                    PRIMITIVE_POI.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER
            )
        );

    public static final RegistryObject <VillagerProfession> DIGGER =
            PROFESSIONS.register(
                    "digger",
                    () -> new VillagerProfession(
                            "digger",
                            PRIMITIVE_POI.get(),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject <VillagerProfession> MINER =
            PROFESSIONS.register(
                    "miner",
                    () -> new VillagerProfession(
                            "miner",
                            PRIMITIVE_POI.get(),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject <VillagerProfession> BUILDER =
            PROFESSIONS.register(
                    "builder",
                    () -> new VillagerProfession(
                            "builder",
                            PRIMITIVE_POI.get(),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject <VillagerProfession> FARMER =
            PROFESSIONS.register(
                    "farmer",
                    () -> new VillagerProfession(
                            "farmer",
                            PRIMITIVE_POI.get(),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

}
