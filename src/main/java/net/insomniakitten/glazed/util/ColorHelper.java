package net.insomniakitten.glazed.util;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ColorHelper {

    private ColorHelper() {}

    public static int getBiomeColor(IBlockAccess world, BlockPos pos) {
        if (world != null && pos != null) {
            return BiomeColorHelper.getGrassColorAtPos(world, pos);
        }
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    public static int getBiomeColor() {
        if (ClientHelper.isWorldLoaded()) {
            World world = ClientHelper.getWorld();
            BlockPos pos = ClientHelper.getPlayerPos();
            return BiomeColorHelper.getGrassColorAtPos(world, pos);
        }
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

}
