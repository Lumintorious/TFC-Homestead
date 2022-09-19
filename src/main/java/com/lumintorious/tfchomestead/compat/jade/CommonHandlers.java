package com.lumintorious.tfchomestead.compat.jade;

import com.lumintorious.tfchomestead.common.block.HomesteadBlock;
import com.lumintorious.tfchomestead.common.api.StoredTrait;
import com.lumintorious.tfchomestead.common.block.entity.FoodHolderBlockEntity;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.util.Helpers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.Block;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class CommonHandlers {
    public static void register(BiConsumer<BlockEntityTooltip, Class<? extends Block>> registerBlock)
    {
        registerBlock.accept(FOOD_HOLDER, HomesteadBlock.class);
    }

    public static final BlockEntityTooltip FOOD_HOLDER = (level, state, pos, entity, tooltip) -> {
        if (entity instanceof FoodHolderBlockEntity holder && !holder.getStack().isEmpty())
        {
            holder.getStack().getCapability(FoodCapability.CAPABILITY).ifPresent(cap -> {
                List<Component> components = new LinkedList<>();
                holder.updatePreservation();
//                cap.getTraits().forEach(trait -> {
//                    trait.addTooltipInfo(holder.getStack(), components);
//                });
//                String all = String.join(", ", components.stream().map(Component::getString).toArray(String[]::new));
                tooltip.accept(new TextComponent(holder.getStack().getDisplayName().getString() + " x " + holder.getStack().getCount()));
//                if(!all.isBlank()) {
//                    tooltip.accept(new TextComponent(all));
//                }
                if(ClientHelpers.hasShiftDown()) {
                    cap.addTooltipInfo(holder.getStack(), components);
                    for(var component : components) {
                        tooltip.accept(component);
                    }
                }
            });
        }
    };
}
