package com.lumintorious.tfchomestead.common.block.entity;

import com.lumintorious.tfchomestead.common.api.StoredTrait;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoodHolderBlockEntity extends BlockEntity {
    protected ItemStack stack = ItemStack.EMPTY;

    public FoodHolderBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public boolean isPreserving() {
        return false;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean getDrops(List<ItemStack> list) {
        ItemStack newStack = stack.copy();
        this.handleGivenStack(newStack);
        list.add(newStack);
        return true;
    }

    public boolean onLeftClick(Player player) {
        if(!stack.isEmpty()) {
            boolean value;
            if(player.isCrouching()) {
                value = giveStack(player);
            }
            else {
                value = giveStack(player, 1);
            }

            this.saveWithFullMetadata();
            this.update();
            return value;
        }
        else {
            return true;
        }
    }

    public boolean onRightClick(Player player) {
        ItemStack playerStack = player.getMainHandItem();
        if(stack.isEmpty() && fits(playerStack)) {
            boolean value = takeStack(player);
            this.saveWithFullMetadata();
            this.update();
            return value;
        }else{
            return true;
        }
    }

    public boolean fits(ItemStack stack){
        return stack.getCapability(FoodCapability.CAPABILITY, null).isPresent() &&
            (this.stack.isEmpty() || (this.stack.getItem() == stack.getItem() && ItemStack.isSame(this.stack, stack)));
    }

    public void handleGivenStack(ItemStack stack) {
        StoredTrait.eraseAll(stack);
        updatePreservation();
    }

    public boolean giveStack(Player player) {
        return giveStack(player, 64);
    }

    public boolean giveStack(Player player, int amount)  {
        if(stack.getItem() == Items.AIR) return false;
        ItemStack stackToGive = stack.split(amount);
        handleGivenStack(stackToGive);
        boolean ok = player.addItem(stackToGive);
        if(!ok){
            this.stack.setCount(this.stack.getCount() + stackToGive.getCount());
        }
        this.update();
        return true;
    }

    public void handleTakenStack(ItemStack stack) {
        updatePreservation();
    }

    public boolean takeStack(Player player) {
      return takeStack(player, 64);
    }

    public boolean takeStack(Player player, int amount) {
        if(!fits(player.getMainHandItem())) return false;
        ItemStack stackToTake = player.getMainHandItem().split(amount);
        if(this.stack.isEmpty()) {
            this.stack = stackToTake;
        }else{
            this.stack.setCount(this.stack.getCount() + stackToTake.getCount());
        }
        handleTakenStack(stackToTake);
        this.update();
        return true;
    }

//    private int timeLeft = 200;
//    public void update() {
//		if(timeLeft <= 0) {
//			timeLeft = 200
//			updatePreservation()
//		}
//		else {
//			timeLeft -= 1
//		}
//    }
//
//

    public void updatePreservation() {}

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handleUpdateTag(nbt);
        updatePreservation();
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        try {
            StoredTrait.eraseAll(stack);
            tag.put("itemStack", stack.serializeNBT());
        }
		finally{
            updatePreservation();
        }
        return tag;
    }

    public void update() {
        this.updatePreservation();
        requestModelDataUpdate();
        setChanged();
        if(this.level != null) {
            this.level.setBlockAndUpdate(worldPosition, getBlockState());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        stack = ItemStack.of(tag.getCompound("itemStack"));
        this.updatePreservation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("itemStack", this.getStack().serializeNBT());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection conn, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(conn, packet);
        handleUpdateTag(packet.getTag());
        this.updatePreservation();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
//        stack = ItemStack.of(tag.getCompound("itemStack"));
    }

    @Override
    public @NotNull CompoundTag getTileData() {
        return this.serializeNBT();
    }
}
