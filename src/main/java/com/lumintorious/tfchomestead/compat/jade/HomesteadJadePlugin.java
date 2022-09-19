package com.lumintorious.tfchomestead.compat.jade;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.dries007.tfc.compat.jade.common.EntityTooltip;
import net.dries007.tfc.compat.jade.common.EntityTooltips;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

@WailaPlugin
@SuppressWarnings("UnstableApiUsage")
public class HomesteadJadePlugin implements IWailaPlugin
{
    @Override
    public void registerClient(IWailaClientRegistration registry)
    {
        CommonHandlers.register((tooltip, aClass) -> register(registry, tooltip, aClass));
    }

    private void register(IWailaClientRegistration registry, BlockEntityTooltip blockEntityTooltip, Class<? extends Block> blockClass)
    {
        registry.registerComponentProvider((tooltip, access, config) -> blockEntityTooltip.display(access.getLevel(), access.getBlockState(), access.getPosition(), access.getBlockEntity(), tooltip::add), TooltipPosition.BODY, blockClass);
    }

    private void register(IWailaClientRegistration registry, EntityTooltip entityTooltip, Class<? extends Entity> entityClass)
    {
        registry.registerComponentProvider((tooltip, access, config) -> entityTooltip.display(access.getLevel(), access.getEntity(), tooltip::add), TooltipPosition.BODY, entityClass);
    }
}
