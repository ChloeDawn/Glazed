package net.insomniakitten.glazed.common.block;

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

import net.insomniakitten.glazed.common.type.MaterialType;
import net.insomniakitten.glazed.common.util.IOverlayProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockMaterial extends BlockBase<MaterialType> implements IOverlayProvider {

    private final BlockPos.MutableBlockPos entityPos = new BlockPos.MutableBlockPos();

    public BlockMaterial() {
        super("material", MaterialType.class);
    }

    @Override
    public ResourceLocation getOverlayTexture(IBlockState state) {
        return getType(state).getOverlayTexture(state);
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(
            IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
            Entity entity, boolean flag) {
        if (getType(state).canCollideWithBlock(entity, world, pos)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(world, pos));
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!entity.isSneaking()) {
            getType(state).onCollidedWithBlock(entity, world, pos);
        }
    }

    @Override
    public void onLanded(World world, Entity entity) {
        if (!entity.isSneaking()) {
            entityPos.setPos(entity.posX, entity.posY, entity.posZ);
            getType(world.getBlockState(entityPos.toImmutable().down()))
                    .onCollidedWithBlock(entity, world, entityPos.toImmutable().down());
        } else {
            super.onLanded(world, entity);
        }
    }

}
