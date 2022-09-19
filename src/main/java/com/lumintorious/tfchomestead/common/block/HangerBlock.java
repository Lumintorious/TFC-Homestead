package com.lumintorious.tfchomestead.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HangerBlock extends HomesteadBlock {
    private final Wood wood;

    public static ExtendedProperties PROPERTIES =
        ExtendedProperties.of(Properties.of(Material.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion()
                .isValidSpawn((a, b, c, d) -> false)
                .isRedstoneConductor((a, b, c) -> false)
                .isSuffocating((a, b, c) -> false)
                .strength(2f)
                .isViewBlocking((a, b, c) -> false)).blockEntity(() -> HomesteadBlockEntities.HANGER.get());

    public static final VoxelShape BASE_SHAPE = Shapes.or(
        box(1, 12, 1, 15, 16, 15)
    );

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return BASE_SHAPE;
    }

    public static Map<Wood, RegistryObject<HangerBlock>> initForAllWoods(DeferredRegister<Block> registerer) {
        return Arrays.stream(Wood.values()).collect(Collectors.toMap(
            (wood) -> wood,
            (wood) -> registerer.register("wood/hanger/" + wood.getSerializedName(), () -> new HangerBlock(wood, PROPERTIES))
        ));
    }

    public HangerBlock(Wood wood, ExtendedProperties properties) {
        super(properties);
        this.wood = wood;
    }

    public Wood getWood() {
        return wood;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        List<ItemStack> stacks = super.getDrops(pState, pBuilder);
        stacks.add(new ItemStack(this.asItem()));
        return stacks;
    }
}
