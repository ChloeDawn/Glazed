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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorManager {

    public static final String CLIENT = "net.insomniakitten.glazed.client.ColorManager$ClientWrapper";
    public static final String SERVER = "net.insomniakitten.glazed.client.ColorManager$ServerWrapper";

    private static final BlockPos.MutableBlockPos POS = new BlockPos.MutableBlockPos();
    // Used in ColorManager#getBiomeColor(stack, tintIndex)

    public static class ClientWrapper extends ColorManager {
        @Override @SideOnly(Side.CLIENT)
        public void registerColorHandler() {
            Glazed.Logger.info(true, "Client instance - registering color handler");
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
                    ColorManager::getBiomeColor, Glazed.GLASS);
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
                    ColorManager::getBiomeColor, Glazed.GLASS);
        }
    }

    public static class ServerWrapper extends ColorManager {
        @Override @SideOnly(Side.SERVER)
        public void registerColorHandler() {
            Glazed.Logger.info(true, "Server instance - skipping color handler registration");
        }
    }

    public void registerColorHandler() {
        // no-op
    }

    @SideOnly(Side.CLIENT)
    private static int getBiomeColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        if (tintIndex != 1) return -1;
        if (world != null && pos != null) {
            return BiomeColorHelper.getGrassColorAtPos(world, pos);
        } return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    @SideOnly(Side.CLIENT)
    private static int getBiomeColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) return -1;
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!stack.isEmpty() && player != null && player.world != null) {
            POS.setPos(player.posX, player.posY, player.posZ);
            // More accurate than EntityPlayer#getPosition
            return BiomeColorHelper.getGrassColorAtPos(player.world, POS);
        } return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

}
