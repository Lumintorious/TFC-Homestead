package com.lumintorious.tfchomestead.common.block.entity;

//object TileHanger extends Initializable {
//        TileEntity.register("hanger", classOf[TileHanger])
//        }

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.LogPileBlockEntity;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class GrainPileBlockEntity extends FoodHolderBlockEntity {
    private boolean isPreserving = false;

    public static Map<Item, Block> GRAIN_TO_PILES = Map.of(
        TFCItems.FOOD.get(Food.WHEAT_GRAIN).get(), HomesteadBlocks.WHEAT_GRAIN_PILE.get(),
        TFCItems.FOOD.get(Food.MAIZE_GRAIN).get(), HomesteadBlocks.MAIZE_GRAIN_PILE.get(),
        TFCItems.FOOD.get(Food.OAT_GRAIN).get(), HomesteadBlocks.OAT_GRAIN_PILE.get(),
        TFCItems.FOOD.get(Food.RYE_GRAIN).get(), HomesteadBlocks.RYE_GRAIN_PILE.get(),
        TFCItems.FOOD.get(Food.RICE_GRAIN).get(), HomesteadBlocks.RICE_GRAIN_PILE.get(),
        TFCItems.FOOD.get(Food.BARLEY_GRAIN).get(), HomesteadBlocks.BARLEY_GRAIN_PILE.get()
    );

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
