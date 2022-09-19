package com.lumintorious.tfchomestead.client.renderers;

import com.lumintorious.tfchomestead.common.block.entity.HangerBlockEntity;
import com.lumintorious.tfchomestead.common.block.entity.JarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
class HangerRenderer implements BlockEntityRenderer<HangerBlockEntity> {

    public void render(HangerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getStack();
        float timeD = (float)(360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        int totalDraws = 4;
        int maxStackSize = Math.min(Math.max(stack.getItem().getItemStackLimit(stack), 1), 64);
        float filled = (float) stack.getCount() / (float) maxStackSize;

        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.5, 0.5);
        pPoseStack.scale(0.45f, 0.45f, 0.45f);
//            RenderHelper.enableStandardItemLighting();
        int currentDraws = 0;
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(timeD));
        pPoseStack.translate(0, 1f, 0);
        for (int i = 0 ; i < 4; i++) {
            pPoseStack.translate(0, -1f, 0);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees((timeD + 22.5f) % 360));
            if(currentDraws >= filled * totalDraws) {
                break;
            } else {
                currentDraws += 1;
            }
            itemRenderer.renderStatic(
                    stack,
                    ItemTransforms.TransformType.FIXED,
                    pPackedLight,
                    pPackedOverlay,
                    pPoseStack,
                    pBufferSource,
                    0
            );
        }

//            RenderHelper.disableStandardItemLighting();
        pPoseStack.popPose();
        // 1: 1 = 16
        // 2: 2 = 16, 1 = 8
        // 64: 4 = 1, 16 = 4, 32 = 8, 64 = 16


    }
}

