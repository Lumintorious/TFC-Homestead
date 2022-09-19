package com.lumintorious.tfchomestead.common.block;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.drinks.AgedAlcohol;
import com.lumintorious.tfchomestead.common.drinks.HomesteadFluid;
import com.lumintorious.tfchomestead.common.item.HomesteadItems;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.Alcohol;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
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

    public static RegistryObject<Block> JAR = BLOCKS.register(
        "jar",
            JarBlock::new
    );

    public static RegistryObject<Block> FULL_JAR = BLOCKS.register(
            "full_jar",
            JarBlock::new
    );

    public static Map<Wood, RegistryObject<HangerBlock>> HANGERS = HangerBlock.initForAllWoods(BLOCKS);

    public static Map<Wood, RegistryObject<FoodShelfBlock>> FOOD_SHELVES = FoodShelfBlock.initForAllWoods(BLOCKS);

    public static final Map<AgedAlcohol, RegistryObject<LiquidBlock>> AGED_ALCOHOLS =
        Helpers.mapOfKeys(AgedAlcohol.class, (fluid) -> {
            return register("fluid/" + fluid.getId(), () -> {
                return new LiquidBlock(
                    ((FlowingFluidRegistryObject) HomesteadFluid.AGED_ALCOHOL.get(fluid)).source(),
                    net.minecraft.world.level.block.state.BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()
                );
            });
        });;

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, HomesteadItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
    public static RegistryObject<Block> WHEAT_GRAIN_PILE = BLOCKS.register(
    "grain_pile/wheat",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> MAIZE_GRAIN_PILE = BLOCKS.register(
        "grain_pile/maize",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> RYE_GRAIN_PILE = BLOCKS.register(
        "grain_pile/rye",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> RICE_GRAIN_PILE = BLOCKS.register(
        "grain_pile/rice",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> OAT_GRAIN_PILE = BLOCKS.register(
        "grain_pile/oat",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

    public static RegistryObject<Block> BARLEY_GRAIN_PILE = BLOCKS.register(
        "grain_pile/barley",
        () -> new GrainPileBlock(GrainPileBlock.PROPERTIES)
    );

}
