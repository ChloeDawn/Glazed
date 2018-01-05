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
import com.google.common.collect.Sets;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.client.ClientHelper;
import net.insomniakitten.glazed.client.SpecialsParser;
import net.insomniakitten.glazed.client.color.IBlockColorProvider;
import net.insomniakitten.glazed.client.color.IItemColorProvider;
import net.insomniakitten.glazed.client.model.ICustomStateMapper;
import net.insomniakitten.glazed.client.model.WrappedModel;
import net.insomniakitten.glazed.data.GlassVariants;
import net.insomniakitten.glazed.item.ItemBlockEnumBase;
import net.insomniakitten.glazed.util.ColorHelper;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public final class BlockGlassPane extends BlockEnumBase<GlassVariants> implements ICustomStateMapper, IBlockColorProvider {

    public static final ImmutableMap<EnumFacing, PropertyBool> CONNECTIONS = ImmutableMap.of(
            EnumFacing.EAST, PropertyBool.create("connect_east"),
            EnumFacing.NORTH, PropertyBool.create("connect_north"),
            EnumFacing.SOUTH, PropertyBool.create("connect_south"),
            EnumFacing.WEST, PropertyBool.create("connect_west")
    );
    private static final ImmutableList<AxisAlignedBB> AABB_PANE = ImmutableList.of(
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    );

    public BlockGlassPane() {
        super("glass_pane", GlassVariants.class);
    }

    private int getBoundingBoxIndex(IBlockState state) {
        int i = 0;
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            if (state.getValue(CONNECTIONS.get(side))) {
                i |= 1 << side.getHorizontalIndex();
            }
        }
        return i;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            IBlockState stateAt = world.getBlockState(pos.offset(side));
            boolean canAttachTo = canAttachTo(world, stateAt, pos.offset(side), side.getOpposite());
            state = state.withProperty(CONNECTIONS.get(side), canAttachTo);
        }
        return state;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState stateAt = world.getBlockState(pos.offset(side));
        boolean canAttachTo = canAttachTo(world, stateAt, pos.offset(side), side.getOpposite());
        return (!canAttachTo || side.getAxis().isVertical()) && super.shouldSideBeRendered(state, world, pos, side);
    }

    private boolean canAttachTo(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        BlockFaceShape shape = state.getBlockFaceShape(world, pos, facing);
        return shape == BlockFaceShape.SOLID || shape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockGlassPane(this);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_PANE.get(getBoundingBoxIndex(getActualState(state, source, pos)));
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN
               ? BlockFaceShape.MIDDLE_POLE_THIN
               : BlockFaceShape.CENTER_SMALL;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
        if (!isActualState) state = getActualState(state, world, pos);
        if (getVariant(state).canCollideWithBlock(entity, world, pos)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_PANE.get(0));
            for (EnumFacing side : EnumFacing.HORIZONTALS) {
                if (state.getValue(CONNECTIONS.get(side))) {
                    AxisAlignedBB aabb = AABB_PANE.get(1 << side.getHorizontalIndex());
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
                }
            }
        }
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        BlockStateContainer.Builder builder = super.createStateContainer();
        CONNECTIONS.values().forEach(builder::add);
        return builder;
    }

    @Override
    public StateMapperBase getStateMapper() {
        return new StateMapperBase() {
            private final ResourceLocation pathDefault = new ResourceLocation(Glazed.ID, "glass_pane");
            private final ResourceLocation pathTinted = new ResourceLocation(Glazed.ID, "glass_pane_tinted");

            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                TreeSet<String> connections = Sets.newTreeSet();
                for (EnumFacing side : EnumFacing.HORIZONTALS) {
                    String value = String.valueOf(state.getValue(CONNECTIONS.get(side)));
                    connections.add(("connect_" + side.getName()) + "=" + value);
                }
                String variant = String.join(",", connections) + ",variant=" + getVariant(state).getName();
                return new ModelResourceLocation(getVariant(state).hasTintIndex() ? pathTinted : pathDefault, variant);
            }

        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return getVariant(state) == GlassVariants.GAIA ? ColorHelper.getBiomeColor(world, pos) : 0;
    }

    private final class ItemBlockGlassPane extends ItemBlockEnumBase<GlassVariants> implements IItemColorProvider {

        public ItemBlockGlassPane(BlockEnumBase<GlassVariants> block) {
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
                   ? SpecialsParser.DATA.get(match) + " pane"
                   : super.getItemStackDisplayName(stack);
        }

        @Override
        public void addModels(Set<WrappedModel> models) {
            ResourceLocation name = new ResourceLocation(Glazed.ID, "glass_pane_item");
            GlassVariants.stream().forEach(variant ->
                    models.add(new WrappedModel.Builder(this, variant.getMetadata())
                            .setResourceLocation(name)
                            .addVariant("variant=" + variant.getName())
                            .build()
                    ));
        }

    }

}
