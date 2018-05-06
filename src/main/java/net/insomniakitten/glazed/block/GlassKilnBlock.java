package net.insomniakitten.glazed.block;

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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.block.entity.GlassKilnEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public final class GlassKilnBlock extends Block {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Plane.HORIZONTAL);
    public static final PropertyEnum<Half> HALF = PropertyEnum.create("half", Half.class);

    public GlassKilnBlock() {
        super(Material.ROCK, MapColor.ADOBE);
        setUnlocalizedName(Glazed.ID + ".glass_kiln");
        setCreativeTab(Glazed.TAB);
        setSoundType(SoundType.STONE);
        setHardness(5.0F);
        setResistance(30.0F);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        boolean active = (meta & 1) != 0;
        Half half = Half.values()[((meta & 2) >> 1)];
        EnumFacing facing = EnumFacing.getHorizontal(meta >> 2);
        return this.getDefaultState()
                .withProperty(ACTIVE, active)
                .withProperty(HALF, half)
                .withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int active = state.getValue(ACTIVE) ? 1 : 0;
        int half = state.getValue(HALF).ordinal() << 1;
        int facing = state.getValue(FACING).getHorizontalIndex() << 2;
        return active | half | facing;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return state.getValue(HALF).selectionBox.offset(pos);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state)) {
            world.removeTileEntity(pos);
            world.destroyBlock(pos.up(), false);
        } else world.destroyBlock(pos.down(), false);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            final BlockPos up = pos.up();
            if (world.getBlockState(up).getBlock().isReplaceable(world, up)) {
                return true;
            } else {
                final BlockPos down = pos.down();
                return world.getBlockState(down).getBlock().isReplaceable(world, down);
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        world.setBlockState(state.getValue(HALF) == Half.LOWER ? pos.up() : pos.down(), state.cycleProperty(HALF));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, FACING, HALF);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 8 : 0;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return state.getValue(HALF) == Half.LOWER && face != EnumFacing.DOWN && face != state.getValue(FACING);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(HALF) == Half.LOWER;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new GlassKilnEntity();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        Half half = world.getBlockState(pos.down()).getBlock().isReplaceable(world, pos.down()) ? Half.UPPER : Half.LOWER;
        return getDefaultState().withProperty(HALF, half).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public enum Half implements IStringSerializable {
        LOWER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D)),
        UPPER(new AxisAlignedBB(0.0D, -1.0D, 0.0D, 1.0D, 1.0D, 1.0D));

        private final AxisAlignedBB selectionBox;

        Half(AxisAlignedBB selectionBox) {
            this.selectionBox = selectionBox;
        }

        public boolean isLower() {
            return this == LOWER;
        }

        public boolean isUpper() {
            return this == UPPER;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
