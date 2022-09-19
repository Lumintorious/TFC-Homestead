package com.lumintorious.tfchomestead.common.block;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.block.entity.FoodShelfBlockEntity;
import com.lumintorious.tfchomestead.common.block.entity.GrainPileBlockEntity;
import com.lumintorious.tfchomestead.common.block.entity.HangerBlockEntity;
import com.lumintorious.tfchomestead.common.block.entity.JarBlockEntity;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

import static com.lumintorious.tfchomestead.TFCHomestead.MOD_ID;

public abstract class HomesteadBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);

    public static RegistryObject<BlockEntityType<JarBlockEntity>> JAR =
        RegistrationHelpers.register(
            BLOCK_ENTITIES,
            "jar",
            JarBlockEntity::new,
            Stream.of(HomesteadBlocks.JAR)
        );

    public static RegistryObject<BlockEntityType<HangerBlockEntity>> HANGER =
        RegistrationHelpers.register(
            BLOCK_ENTITIES,
            "hanger",
            HangerBlockEntity::new,
            HomesteadBlocks.HANGERS.values().stream()
        );

    public static RegistryObject<BlockEntityType<FoodShelfBlockEntity>> FOOD_SHELF =
        RegistrationHelpers.register(
            BLOCK_ENTITIES,
            "food_shelf",
            FoodShelfBlockEntity::new,
            HomesteadBlocks.FOOD_SHELVES.values().stream()
        );

    public static RegistryObject<BlockEntityType<GrainPileBlockEntity>> GRAIN_PILE =
        RegistrationHelpers.register(
            BLOCK_ENTITIES,
            "grain_pile",
            GrainPileBlockEntity::new,
            Stream.of(
                HomesteadBlocks.WHEAT_GRAIN_PILE,
                HomesteadBlocks.MAIZE_GRAIN_PILE,
                HomesteadBlocks.OAT_GRAIN_PILE,
                HomesteadBlocks.RICE_GRAIN_PILE,
                HomesteadBlocks.RYE_GRAIN_PILE
            )
        );
}
