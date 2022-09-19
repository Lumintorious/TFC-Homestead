package com.lumintorious.tfchomestead.common.block;

import com.lumintorious.tfchomestead.common.block.entity.FoodHolderBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class GrainPileBlock extends HomesteadBlock {
    public static ExtendedProperties PROPERTIES =
        ExtendedProperties.of(Properties.of(Material.GRASS).strength(0.3F).sound(SoundType.GRASS).noOcclusion()
            .isValidSpawn((a, b, c, d) -> false)
            .isRedstoneConductor((a, b, c) -> false)
            .isSuffocating((a, b, c) -> false)
            .destroyTime(3.5f)
            .isViewBlocking((a, b, c) -> false)).blockEntity(() -> HomesteadBlockEntities.GRAIN_PILE.get());

    public GrainPileBlock(ExtendedProperties properties) {
        super(properties);
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
