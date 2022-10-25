package com.lumintorious.tfchomestead.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HangerBlock extends HomesteadBlock {
    private final Wood wood;

    public static final VoxelShape BASE_SHAPE = Shapes.or(
        box(1, 12, 1, 15, 16, 15)
    );

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return BASE_SHAPE;
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
