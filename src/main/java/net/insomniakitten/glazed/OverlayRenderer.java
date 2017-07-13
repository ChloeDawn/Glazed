package net.insomniakitten.glazed;

/*
 *  Copyright 2017 InsomniaKitten
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

import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.material.BlockMaterial;
import net.insomniakitten.glazed.glass.GlassType;
import net.insomniakitten.glazed.material.MaterialType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class OverlayRenderer {

    public static final HashMap<GlassType, ResourceLocation> GLASS =
            new HashMap<GlassType, ResourceLocation>();
    public static final HashMap<MaterialType, ResourceLocation> MATERIALS =
            new HashMap<MaterialType, ResourceLocation>();

    static {
        GLASS.put(GlassType.VOIDIC, new ResourceLocation(Glazed.MOD_ID, "textures/overlay/voided.png"));
        MATERIALS.put(MaterialType.SLAG, new ResourceLocation(Glazed.MOD_ID, "textures/overlay/slag.png"));
        // TODO: Move to enums
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (!event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) return;
        int w = event.getResolution().getScaledWidth(), h = event.getResolution().getScaledHeight();
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.player.world;
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + mc.player.eyeHeight, mc.player.posZ);
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockGlass
                && GLASS.containsKey(GlassType.getType(state))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            mc.renderEngine.bindTexture(GLASS.get(GlassType.getType(state)));
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, w, h);
            GlStateManager.popMatrix();
        }

        if (state.getBlock() instanceof BlockMaterial
                && MATERIALS.containsKey(MaterialType.getType(state))){
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            mc.renderEngine.bindTexture(MATERIALS.get(MaterialType.getType(state)));
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, w, h);
            GlStateManager.popMatrix();
        }

    }

}
