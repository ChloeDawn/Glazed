package net.insomniakitten.glazed.client.render.selection;

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
import net.insomniakitten.glazed.util.CollectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Glazed.ID, value = Side.CLIENT)
public final class RenderSelectionBoxes {

    private RenderSelectionBoxes() {}

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();
        //noinspection ConstantConditions
        if (pos == null) return;
        ImmutableList<Block> blocks = GlazedRegistry.BLOCK_FACTORY.entries();
        EntityPlayer player = event.getPlayer();
        IBlockState state = player.getEntityWorld().getBlockState(pos);
        float partialTicks = event.getPartialTicks();
        CollectionHelper.forThoseOf(blocks, ISelectionBoxRenderer.class, (block, iface) -> {
            if (state.getBlock().getClass().isAssignableFrom(block.getClass())) {
                ISelectionBoxRenderer.renderSelectionBoxes(state, pos, iface, player, partialTicks);
            }
        });
    }

}
