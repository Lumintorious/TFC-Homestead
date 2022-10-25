package com.lumintorious.tfchomestead.common;

import com.lumintorious.tfchomestead.common.block.HomesteadBlocks;
import com.lumintorious.tfchomestead.common.drinks.AgedAlcohol;
import com.lumintorious.tfchomestead.common.drinks.HomesteadFluid;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class HomesteadTabs {
    public static CreativeModeTab MAIN = new CreativeModeTab("tfchomestead") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(HomesteadBlocks.FOOD_SHELVES.get(Wood.OAK).get(), 1);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            super.fillItemList(pItems);
            for(var alcohol : AgedAlcohol.values()) {
                pItems.add(getJugWith(alcohol));
            }
        }
    };

    private static ItemStack getJugWith(AgedAlcohol alcohol) {
        ItemStack stack = new ItemStack(TFCItems.JUG.get(), 1);
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            cap.fill(new FluidStack(HomesteadFluid.AGED_ALCOHOL.get(alcohol).source().get(), 100), IFluidHandler.FluidAction.EXECUTE);
        });
        return stack;
    }
}
