package com.lumintorious.tfchomestead.common.item;

import com.lumintorious.tfchomestead.common.block.entity.JarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class FullJarItemBlock extends BlockItem{
    public FullJarItemBlock(Block block, Properties pProperties) {
        super(block, pProperties);
    }

    @Override
    public Component getName(ItemStack stack) {
        List<Component> list = new LinkedList<>();
        list.add(super.getName(stack));
        if(stack.hasTag())
        {
            CompoundTag tag = stack.getTag();
            assert tag != null;
            if(tag.contains("fluid"))
            {
                FluidStack tagFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("fluid"));
                list.add(new TextComponent(" (" + tagFluid.getAmount() + "mb "));
                list.add(tagFluid.getDisplayName());

                list.add(new TextComponent( ")"));
            }
        }
        return ComponentUtils.formatList(list, new TextComponent(""));
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        if(super.placeBlock(pContext, pState)) {
            BlockPos pos = pContext.getClickedPos();
            if(pContext.getLevel().getBlockEntity(pos) instanceof JarBlockEntity jar) {
                CompoundTag tag = pContext.getItemInHand().getTag();
                if(tag != null && tag.contains("fluid")) {
                    jar.getTank().setFluid(FluidStack.loadFluidStackFromNBT(tag.getCompound("fluid")));
                }
            }
            return true;
        }
        return false;
    }
}
