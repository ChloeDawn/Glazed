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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockGlass extends Block {

    public BlockGlass() {
        super(Material.GLASS);
        setRegistryName("glass");
        setUnlocalizedName(Glazed.MOD_ID + ".glass");
        setCreativeTab(Glazed.TAB);
    }

    @Override
    public float getBlockHardness(
            IBlockState state,
            World world,
            BlockPos pos) {
        return GlassType.getType(state)
                .getHardness();
    }

    @Override
    public float getExplosionResistance(
            World world,
            BlockPos pos,
            @Nullable Entity exploder,
            Explosion explosion) {
        return GlassType.getType(
                world.getBlockState(pos))
                .getResistance();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(
            IBlockState state,
            @Nonnull IBlockAccess world,
            @Nonnull BlockPos pos,
            EnumFacing side) {
        IBlockState iblockstate = world.getBlockState(
                pos.offset(side));
        Block block = iblockstate.getBlock();
        if (state != iblockstate) {
            return true;
        } else if (block == this) {
            return false;
        } else return false;
    }

    @Override @Nonnull
    public SoundType getSoundType(
            IBlockState state,
            World world,
            BlockPos pos,
            @Nullable Entity entity) {
        return GlassType.getType(state)
                .getSoundType();
    }

    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isFullBlock(IBlockState state) { return false; }

    @Override
    public void getSubBlocks(
            CreativeTabs tab,
            NonNullList<ItemStack> items) {
        for (int i = 0; i < GlassType.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override public int damageDropped(IBlockState state) {
        return GlassType.getType(state)
                .ordinal();
    }

    @Override
    public int getLightValue(IBlockState state) {
        return GlassType.getType(state)
                .getLightLevel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return GlassType.getType(state)
                .getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(
                GlassType.getProperty(),
                GlassType.getType(meta));
    }

    @Override @Nonnull
    public IBlockState getStateForPlacement(
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing facing,
            float hitX, float hitY,
            float hitZ, int meta,
            @Nonnull EntityLivingBase placer,
            EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override @Nonnull
    public ItemStack getPickBlock(
            @Nonnull IBlockState state,
            RayTraceResult target,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            EntityPlayer player) {
        return new ItemStack(this, 1,
                getMetaFromState(state));
    }

    @Override
    public boolean canRenderInLayer(
            IBlockState state,
            BlockRenderLayer layer) {
        return layer.equals(
                GlassType.getType(state)
                .getRenderLayer());
    }

    @Override
    public void getDrops(
            @Nonnull NonNullList<ItemStack> drops,
            IBlockAccess world,
            BlockPos pos,
            @Nonnull IBlockState state,
            int fortune) {
        if (!GlassType.getType(state).dropsItem())
            return;
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override public boolean canSilkHarvest() { return true; }

    @Override
    public boolean isToolEffective(
            String toolType,
            @Nonnull IBlockState state) {
        return GlassType.getType(state).isHeavy()
                && toolType.equals("pickaxe");
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return GlassType.getType(state)
                .isOpaque() ? 255 : 0;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return GlassType.getType(state)
                .getResistance() >= 3000;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return GlassType.getType(state)
                .isPowered();
    }

    @Override
    public int getStrongPower(
            IBlockState state,
            IBlockAccess world,
            BlockPos pos,
            EnumFacing side) {
        return GlassType.getType(state)
                .isPowered() ? 15 : 0;
    }

    @Override
    public int getWeakPower(
            IBlockState state,
            IBlockAccess world,
            BlockPos pos,
            EnumFacing side) {
        return GlassType.getType(state)
                .isPowered() ? 15 : 0;
    }

    @Override
    public void onEntityCollidedWithBlock(
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull IBlockState state,
            @Nonnull Entity entity) {
        if (!GlassType.getType(state)
                .equals(GlassType.VOIDIC))
            return;
        if (entity.isSneaking())
            return;
        if (entity.motionY <= 0.0D)
            entity.motionY *= 0.4D;
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
        entity.fallDistance = 0.0F;
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    public void addCollisionBoxToList(
            IBlockState state,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull AxisAlignedBB aabb,
            @Nonnull List<AxisAlignedBB> collisions,
            @Nullable Entity entity,
            boolean p_185477_7_) {
        if (!GlassType.getType(state).equals(GlassType.VOIDIC)
                || entity == null
                || !(entity instanceof EntityPlayer)
                || entity.isSneaking())
            super.addCollisionBoxToList(
                    state, world, pos, aabb,
                    collisions, entity, p_185477_7_);
    }

    @Override @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(
                this, GlassType.getProperty());
    }

}
