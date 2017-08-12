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
        return MaterialBlockType.getType(state).getMaterial();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return MaterialBlockType.getType(state).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        return MaterialBlockType.getType(state).getResistance();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer.equals(MaterialBlockType.getType(state).getRenderLayer());
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return MaterialBlockType.getType(state).getSoundType();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < MaterialBlockType.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return MaterialBlockType.getType(state).getMetadata();
    }

    @Override
    public int getLightValue(IBlockState state) {
        return MaterialBlockType.getType(state).getLightLevel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return MaterialBlockType.getType(state).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(
                MaterialBlockType.getProperty(),
                MaterialBlockType.getType(meta));
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
    public boolean isFullCube(IBlockState state) {
        return MaterialBlockType.getType(state).isSolid();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return MaterialBlockType.getType(state).isSolid();
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return MaterialBlockType.getType(state).isSolid() && super.causesSuffocation(state);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.SLAG)) {
            if (entity.motionY < 0.0D) {
                entity.motionY *= 0.005D;
            }
            entity.motionX *= 0.4D;
            entity.motionZ *= 0.4D;
            entity.fallDistance = 0.0F;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.CRYSTAL))
            return new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.625, 0.9);
        else {
            return super.getBoundingBox(state, source, pos);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.SLAG)) {
            return new AxisAlignedBB(
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1.0D, pos.getY() + 0.0625D, pos.getZ() + 1.0D);
        } else {
            return super.getCollisionBoundingBox(state, world, pos);
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        double offsetX = x + rand.nextFloat(), offsetY = y + 0.8F, offsetZ = z + rand.nextFloat();
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.CRYSTAL)
                && world.getBlockState(pos.up()).getMaterial() == Material.AIR
                && !world.getBlockState(pos.up()).isOpaqueCube()) {
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, offsetX, offsetY, offsetZ, 0.0D, 0.0D, 0.0D);

        }
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.SLAG)
                && world.getBlockState(pos.up()).getMaterial() == Material.AIR
                && !world.getBlockState(pos.up()).isOpaqueCube()) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX, offsetY, offsetZ, 0.0D, 0.0D, 0.0D);
            if (rand.nextInt(200) == 0) {
                world.playSound(x, y, z, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS,
                        0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }
    }

    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (MaterialBlockType.getType(state).equals(MaterialBlockType.CRYSTAL)) {
            boolean canStay = this.isTopSolid(world.getBlockState(pos.down()));
            if (!world.isRemote && !canStay && !world.isAirBlock(pos)) {
                SoundEvent sound = MaterialBlockType.getType(state).getSoundType().getBreakSound();
                world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
                BlockPos dropPos = new BlockPos(pos.getX(), pos.getY() - 0.5, pos.getZ());
                dropBlockAsItem(world, dropPos, state, 0);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)  {
        IBlockState iblockstate = world.getBlockState(pos.offset(side));
        MaterialBlockType type = MaterialBlockType.getType(iblockstate);
        return (iblockstate.getBlock() == this && type.equals(MaterialBlockType.SLAG))
                || super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public boolean isToolEffective(String toolType, IBlockState state) {
        return MaterialBlockType.getType(state).equals(MaterialBlockType.CRYSTAL) && toolType.equals("pickaxe");
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MaterialBlockType.getProperty());
    }

}
