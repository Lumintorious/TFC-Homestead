package com.lumintorious.tfchomestead.common.block.entity;

//object TileHanger extends Initializable {
//        TileEntity.register("hanger", classOf[TileHanger])
//        }

import com.lumintorious.tfchomestead.common.api.StoredTrait;
import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GrainPileBlockEntity extends FoodHolderBlockEntity {
    private boolean isPreserving = false;

    public static final TagKey<Item> GRAIN_TAG =
        TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "foods/grains"));

    @Override
    public void handleGivenStack(ItemStack stack) {
        StoredTrait.eraseAll(stack);
        super.handleGivenStack(stack);
    }

    public GrainPileBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public GrainPileBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(HomesteadBlockEntities.HANGER.get(), pWorldPosition, pBlockState);
    }

    public boolean canPreserve() {
        return getLevel() != null && !stack.isEmpty() &&
            getLevel().getBrightness(LightLayer.SKY, getBlockPos()) < 14;
    }

    public boolean fits(ItemStack stack) {
        return stack.is(GRAIN_TAG);
    }

    @Override
    public boolean takeStack(Player player, int amount) {
        return super.takeStack(player, Math.min(16, amount));
    }


    @Override
    public void updatePreservation() {
        if(stack == null) {
            isPreserving = false;
            return;
        }

        if (canPreserve()) {
            FoodCapability.applyTrait(stack, StoredTrait.SHELTERED);
            isPreserving = true;
        }
        else{
            FoodCapability.removeTrait(stack, StoredTrait.SHELTERED);
            isPreserving = false;
        }
    }
}
