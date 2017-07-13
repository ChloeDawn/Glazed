package net.insomniakitten.glazed.material;

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

import com.sun.istack.internal.NotNull;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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
import java.util.Random;

public class BlockMaterial extends Block {

    public BlockMaterial() {
        super(Material.ROCK);
        setRegistryName("material");
        setUnlocalizedName(Glazed.MOD_ID + ".material");
        setCreativeTab(Glazed.TAB);
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return MaterialType.getType(state).getMaterial();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return MaterialType.getType(state).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return MaterialType.getType(world.getBlockState(pos)).getResistance();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer.equals(MaterialType.getType(state).getRenderLayer());
    }

    @Override
    @Nonnull
    public SoundType getSoundType(
            IBlockState state, World world,
            BlockPos pos, @Nullable Entity entity) {
        return MaterialType.getType(state).getSoundType();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < MaterialType.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return MaterialType.getType(state).getLightLevel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return MaterialType.getType(state).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(
                MaterialType.getProperty(),
                MaterialType.getType(meta));
    }

    @Override
    @Nonnull
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

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return MaterialType.getType(state).isSolid();
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return MaterialType.getType(state).isSolid();
    }

    @Override
    public void onEntityCollidedWithBlock(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Entity entity) {
        if (!MaterialType.getType(state).equals(MaterialType.SLAG)) return;
        if (entity.motionY < 0.0D)
            entity.motionY *= 0.005D;
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
        entity.fallDistance = 0.0F;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (MaterialType.getType(state).equals(MaterialType.CRYSTAL))
            return new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.625, 0.9);
        else return super.getBoundingBox(state, source, pos);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        if (MaterialType.getType(state).equals(MaterialType.SLAG))
            return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1.0D, pos.getY() + 0.0625D, pos.getZ() + 1.0D);
        return super.getCollisionBoundingBox(state, world, pos);
    }

    public void neighborChanged(IBlockState state, World world,
                                BlockPos pos, Block block, BlockPos fromPos) {
        if (!MaterialType.getType(state).equals(MaterialType.CRYSTAL)) return;

        boolean isBlockBelowEndStone = world.getBlockState(pos.down()).getBlock().equals(Blocks.END_STONE);
        SoundEvent sound = MaterialType.getType(state).getSoundType().getBreakSound();

        if (!world.isRemote && !isBlockBelowEndStone && !world.isAirBlock(pos)) {
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
            BlockPos dropPos = new BlockPos(pos.getX(), pos.getY() - 0.5, pos.getZ());
            dropBlockAsItem(world, dropPos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        double d0 = pos.getX(), d1 = pos.getY(), d2 = pos.getZ();
        double d3 = ((float) pos.getX() + rand.nextFloat());
        double d4 = ((float) pos.getY() + 0.8F);
        double d5 = ((float) pos.getZ() + rand.nextFloat());
        if (MaterialType.getType(state).equals(MaterialType.CRYSTAL)) {
            if (world.getBlockState(pos.up()).getMaterial() == Material.AIR
                    && !world.getBlockState(pos.up()).isOpaqueCube()) {
                world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d3, d4, d5, 0.0D, 0.0D, 0.0D);
            }
        }
        if (MaterialType.getType(state).equals(MaterialType.SLAG)) {
            if (world.getBlockState(pos.up()).getMaterial() == Material.AIR
                    && !world.getBlockState(pos.up()).isOpaqueCube()) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                if (rand.nextInt(200) == 0)
                    world.playSound(
                            d0, d1, d2,
                            SoundEvents.BLOCK_LAVA_AMBIENT,
                            SoundCategory.BLOCKS,
                            0.2F + rand.nextFloat() * 0.2F,
                            0.9F + rand.nextFloat() * 0.15F,
                            false);
            }
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)  {
        IBlockState iblockstate = world.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return block != this;
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return MaterialType.getType(state).equals(MaterialType.CRYSTAL) && type.equals("pickaxe");
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MaterialType.getProperty());
    }

}
