package net.insomniakitten.glazed.client;

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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.common.kiln.BlockKiln;
import net.insomniakitten.glazed.common.kiln.TileKiln;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Glazed.MOD_ID, value = Side.CLIENT)
public class KilnDebugRenderer {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (Glazed.isDeobf() && ClientHelper.isWorldLoaded()) {
            BlockPos posAt = ClientHelper.getRayTrace().getBlockPos();
            IBlockState stateAt = ClientHelper.getWorld().getBlockState(posAt);
            if (stateAt.getBlock() instanceof BlockKiln) {
                BlockPos tilePos = BlockKiln.getTilePos(stateAt, posAt);
                TileEntity tileAt = ClientHelper.getWorld().getTileEntity(tilePos);
                if (tileAt != null && tileAt instanceof TileKiln) {
                    String active = String.valueOf(((TileKiln) tileAt).isActive);
                    String progress = String.valueOf(((TileKiln) tileAt).getProgress());
                    event.getLeft().add(tileAt.getClass().getSimpleName() + " @ " + tilePos.toString());
                    event.getLeft().add("active: " + active + ", progress: " + progress);
                }
            }
        }
    }

}
