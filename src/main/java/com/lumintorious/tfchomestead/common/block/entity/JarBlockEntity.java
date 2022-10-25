package com.lumintorious.tfchomestead.common.block.entity;

import com.lumintorious.tfchomestead.common.block.HomesteadBlockEntities;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarBlockEntity extends FoodHolderBlockEntity {
    private FluidTank tank = new FluidTank(8000);
    private boolean isPreserving = false;

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public JarBlockEntity(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public JarBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(HomesteadBlockEntities.JAR.get(), pWorldPosition, pBlockState);
    }

    public FluidStack getFluidStack() {
        return tank.getFluid();
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public boolean getDrops(List<ItemStack> list) {
        boolean hasLiquid = this.tank.getFluidAmount() > 0;
        var stack = new ItemStack(HomesteadBlocks.JAR.get(), 1);
        if(hasLiquid) {
            stack = new ItemStack(HomesteadBlocks.FULL_JAR.get(), 1);
        }
        CompoundTag tag = new CompoundTag();
        if(!this.stack.isEmpty()) {
            ItemStack givenStack = this.stack.copy();
            handleGivenStack(givenStack);
            list.add(givenStack);
        }
        if(hasLiquid) {
            tag.put("fluid", tank.getFluid().writeToNBT(new CompoundTag()));
        }
        if(!tag.isEmpty())
        {
            stack.setTag(tag);
        }
        list.add(stack);
        return false;
    }

    @Override
    public void updatePreservation() {
        if(stack == null) {
            isPreserving = false;
            return;
        }
        if (canPreserve(stack)) {
            FoodCapability.applyTrait(stack, StoredTrait.JAR);
            isPreserving = true;
        }
        else{
            FoodCapability.removeTrait(stack, StoredTrait.JAR);
            isPreserving = false;
        }
    }

    @Override
    public boolean fits(ItemStack stack) {
        return stack.getCapability(FoodCapability.CAPABILITY).isPresent();
    }

    public boolean canPreserve(ItemStack stack) {
        FluidStack fluidStack = tank.getFluid();

        if(fluidStack.getFluid() != TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).getSource() || fluidStack.getAmount() < stack.getCount() * 125) {
            return false;
        }
        else {
            return stack.getCapability(FoodCapability.CAPABILITY, null).map(cap ->
                cap.getTraits().contains(FoodTraits.PICKLED)
            ).orElse(false);
        }
    }

    public boolean onRightClick(Player player) {
        ItemStack playerStack = player.getMainHandItem();
        if(playerStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
            FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, tank);
            update();
            return true;
        }
        else if(playerStack.isEmpty() && !player.level.isClientSide()) {
            update();
            return true;
        }
        else {
            return super.onRightClick(player);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.FLUID)
            return holder.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.readFromNBT(tag.getCompound("tank"));
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("tank", tank.writeToNBT(new CompoundTag()));
    }
}
