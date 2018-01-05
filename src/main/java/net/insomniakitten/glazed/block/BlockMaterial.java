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

import net.insomniakitten.glazed.block.base.BlockEnumBase;
import net.insomniakitten.glazed.data.MaterialVariants;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class BlockMaterial extends BlockEnumBase<MaterialVariants> {

    public BlockMaterial() {
        super("material", MaterialVariants.class);
    }

    @Override
    public void onLanded(World world, Entity entity) {
        if (!entity.isSneaking()) {
            BlockPos posDown = new BlockPos(entity).down();
            getVariant(world.getBlockState(posDown)).onCollidedWithBlock(entity, world, posDown);
        } else {
            super.onLanded(world, entity);
        }
    }

}
