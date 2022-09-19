package com.lumintorious.tfchomestead.common.block.entity;

//object TileHanger extends Initializable {
//        TileEntity.register("hanger", classOf[TileHanger])
//        }

import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import net.dries007.tfc.common.capabilities.food.*;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HangerBlockEntity extends FoodHolderBlockEntity {
    private boolean isPreserving = false;

    public static final TagKey<Item> COOKED_MEAT =
        TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "foods/cooked_meats"));

    public static final TagKey<Item> RAW_MEAT =
        TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("tfc", "foods/meats"));

    @Override
    public void handleGivenStack(ItemStack stack) {
        StoredTrait.eraseAll(stack);
        super.handleGivenStack(stack);
    }

    public HangerBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public HangerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(HomesteadBlockEntities.HANGER.get(), pWorldPosition, pBlockState);
    }

    public boolean canPreserve() {
        return getLevel() != null && !stack.isEmpty() &&
            getLevel().getBrightness(LightLayer.SKY, getBlockPos()) < 2;
    }

    @Override
    public boolean fits(ItemStack stack) {
        return stack.is(RAW_MEAT) || stack.is(COOKED_MEAT) || stack.is(TFCItems.FOOD.get(Food.GARLIC).get());
    }

    @Override
    public void updatePreservation() {
        if(stack == null) {
            isPreserving = false;
            return;
        }

        FoodCapability.applyTrait(stack, StoredTrait.HUNG);
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
