package net.insomniakitten.glazed.block;

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

import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.client.ClientHelper;
import net.insomniakitten.glazed.client.SpecialsParser;
import net.insomniakitten.glazed.client.color.IBlockColorProvider;
import net.insomniakitten.glazed.client.color.IItemColorProvider;
import net.insomniakitten.glazed.data.GlassVariants;
import net.insomniakitten.glazed.item.ItemBlockEnumBase;
import net.insomniakitten.glazed.util.ColorHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public final class BlockGlass extends BlockEnumBase<GlassVariants> implements IBlockColorProvider {

    public BlockGlass() {
        super("glass", GlassVariants.class);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockGlass(this);
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state != world.getBlockState(pos.offset(side)) && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return getVariant(state) == GlassVariants.GAIA ? ColorHelper.getBiomeColor(world, pos) : 0;
    }

    protected final class ItemBlockGlass extends ItemBlockEnumBase<GlassVariants> implements IItemColorProvider {

        public ItemBlockGlass(BlockEnumBase<GlassVariants> block) {
            super(block);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int getItemColor(ItemStack stack, int tintIndex) {
            return getVariant(stack.getMetadata()) == GlassVariants.GAIA ? ColorHelper.getBiomeColor() : 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getItemStackDisplayName(ItemStack stack) {
            String type = getVariant(stack.getMetadata()).getName();
            Pair<UUID, String> match = Pair.of(ClientHelper.getPlayerUUID(), type);
            return SpecialsParser.DATA.keySet().contains(match)
                   ? SpecialsParser.DATA.get(match)
                   : super.getItemStackDisplayName(stack);
        }

    }

}
