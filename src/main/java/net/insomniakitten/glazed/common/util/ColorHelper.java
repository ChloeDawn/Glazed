package net.insomniakitten.glazed.common.util;

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

import net.insomniakitten.glazed.client.ClientHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorHelper {

    public static int getBiomeColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        if (state == null || tintIndex != 1) {
            return -1;
        } else {
            if (world != null && pos != null) {
                return BiomeColorHelper.getGrassColorAtPos(world, pos);
            }
            return ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    }

    public static int getBiomeColor(ItemStack stack, int tintIndex) {
        if (stack.isEmpty() || tintIndex != 1) {
            return -1;
        } else {
            if (ClientHelper.isWorldLoaded()) {
                return BiomeColorHelper.getGrassColorAtPos(ClientHelper.getWorld(), ClientHelper.getPlayerPos());
            }
            return ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    }

    public static BlockColors getBlockColors() {
        return Minecraft.getMinecraft().getBlockColors();
    }

    public static ItemColors getItemColors() {
        return Minecraft.getMinecraft().getItemColors();
    }

    public static void registerBiomeColorizer(Block block) {
        getBlockColors().registerBlockColorHandler(ColorHelper::getBiomeColor, block);
        getItemColors().registerItemColorHandler(ColorHelper::getBiomeColor, block);
    }

}
