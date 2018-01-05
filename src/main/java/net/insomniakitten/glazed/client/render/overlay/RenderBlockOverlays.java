package net.insomniakitten.glazed.client.render.overlay;

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

import com.google.common.collect.ImmutableList;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.GlazedRegistry;
import net.insomniakitten.glazed.client.ClientHelper;
import net.insomniakitten.glazed.util.CollectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Glazed.ID, value = Side.CLIENT)
public final class RenderBlockOverlays {

    private RenderBlockOverlays() {}

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (ClientHelper.isWorldLoaded() && event.getType().equals(ElementType.ALL)) {
            ImmutableList<Block> blocks = GlazedRegistry.BLOCK_FACTORY.entries();
            BlockPos pos = ClientHelper.getPlayerPos().up();
            IBlockState state = ClientHelper.getWorld().getBlockState(pos);
            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight();

            CollectionHelper.forThoseOf(blocks, IOverlayRenderer.class, (block, renderer) -> {
                if (state.getBlock() == block) {
                    IOverlayRenderer.renderOverlay(state, renderer, width, height);
                }
            });
        }
    }

}
