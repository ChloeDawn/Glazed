package net.insomniakitten.glazed.glass;

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
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockGlass extends Block {

    private static final PropertyEnum<GlassType> GLASS_TYPE = PropertyEnum.create("type", GlassType.class);

    public BlockGlass() {
        super(Material.GLASS);
        setRegistryName("glass");
        setUnlocalizedName(Glazed.MOD_ID + ".glass");
        setCreativeTab(Glazed.TAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = world.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if (state != iblockstate) {
            return true;
        } else if (block == this) {
            return false;
        } else return false;
    }

    private GlassType getType(IBlockState state) {
        return state.getValue(GLASS_TYPE);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return getType(state).equals(GlassType.REINFORCED) ? SoundType.METAL : SoundType.GLASS;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!tab.equals(this.getCreativeTabToDisplayOn())) return;
        for (int i = 0; i < GlassType.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getLightValue(IBlockState state) {
        return getType(state).getLightLevel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getType(state).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int max = GlassType.values().length;
        return this.getDefaultState().withProperty(GLASS_TYPE, GlassType.values()[meta % max]);
    }

    @Override @Nonnull
    public IBlockState getStateForPlacement(
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing facing,
            float hitX, float hitY,
            float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target,
            World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!getType(state).equals(GlassType.REINFORCED))
            drops.clear();
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public boolean canSilkHarvest() {
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return getType(state).isOpaque() ? 255 : 0;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return getType(state).equals(GlassType.REINFORCED);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return getType(state).equals(GlassType.ENERGETIC);
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getType(state).equals(GlassType.ENERGETIC) ? 15 : 0;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getType(state).equals(GlassType.ENERGETIC) ? 15 : 0;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GLASS_TYPE);
    }

}
