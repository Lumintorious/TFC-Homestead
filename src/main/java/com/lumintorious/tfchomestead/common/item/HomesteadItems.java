package com.lumintorious.tfchomestead.common.item;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.HomesteadTabs;
import com.lumintorious.tfchomestead.common.block.HomesteadBlock;
import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import com.lumintorious.tfchomestead.common.block.JarBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomesteadItems {
    public static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TFCHomestead.MOD_ID);

    public static RegistryObject<Item> WALKING_CANE = ITEMS.register(
            "walking_cane",
            () -> new ToolItem(
                    Tiers.WOOD,
                    0,
                    0,
                    TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("tfchomestead", "mineable/walking_cane")),
                    new Item.Properties().tab(HomesteadTabs.MAIN)
            )
    );

    public static RegistryObject<Item> REFINED_WALKING_CANE = ITEMS.register(
            "refined_walking_cane",
            () -> new ToolItem(
                    Tiers.IRON,
                    0,
                    0,
                    TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("tfchomestead", "mineable/walking_cane")),
                    new Item.Properties().tab(HomesteadTabs.MAIN)
            )
    );

    public static RegistryObject<Item> FULL_JAR = ITEMS.register(
            "full_jar",
            () -> new FullJarItemBlock(new Item.Properties().tab(HomesteadTabs.MAIN))
    );

    public static RegistryObject<Item> JAR = ITEMS.register(
            "jar",
            () -> new BlockItem(HomesteadBlocks.JAR.get(), new Item.Properties().tab(HomesteadTabs.MAIN))
    );

    public static Map<Wood, RegistryObject<Item>> HANGERS = HomesteadBlocks.HANGERS.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                (pair) -> ITEMS.register(
                    "wood/hanger/" + pair.getKey().getSerializedName(),
                    () -> new BlockItem(pair.getValue().get(), new Item.Properties().tab(HomesteadTabs.MAIN))
                )
            )
        );

    public static Map<Wood, RegistryObject<Item>> FOOD_SHELVES = HomesteadBlocks.FOOD_SHELVES.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                (pair) -> ITEMS.register(
                    "wood/food_shelf/" + pair.getKey().getSerializedName(),
                    () -> new BlockItem(pair.getValue().get(), new Item.Properties().tab(HomesteadTabs.MAIN))
                )
            )
        );
}
