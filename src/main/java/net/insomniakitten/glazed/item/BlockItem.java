package net.insomniakitten.glazed.item;

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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public final class BlockItem extends ItemBlock {
    @Nullable
    private final NameSuffix suffix;
    private final MetaFilter metaFilter;

    public BlockItem(Block block, MetaFilter metaFilter, @Nullable NameSuffix suffix) {
        super(block);
        this.metaFilter = metaFilter;
        this.suffix = suffix;
        setHasSubtypes(suffix != null);
    }

    public BlockItem(Block block, @Nullable NameSuffix suffix) {
        this(block, MetaFilter.ANY, suffix);
    }

    public BlockItem(Block block, MetaFilter metaFilter) {
        this(block, metaFilter, null);
    }

    public BlockItem(Block block) {
        this(block, MetaFilter.ANY, null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        return metaFilter.check(stack.getMetadata()) && super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = block.getUnlocalizedName();
        if (suffix != null) {
            name += "." + suffix.apply(stack);
        }
        return name;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        return metaFilter.check(stack) && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    }

    @Override
    public int getMetadata(int damage) {
        return hasSubtypes ? damage : 0;
    }

    public interface MetaFilter {
        MetaFilter ANY = meta -> true;

        boolean check(int meta);

        default boolean check(ItemStack stack) {
            return check(stack.getMetadata());
        }
    }

    public interface NameSuffix {
        String apply(ItemStack stack);
    }
}
