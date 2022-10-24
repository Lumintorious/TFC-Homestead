package com.lumintorious.tfchomestead.common.block;

import net.dries007.tfc.common.items.Food;

public enum Grain
{
    WHEAT(Food.WHEAT),
    BARLEY(Food.BARLEY),
    OAT(Food.OAT),
    MAIZE(Food.MAIZE),
    RYE(Food.RYE_GRAIN),
    RICE(Food.RICE);

    private final Food food;

    Grain(Food food)
    {
        this.food = food;
    }

    public Food getFood()
    {
        return food;
    }
}
