package com.lumintorious.tfchomestead.common.block;

import com.lumintorious.tfchomestead.common.block.entity.FoodHolderBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class GrainPileBlock extends HomesteadBlock {

    private final Supplier<? extends Item> grain;

    public GrainPileBlock(ExtendedProperties properties, Supplier<? extends Item> grain) {
        super(properties);
        this.grain = grain;
    }

    public Item getGrainItem()
    {
        return grain.get();
    }

    @Override
    public float getSpeedFactor() {
        return super.getSpeedFactor();
    }

    @Override
    public void attack(BlockState pState, Level level, BlockPos pPos, Player pPlayer) {
        super.attack(pState, level, pPos, pPlayer);

        BlockEntity entity = level.getBlockEntity(pPos);
        if(entity == null) {
            level.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
            pPlayer.sendMessage(new TextComponent("Terminated"), pPlayer.getUUID());
        }

        if(entity instanceof FoodHolderBlockEntity holder) {
            ItemStack stack = holder.getStack();
            if(level != null && !level.isClientSide() && (stack.isEmpty() || stack == null)) {
                level.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
            }
        }

    }

}
