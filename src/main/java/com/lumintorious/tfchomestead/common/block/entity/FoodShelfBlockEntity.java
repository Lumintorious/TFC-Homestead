package com.lumintorious.tfchomestead.common.block.entity;

import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FoodShelfBlockEntity extends FoodHolderBlockEntity {
    private boolean isPreserving = false;

    @Override
    public void handleGivenStack(ItemStack stack) {
        StoredTrait.eraseAll(stack);
        FoodCapability.applyTrait(stack, StoredTrait.HUNG);
        FoodCapability.applyTrait(stack, StoredTrait.COOL);
        super.handleGivenStack(stack);
    }

    public FoodShelfBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public FoodShelfBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(HomesteadBlockEntities.FOOD_SHELF.get(), pWorldPosition, pBlockState);
    }

    public boolean canPreserve() {
        return getLevel() != null && !stack.isEmpty() &&
            getLevel().getBrightness(LightLayer.SKY, getBlockPos()) < 2;
    }

    public boolean fits(ItemStack stack) {
        return stack.getCapability(FoodCapability.CAPABILITY).isPresent();
    }

    @Override
    public void updatePreservation() {
        if(stack == null) {
            isPreserving = false;
            return;
        }

        FoodCapability.applyTrait(stack, StoredTrait.SHELVED);
        if (canPreserve()) {
            FoodCapability.applyTrait(stack, StoredTrait.COOL);
            isPreserving = true;
        }
        else{
            FoodCapability.removeTrait(stack, StoredTrait.COOL);
            isPreserving = false;
        }
    }
}
