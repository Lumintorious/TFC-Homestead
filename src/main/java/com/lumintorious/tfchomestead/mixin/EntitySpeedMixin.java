package com.lumintorious.tfchomestead.mixin;

import com.lumintorious.tfchomestead.TFCHomestead;
import com.lumintorious.tfchomestead.common.TFCHomesteadConfig;
import com.lumintorious.tfchomestead.common.item.HomesteadItems;
import net.dries007.tfc.common.entities.TFCEntities;
import net.dries007.tfc.common.entities.livestock.horse.TFCDonkey;
import net.dries007.tfc.common.entities.livestock.horse.TFCHorse;
import net.dries007.tfc.common.entities.livestock.horse.TFCMule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntitySpeedMixin {
    private Entity self() {
        return (Entity) (Object) this;
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getBlockSpeedFactor(CallbackInfoReturnable<Float> callbackInfo) {
        if(callbackInfo.getReturnValue() < 1f) {
            if(self() instanceof TFCHorse || self() instanceof TFCDonkey || self() instanceof TFCMule && TFCHomesteadConfig.SERVER.enableRideableConstantSpeed.get()) {
                callbackInfo.setReturnValue(1f);
            } else if(self() instanceof Player player) {
                ItemStack main = player.getMainHandItem();
                ItemStack off = player.getOffhandItem();
                if(main.is(HomesteadItems.WALKING_CANE.get()) || off.is(HomesteadItems.WALKING_CANE.get())) {
                    float difference = 1f - callbackInfo.getReturnValueF();
                    callbackInfo.setReturnValue(1f - difference / 4);
                } else if(main.is(HomesteadItems.REFINED_WALKING_CANE.get()) || off.is(HomesteadItems.REFINED_WALKING_CANE.get())) {
                    callbackInfo.setReturnValue(1f);
                }

            }
        }
    }
}