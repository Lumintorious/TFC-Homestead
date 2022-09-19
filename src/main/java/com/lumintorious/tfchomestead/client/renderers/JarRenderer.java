package com.lumintorious.tfchomestead.client.renderers;

import com.lumintorious.tfchomestead.common.block.entity.JarBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.UUID;

import static net.dries007.tfc.client.RenderHelpers.BLOCKS_ATLAS;

@OnlyIn(Dist.CLIENT)
public class JarRenderer implements BlockEntityRenderer<JarBlockEntity> {
    @Override
    public void render(JarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getStack();
        FluidStack fluidStack = pBlockEntity.getFluidStack();
        float timeD = (float)(360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        pPoseStack.pushPose();
        if (stack != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5, 0.3, 0.5);
            pPoseStack.scale(0.50f, 0.50f, 0.50f);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(timeD));
            pPoseStack.translate(0f, Math.sin(timeD / 10) / 15, 0f);
            pPoseStack.pushPose();
            itemRenderer.renderStatic(
                stack,
                ItemTransforms.TransformType.FIXED,
                pPackedLight,
                pPackedOverlay,
                pPoseStack,
                pBufferSource,
                0
            );
            pPoseStack.popPose();
            pPoseStack.popPose();
        }

        if (fluidStack != null)
        {
            pPoseStack.pushPose();
            int color = RenderHelpers.getFluidColor(fluidStack.getFluid());
            float r = ((color >> 16) & 0xFF) / 255F;
            float g = ((color >> 8) & 0xFF) / 255F;
            float b = (color & 0xFF) / 255F;
            float a = ((color >> 24) & 0xFF) / 255F;

            RenderSystem.setShaderColor(r, g, b, a);

            var buffer = pBufferSource.getBuffer(RenderType.entityTranslucentCull(BLOCKS_ATLAS));
            var filled = (float) fluidStack.getAmount() / (float) pBlockEntity.getTank().getCapacity();

            RenderSystem.disableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            float inc = 1F / 16F;

            Vector3f p0 = new Vector3f(4.1f * inc, 0.1f * inc, 4.1f * inc);
            Vector3f p1 = new Vector3f(11.9f * inc, 8.9f * inc * filled, 11.9f * inc);


            Fluid fluid = fluidStack.getFluid();
            FluidAttributes attributes = fluid.getAttributes();
            ResourceLocation texture = attributes.getStillTexture(fluidStack);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(BLOCKS_ATLAS).apply(texture);

            Matrix4f matrix4f = pPoseStack.last().pose();

            buffer.vertex(matrix4f, p0.x(), p1.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p1.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            buffer.vertex(matrix4f, p1.x(), p0.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p0.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p0.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p0.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            buffer.vertex(matrix4f, p1.x(), p0.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p1.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p0.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            buffer.vertex(matrix4f, p0.x(), p0.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p1.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p0.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            buffer.vertex(matrix4f, p1.x(), p0.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p0.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p1.x(), p1.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            buffer.vertex(matrix4f, p0.x(), p0.y(), p0.z()).color(color).uv(sprite.getU0(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p0.y(), p1.z()).color(color).uv(sprite.getU0(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p1.y(), p1.z()).color(color).uv(sprite.getU1(), sprite.getV1()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();
            buffer.vertex(matrix4f, p0.x(), p1.y(), p0.z()).color(color).uv(sprite.getU1(), sprite.getV0()).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(0, 0, 1).endVertex();

            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.depthMask(false);
            RenderSystem.disableBlend();
            pPoseStack.popPose();
        }

        pPoseStack.popPose();

    }
}
