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

import net.insomniakitten.glazed.util.MaterialHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Locale;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public interface IPropertyProvider<E extends Enum<E> & IStringSerializable> extends IStringSerializable {

    E getProvider();

    Material getMaterial();

    default float getHardness() {
        return MaterialHelper.getHardness(getMaterial());
    }

    default float getResistance() {
        return MaterialHelper.getResistance(getMaterial());
    }

    default SoundType getSoundType() {
        return MaterialHelper.getSoundType(getMaterial());
    }

    default BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    default String getOrePrefix() {
        return "block";
    }

    default int getLightLevel() {
        return 0;
    }

    default int getLightOpacity() {
        return 255;
    }

    default boolean isToolEffective(String tool) {
        return false;
    }

    default AxisAlignedBB getBoundingBox() {
        return Block.FULL_BLOCK_AABB;
    }

    default int getRedstonePower() {
        return 0;
    }

    default String getName() {
        return this.getProvider().name().toLowerCase(Locale.ROOT);
    }

    default String getUnlocalizedName() {
        return LOWER_UNDERSCORE.to(LOWER_CAMEL, getName());
    }

    default int getMetadata() {
        return this.getProvider().ordinal();
    }

    default String getOreDict() {
        return this.getOrePrefix() + LOWER_UNDERSCORE.to(UPPER_CAMEL, getName());
    }

    default boolean requiresSilkTouch() {
        return false;
    }

    default boolean isFullCube() {
        return true;
    }

    default boolean hasTintIndex() {
        return false;
    }

    default BlockFaceShape getBlockFaceShape(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.SOLID;
    }

    default void onEntityWalk(World world, BlockPos pos, Entity entity) {

    }

    default void onCollidedWithBlock(Entity entity, World world, BlockPos pos) {

    }

    default boolean canCollideWithBlock(Entity entity, World world, BlockPos pos) {
        return entity != null;
    }

}
