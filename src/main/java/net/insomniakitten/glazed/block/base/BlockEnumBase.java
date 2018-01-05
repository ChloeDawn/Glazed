package net.insomniakitten.glazed.block.base;

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

import com.google.common.base.CaseFormat;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.item.IItemProvider;
import net.insomniakitten.glazed.item.ItemBlockBase;
import net.insomniakitten.glazed.item.ItemBlockEnumBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockEnumBase<E extends Enum<E> & IPropertyProvider<E>> extends Block implements IItemProvider {

    private final E[] enumConstants;
    private final PropertyEnum<E> propertyEnum;
    private final BlockStateContainer stateContainer;
    private final boolean hasItemSubtypes;

    public BlockEnumBase(String name, String prefix, Class<E> clazz, boolean itemSubtypes) {
        super(Material.AIR);
        enumConstants = clazz.getEnumConstants();
        propertyEnum = PropertyEnum.create(prefix, clazz);
        stateContainer = createStateContainer().build();
        hasItemSubtypes = itemSubtypes;
        String loc = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
        setRegistryName(name);
        setUnlocalizedName(Glazed.ID + "." + loc);
        setCreativeTab(Glazed.CTAB);
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockEnumBase(String name, String prefix, Class<E> clazz) {
        this(name, prefix, clazz, true);
    }

    public BlockEnumBase(String name, Class<E> clazz, boolean itemSubtypes) {
        this(name, "variant", clazz, itemSubtypes);
    }

    public BlockEnumBase(String name, Class<E> clazz) {
        this(name, "variant", clazz);
    }

    @Override
    public ItemBlock getItemBlock() {
        return hasItemSubtypes ? new ItemBlockEnumBase<>(this) : new ItemBlockBase(this);
    }

    public final E getVariant(IBlockState state) {
        return state.getValue(propertyEnum);
    }

    public final E getVariant(int meta) {
        return enumConstants[MathHelper.clamp(meta, 0, enumConstants.length)];
    }

    public final E[] getValues() {
        return enumConstants;
    }

    public final PropertyEnum<E> getProperty() {
        return propertyEnum;
    }

    @Override
    @Deprecated
    public Material getMaterial(IBlockState state) {
        return getVariant(state).getMaterial();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(propertyEnum, getVariant(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getVariant(state).getMetadata();
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return getVariant(state).isFullCube();
    }

    @Override
    @Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getVariant(state).getHardness();
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getVariant(state).getBoundingBox();
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return getVariant(state).getBlockFaceShape(world, pos, side);
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean flag) {
        if (getVariant(state).canCollideWithBlock(entity, world, pos)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(world, pos));
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return propertyEnum == null || getVariant(state).isFullCube();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return hasItemSubtypes ? getVariant(state).getMetadata() : 0;
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        getVariant(world.getBlockState(pos)).onEntityWalk(world, pos, entity);
    }

    @Override
    @Deprecated
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getVariant(state).getRedstonePower();
    }

    @Override
    @Deprecated
    public boolean canProvidePower(IBlockState state) {
        return getVariant(state).getRedstonePower() > 0;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        getVariant(state).onCollidedWithBlock(entity, world, pos);
    }

    @Override
    @Deprecated
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getVariant(state).getRedstonePower();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (hasItemSubtypes) {
            for (E variant : enumConstants) {
                items.add(new ItemStack(this, 1, variant.getMetadata()));
            }
        } else items.add(new ItemStack(this));
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).build();
    }

    @Override
    public final BlockStateContainer getBlockState() {
        return stateContainer;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getVariant(state).getLightLevel();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!getVariant(state).requiresSilkTouch()) super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return getVariant(state).requiresSilkTouch();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity entity, Explosion explosion) {
        return getVariant(world.getBlockState(pos)).getResistance();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return hasItemSubtypes ? new ItemStack(this, 1, getVariant(state).getMetadata()) : new ItemStack(this);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getVariant(state).getLightOpacity();
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return getVariant(state).isToolEffective(type);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer.equals(getVariant(state).getRenderLayer());
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return getVariant(state).getSoundType();
    }

    protected BlockStateContainer.Builder createStateContainer() {
        return new BlockStateContainer.Builder(this).add(propertyEnum);
    }

}