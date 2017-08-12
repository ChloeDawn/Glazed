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

import java.util.List;

public class BlockGlass extends Block {

    public BlockGlass() {
        super(Material.GLASS);
        setRegistryName("glass");
        setUnlocalizedName(Glazed.MOD_ID + ".glass");
        setCreativeTab(Glazed.TAB);
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return GlassBlockType.getType(state).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return GlassBlockType.getType(world.getBlockState(pos)).getResistance();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = world.getBlockState(pos.offset(side));
        return state != iblockstate;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return GlassBlockType.getType(state).getSoundType();
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
        for (int i = 0; i < GlassBlockType.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override public int damageDropped(IBlockState state) {
        return GlassBlockType.getType(state).ordinal();
    }

    @Override
    public int getLightValue(IBlockState state) {
        return GlassBlockType.getType(state).getLightLevel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return GlassBlockType.getType(state).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        GlassBlockType type = GlassBlockType.getType(meta);
        return this.getDefaultState().withProperty(GlassBlockType.getProperty(), type);
    }

    @Override
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing facing,
            float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target,
            World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer.equals(GlassBlockType.getType(state).getRenderLayer());
    }

    @Override
    public void getDrops(
            NonNullList<ItemStack> drops, IBlockAccess world,
            BlockPos pos, IBlockState state, int fortune) {
        if (GlassBlockType.getType(state).dropsItem()) {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }

    @Override
    public boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean isToolEffective(String toolType, IBlockState state) {
        return GlassBlockType.getType(state).isHeavy() && toolType.equals("pickaxe");
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return GlassBlockType.getType(state).isOpaque() ? 255 : 0;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return GlassBlockType.getType(state).getResistance() >= 3000;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return GlassBlockType.getType(state).isPowered();
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return GlassBlockType.getType(state).isPowered() ? 15 : 0;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return GlassBlockType.getType(state).isPowered() ? 15 : 0;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        boolean isVoidic = GlassBlockType.getType(state).equals(GlassBlockType.VOIDIC);
        if (!isVoidic || entity.isSneaking()) {
            return;
        }
        if (entity.motionY <= 0.0D) {
            entity.motionY *= 0.4D;
        }
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
        entity.fallDistance = 0.0F;
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void addCollisionBoxToList(
            IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb,
            List<AxisAlignedBB> collisions, Entity entity, boolean flag) {
        boolean isVoidic = GlassBlockType.getType(state).equals(GlassBlockType.VOIDIC);
        if (!isVoidic || entity == null || !(entity instanceof EntityPlayer) || entity.isSneaking())
            super.addCollisionBoxToList(state, world, pos, aabb, collisions, entity, flag);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GlassBlockType.getProperty());
    }

}
