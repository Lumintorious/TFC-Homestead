package com.lumintorious.tfchomestead.common.block;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.HomesteadTabs;
import com.lumintorious.tfchomestead.common.drinks.AgedAlcohol;
import com.lumintorious.tfchomestead.common.drinks.HomesteadFluid;
import com.lumintorious.tfchomestead.common.item.FullJarItemBlock;
import com.lumintorious.tfchomestead.common.item.HomesteadItems;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class HomesteadBlocks {
    public static DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, TFCHomestead.MOD_ID);

    public static RegistryObject<Block> JAR = register(
        "jar",
            JarBlock::new,
            HomesteadTabs.MAIN
    );

    public static RegistryObject<Block> FULL_JAR = register(
            "full_jar",
            JarBlock::new,
        b -> new FullJarItemBlock(b, new Item.Properties().tab(HomesteadTabs.MAIN))
    );

    public static Map<Wood, RegistryObject<Block>> HANGERS = Helpers.mapOfKeys(Wood.class, wood -> register("wood/hanger/" + wood.getSerializedName(), () -> new HangerBlock(wood, hangerProperties()), HomesteadTabs.MAIN));
    public static Map<Wood, RegistryObject<Block>> FOOD_SHELVES = Helpers.mapOfKeys(Wood.class, wood -> register("wood/food_shelf/" + wood.getSerializedName(), () -> new FoodShelfBlock(wood, shelfProperties()), HomesteadTabs.MAIN));

    public static ExtendedProperties hangerProperties() {
        return ExtendedProperties.of(BlockBehaviour.Properties.of(Material.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion()
                .isValidSpawn((a, b, c, d) -> false)
                .isRedstoneConductor((a, b, c) -> false)
                .isSuffocating((a, b, c) -> false)
                .strength(2f)
                .isViewBlocking((a, b, c) -> false)).blockEntity(() -> HomesteadBlockEntities.HANGER.get());
    }


    public static ExtendedProperties shelfProperties() {
        return ExtendedProperties.of(BlockBehaviour.Properties.of(Material.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion()
            .isValidSpawn((a, b, c, d) -> false)
            .isRedstoneConductor((a, b, c) -> false)
            .isSuffocating((a, b, c) -> false)
            .strength(2f)
            .isViewBlocking((a, b, c) -> false)).blockEntity(() -> HomesteadBlockEntities.FOOD_SHELF.get());
    }

    public static final Map<AgedAlcohol, RegistryObject<LiquidBlock>> AGED_ALCOHOLS =
        Helpers.mapOfKeys(AgedAlcohol.class, (fluid) -> {
            return register("fluid/" + fluid.getId(), () -> {
                return new LiquidBlock(
                    ((FlowingFluidRegistryObject) HomesteadFluid.AGED_ALCOHOL.get(fluid)).source(),
                    BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()
                );
            });
        });;

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab tab) {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties().tab(tab)));
    }
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, HomesteadItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
    public static RegistryObject<Block> WHEAT_GRAIN_PILE = register(
    "grain_pile/wheat",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> MAIZE_GRAIN_PILE = register(
        "grain_pile/maize",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> RYE_GRAIN_PILE = register(
        "grain_pile/rye",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> RICE_GRAIN_PILE = register(
        "grain_pile/rice",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> OAT_GRAIN_PILE = register(
        "grain_pile/oat",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> BARLEY_GRAIN_PILE = register(
        "grain_pile/barley",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

}
