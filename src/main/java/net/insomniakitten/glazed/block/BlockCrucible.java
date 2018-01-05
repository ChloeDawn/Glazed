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
import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.block.base.IPropertyProvider;
import net.insomniakitten.glazed.client.render.selection.ISelectionBoxRenderer;
import net.insomniakitten.glazed.client.render.selection.RenderSelectionBoxes;
import net.insomniakitten.glazed.tile.TileCrucible;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraft.util.EnumFacing.WEST;

public final class BlockCrucible extends BlockEnumBase<BlockCrucible.Section> implements ISelectionBoxRenderer {

    public BlockCrucible() {
        super("crucible", "section", Section.class, false);
    }

    /**
     * Adds selection box for the inside space of the crucible basin to the render event
     * @return The state-relative center of the structure, and the bounding box to render
     * @see RenderSelectionBoxes#onDrawBlockHighlight(DrawBlockHighlightEvent)
     */
    @Override
    public Function<Pair<BlockPos, IBlockState>, Map<BlockPos, AxisAlignedBB>> getSelectionBoxes() {
        return pair -> {
            Map<BlockPos, AxisAlignedBB> selectionBoxes = new HashMap<>();
            BlockPos posCenter = getVariant(pair.getValue()).getCenterBlock(pair.getKey());
            AxisAlignedBB aabbCenter = new AxisAlignedBB(0.004, 0.1915, 0.004, 0.996, 0.996, 0.996);
            // Only render when looking at lower half of structure
            if (pair.getKey().getY() == posCenter.getY()) selectionBoxes.put(posCenter, aabbCenter);
            return selectionBoxes;
        };
    }

    /**
     * Places frame blocks around the existing center block
     * @param center The BlockPos relative to the center of the structure
     */
    private void placeStructureFrame(World world, BlockPos center) {
        for (Section section : Section.values()) {
            if (section.isCenter()) continue;
            IBlockState state = getDefaultState().withProperty(getProperty(), section);
            world.setBlockState(section.getOffsetFromCenter(center), state);
        }
    }

    /**
     * Removes all surrounding frame blocks from around the center block
     * @param center The BlockPos relative to the center of the structure
     */
    private void breakStructureFrame(World world, BlockPos center) {
        for (Section section : Section.values()) {
            BlockPos pos = section.getOffsetFromCenter(center);
            if (!section.isCenter()) world.setBlockToAir(pos);
        }
    }

    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getVariant(state).getCollisionBoundingBox();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BlockPos center = getVariant(state).getCenterBlock(pos);
        breakStructureFrame(world, center);
        // TODO: Handle tile entity data pre-removal
        world.removeTileEntity(center);
        world.setBlockToAir(center);
    }

    @Override
    @Deprecated
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        AxisAlignedBB aabbCollision = getVariant(state).getCollisionBoundingBox();
        AxisAlignedBB aabbSelection = getVariant(state).getBoundingBox();
        return rayTrace(pos, start, end, aabbCollision != null ? aabbCollision : aabbSelection);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos center) {
        for (Section section : Section.values()) {
            BlockPos pos = section.getOffsetFromCenter(center);
            Block block = world.getBlockState(pos).getBlock();
            if (!block.isReplaceable(world, pos)) return false;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        placeStructureFrame(world, pos);
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getVariant(state).isCenter();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getVariant(state).isCenter() ? new TileCrucible() : null;
    }

    public enum Section implements IPropertyProvider<Section> {

        CENTER_LOWER(new AxisAlignedBB(-0.5D, 0.0D, -0.5D, 1.5D, 1.0D, 1.5D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D)),
        NORTH_LOWER(new AxisAlignedBB(-0.5D, 0.0D, 0.5D, 1.5D, 1.0D, 2.5D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D), NORTH),
        SOUTH_LOWER(new AxisAlignedBB(-0.5D, 0.0D, -1.5D, 1.5D, 1.0D, 0.5D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D), SOUTH),
        WEST_LOWER(new AxisAlignedBB(0.5D, 0.0D, -0.5D, 2.5D, 1.0D, 1.5D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), WEST),
        EAST_LOWER(new AxisAlignedBB(-1.5D, 0.0D, -0.5D, 0.5D, 1.0D, 1.5D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D), EAST),
        NORTH_WEST_LOWER(new AxisAlignedBB(0.5D, 0.0D, 0.5D, 2.5D, 1.0D, 2.5D), new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D), NORTH, WEST),
        NORTH_EAST_LOWER(new AxisAlignedBB(-1.5D, 0.0D, 0.5D, 0.5D, 1.0D, 2.5D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D), NORTH, EAST),
        SOUTH_WEST_LOWER(new AxisAlignedBB(0.5D, 0.0D, 0.5D, 2.5D, 1.0D, -1.5D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D), SOUTH, WEST),
        SOUTH_EAST_LOWER(new AxisAlignedBB(-1.5D, 0.0D, -1.5D, 0.5D, 1.0D, 0.5D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D), SOUTH, EAST),
        CENTER_UPPER(new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 1.0, 0.875), NULL_AABB, UP),
        NORTH_WEST_UPPER(new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D), NORTH, WEST, UP),
        NORTH_EAST_UPPER(new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D), NORTH, EAST, UP),
        SOUTH_WEST_UPPER(new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D), SOUTH, WEST, UP),
        SOUTH_EAST_UPPER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D), SOUTH, EAST, UP);

        private final AxisAlignedBB aabbSelection;
        private final AxisAlignedBB aabbCollision;
        private final ImmutableList<EnumFacing> offsets;

        Section(AxisAlignedBB aabbSelection, AxisAlignedBB aabbCollision, EnumFacing... offsets) {
            this.aabbSelection = aabbSelection;
            this.aabbCollision = aabbCollision;
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
        public AxisAlignedBB getBoundingBox() {
            return aabbSelection;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }

        @Override
        public BlockFaceShape getBlockFaceShape(IBlockAccess world, BlockPos pos, EnumFacing side) {
            return isCenter() ? BlockFaceShape.BOWL : BlockFaceShape.UNDEFINED;
        }

        public AxisAlignedBB getCollisionBoundingBox() {
            return aabbCollision;
        }

        /**
         * A simple verbose method to determine if the current Section is the center
         * @return Whether the queried Section is equal to {@link Section#CENTER_LOWER}
         */
        public boolean isCenter() {
            return equals(CENTER_LOWER);
        }

        /**
         * Query the Section's relative offset from the center of the structure
         * The BlockPos offset is determined by the Section's defined EnumFacing offsets
         * @param center The BlockPos relative to the center of the structure
         * @return The Section's offset BlockPos
         */
        public BlockPos getOffsetFromCenter(BlockPos center) {
            if (!offsets.isEmpty()) {
                for (EnumFacing offset : offsets) {
                    center = center.offset(offset);
                }
            }
            return center;
        }

        /**
         * Query the center of the structure relative to the current Section
         * @param pos The BlockPos of the frame block being queried
         * @return The BlockPos for the center of the structure
         */
        public BlockPos getCenterBlock(BlockPos pos) {
            if (!offsets.isEmpty()) {
                for (EnumFacing offset : offsets) {
                    pos = pos.offset(offset.getOpposite());
                }
            }
            return pos;
        }

    }

}
