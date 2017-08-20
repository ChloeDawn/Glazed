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
import net.insomniakitten.glazed.glass.GlassType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorManager {

    public static class ClientWrapper extends ColorManager {
        @Override @SideOnly(Side.CLIENT)
        public void registerColorHandler() {
            Glazed.LOGGER.info("Client instance - registering color handler");
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
                    new IBlockColor() {
                        @Override @SideOnly(Side.CLIENT)
                        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int index) {
                            if (GlassType.get(state).equals(GlassType.GAIA)) {
                                if (world != null && pos != null) {
                                    return BiomeColorHelper.getGrassColorAtPos(world, pos);
                                }
                                return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                            }
                            return -1;
                        }
                    }, Glazed.ModBlocks.GLASS.get());
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
                    new IItemColor() {
                        @Override @SideOnly(Side.CLIENT)
                        public int getColorFromItemstack(ItemStack stack, int index) {
                            EntityPlayer player = Minecraft.getMinecraft().player;
                            GlassType type = GlassType.get(stack.getMetadata());
                            if (player != null && type.equals(GlassType.GAIA)) {
                                if (player.world != null) {
                                    return BiomeColorHelper.getGrassColorAtPos(player.world, player.getPosition());
                                }
                                return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                            }
                            return -1;
                        }
                    }, Item.getItemFromBlock(Glazed.ModBlocks.GLASS.get()));
        }
    }

    public static class ServerWrapper extends ColorManager {
        @Override @SideOnly(Side.CLIENT)
        public void registerColorHandler() {
            Glazed.LOGGER.info("Server instance - skipping color handler registration");
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerColorHandler() {
        // no-op
    }

}
