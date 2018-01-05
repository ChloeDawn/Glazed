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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.block.base.IPropertyProvider;
import net.insomniakitten.glazed.item.ItemBlockBase;
import net.insomniakitten.glazed.tile.TileKilnMaster;
import net.insomniakitten.glazed.tile.TileKilnSlave;
import net.insomniakitten.glazed.util.BoundingBoxHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;

public final class BlockKiln extends BlockEnumBase<BlockKiln.Section> {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockKiln() {
        super("kiln", "section", Section.class, false);
    }

    private void breakStructureFrame(World world, BlockPos tile, EnumFacing facing) {
        for (Section section : Section.values()) {
            BlockPos pos = section.getSlavePosFromMaster(tile, facing);
            world.removeTileEntity(pos);
            world.setBlockToAir(pos);
        }
    }

    private TileKilnMaster getMasterTile(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        TileKilnMaster master = null;
        if (tile != null) {
            if (getVariant(state).isSlave()) {
                BlockPos newPos = ((TileKilnSlave) tile).getMasterPos();
                master = (TileKilnMaster) world.getTileEntity(newPos);
            } else master = (TileKilnMaster) tile;
        }
        return master;
    }

    private EnumFacing getFacing(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getActualState(world, pos).getValue(FACING);
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileKilnMaster tile = getMasterTile(state, world, pos);
        return state.withProperty(FACING, tile != null ? tile.getFacing() : NORTH);
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return getVariant(state).isSlave()
               ? EnumBlockRenderType.INVISIBLE
               : EnumBlockRenderType.MODEL;
        // FIXME: Remove after segmenting model elements
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileKilnMaster tile = getMasterTile(state, world, pos);
        if (tile != null) {
            breakStructureFrame(world, tile.getPos(), tile.getFacing());
            world.removeTileEntity(tile.getPos());
            world.setBlockToAir(tile.getPos());
        }
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getVariant(state).isSlave() ? new TileKilnSlave() : new TileKilnMaster();
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this) {
            @Override
            public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
                final EnumFacing facing = player.getHorizontalFacing().getOpposite();
                for (Section section : Section.values()) {
                    if (section.canPlaceAt(world, pos, facing)) {
                        BlockPos masterPos = section.getMasterPosFromSlave(pos, facing);
                        placeKilnMaster(world, masterPos, facing);
                        placeKilnSlaves(world, masterPos, facing);
                        return true;
                    }
                }
                return false;
            }

            private void placeKilnMaster(World world, BlockPos masterPos, EnumFacing facing) {
                world.setBlockState(masterPos, getDefaultState());
                world.setTileEntity(masterPos, new TileKilnMaster(facing));
            }

            private void placeKilnSlaves(World world, BlockPos masterPos, EnumFacing facing) {
                for (Section section : Section.values()) {
                    if (!section.isSlave()) continue;
                    BlockPos slavePos = section.getSlavePosFromMaster(masterPos, facing);
                    world.setBlockState(slavePos, getDefaultState().withProperty(getProperty(), section));
                    world.setTileEntity(slavePos, new TileKilnSlave(masterPos));
                }
            }
        };

    }

    @Deprecated
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getVariant(state).getBoundingBox(getFacing(state, world, pos));
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(FACING);
    }

    public enum Section implements IPropertyProvider<Section> {

        FRONT_RIGHT_LOWER(BoundingBoxHelper.computeAABBsForFacing(0, 0, 0, 32, 32, 32)),
        FRONT_LEFT_LOWER(BoundingBoxHelper.computeAABBsForFacing(-16, 0, 0, 16, 32, 32), EAST),
        REAR_RIGHT_LOWER(BoundingBoxHelper.computeAABBsForFacing(0, 0, -16, 32, 32, 16), SOUTH),
        REAR_LEFT_LOWER(BoundingBoxHelper.computeAABBsForFacing(-16, 0, -16, 16, 32, 16), SOUTH, EAST),
        FRONT_RIGHT_UPPER(BoundingBoxHelper.computeAABBsForFacing(0, -16, 0, 32, 16, 32), UP),
        FRONT_LEFT_UPPER(BoundingBoxHelper.computeAABBsForFacing(-16, -16, 0, 16, 16, 32), UP, EAST),
        REAR_RIGHT_UPPER(BoundingBoxHelper.computeAABBsForFacing(0, -16, -16, 32, 16, 16), UP, SOUTH),
        REAR_LEFT_UPPER(BoundingBoxHelper.computeAABBsForFacing(-16, -16, -16, 16, 16, 16), UP, SOUTH, EAST);

        private final ImmutableMap<EnumFacing, AxisAlignedBB> structureRange;
        private final ImmutableList<EnumFacing> offsets;

        Section(ImmutableMap<EnumFacing, AxisAlignedBB> structureRange, EnumFacing... offsets) {
            this.structureRange = structureRange;
            this.offsets = ImmutableList.copyOf(offsets);
        }

        @Override
        public Section getProvider() {
            return this;
        }

        @Override
        public Material getMaterial() {
            return Material.ROCK;
        }

        @Override
        public float getHardness() {
            return 5.0F;
        }

        @Override
        public float getResistance() {
            return 30.0F;
        }

        @Override
        public int getLightOpacity() {
            return 0;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }

        public AxisAlignedBB getBoundingBox(EnumFacing facing) {
            return structureRange.get(facing); // FIXME Better bounding boxes once model is done
        }

        private boolean isSlave() {
            return !equals(FRONT_RIGHT_LOWER);
        }

        private boolean canPlaceAt(IBlockAccess world, BlockPos pos, EnumFacing facing) {
            AxisAlignedBB range = structureRange.get(facing);
            BlockPos min = pos.add(new Vec3i(range.minX, range.minY, range.minZ));
            BlockPos max = pos.add(new Vec3i(range.maxX, range.maxY, range.maxZ));
            for (MutableBlockPos target : MutableBlockPos.getAllInBoxMutable(min, max)) {
                Block block = world.getBlockState(target).getBlock();
                if (!block.isReplaceable(world, target)) return false;
            }
            return true;
        }

        private BlockPos getMasterPosFromSlave(BlockPos pos, EnumFacing facing) {
            for (EnumFacing offset : offsets) {
                offset = offset.getOpposite();
                offset = rotateOffset(offset, facing);
                pos = pos.offset(offset);
            }
            return pos;
        }

        private BlockPos getSlavePosFromMaster(BlockPos pos, EnumFacing facing) {
            for (EnumFacing offset : offsets) {
                offset = rotateOffset(offset, facing);
                pos = pos.offset(offset);
            }
            return pos;
        }

        private EnumFacing rotateOffset(EnumFacing offset, EnumFacing facing) {
            if (offset.getAxis().isHorizontal()) {
                switch (facing) {
                    case SOUTH:
                        offset = offset.getOpposite();
                        break;
                    case WEST:
                        offset = offset.rotateYCCW();
                        break;
                    case EAST:
                        offset = offset.rotateY();
                        break;
                }
            }
            return offset;
        }

    }

}
