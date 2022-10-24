package com.lumintorious.tfchomestead.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.lumintorious.tfchomestead.common.block.GrainPileBlock;
import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import net.dries007.tfc.util.InteractionManager;

public class HomesteadInteractionManager
{
    public static void init()
    {
        HomesteadBlocks.GRAIN_PILES.forEach((grain, pile) -> {
            final GrainPileBlock block = (GrainPileBlock) pile.get();
            final Item item = block.getGrainItem();

            InteractionManager.register(item, (stack, context) -> {
                final Level level = context.getLevel();
                final BlockPos pos = context.getClickedPos();
                final BlockState stateAt = level.getBlockState(pos);
            });
        });
    }
}
