package net.insomniakitten.glazed;

/*
 *  Copyright 2018 InsomniaKitten
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.function.IntFunction;

import static net.insomniakitten.glazed.GlazedRegistry.GLASS_KILN;
import static net.insomniakitten.glazed.block.GlassKilnBlock.HALF;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.SRC_COLOR;
import static net.minecraft.client.renderer.GlStateManager.DestFactor.ZERO;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.DST_COLOR;
import static net.minecraft.client.renderer.GlStateManager.SourceFactor.ONE;

@SideOnly(Side.CLIENT)
enum GlazedClient implements IResourceManagerReloadListener {
    INSTANCE;

    private final TextureAtlasSprite[] destroyStageSprites = new TextureAtlasSprite[10];

    private final IntFunction<TextureAtlasSprite> spriteIntFunction = index ->
            FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite(
                    "minecraft:blocks/destroy_stage_" + index
            );

    @Override
    public void onResourceManagerReload(IResourceManager rm) {
        Glazed.LOGGER.debug("Reloading destroy stage sprite cache");
        Arrays.setAll(destroyStageSprites, spriteIntFunction);
    }

    @SubscribeEvent
    protected void onRenderWorldLast(RenderWorldLastEvent event) {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc.playerController == null) return;
        if (!mc.playerController.getIsHittingBlock()) return;

        final BlockPos curr = mc.playerController.currentBlock;
        final IBlockState state = mc.world.getBlockState(curr);

        if (state.getBlock() != GLASS_KILN) return;

        final int destroyStage = (int) (mc.playerController.curBlockDamageMP * 10.0F) - 1;

        if (destroyStage < 0 || destroyStage > 10) return;

        try {
            GlStateManager.pushMatrix();
            GlStateManager.tryBlendFuncSeparate(DST_COLOR, SRC_COLOR, ONE, ZERO);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder buffer = tessellator.getBuffer();
            final Vec3d vec = getPositionVector(mc.player, event.getPartialTicks());
            final TextureAtlasSprite sprite = destroyStageSprites[destroyStage];
            final BlockPos pos = state.getValue(HALF).isUpper() ? curr.down() : curr.up();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            buffer.setTranslation(-vec.x, -vec.y, -vec.z);
            buffer.noColor();

            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getBlockRendererDispatcher().renderBlockDamage(state, pos, sprite, mc.world);

            buffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } catch (Exception e) {
            Glazed.LOGGER.error("Caught exception whilst rendering destroy stage quads", e);
        } finally {
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.depthMask(true);
            GlStateManager.popMatrix();
        }
    }

    private Vec3d getPositionVector(Entity entity, float partialTicks) {
        final double lastX = entity.lastTickPosX;
        final double lastY = entity.lastTickPosY;
        final double lastZ = entity.lastTickPosZ;
        final double posX = lastX + ((entity.posX - lastX) * partialTicks);
        final double posY = lastY + ((entity.posY - lastY) * partialTicks);
        final double posZ = lastZ + ((entity.posZ - lastZ) * partialTicks);
        return new Vec3d(posX, posY, posZ);
    }
}
