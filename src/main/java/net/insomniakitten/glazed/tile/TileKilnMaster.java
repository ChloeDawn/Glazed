package net.insomniakitten.glazed.tile;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public final class TileKilnMaster extends TileEntityBase {

    /**
     * EnumFacing$Plane#HORIZONTAL#values is not respective of EnumFacing#getHorizontalIndex
     * So this is a custom collection to query that has the correct indexing
     */
    private static final ImmutableList<EnumFacing> FACINGS = ImmutableList.of(
            EnumFacing.SOUTH,
            EnumFacing.WEST,
            EnumFacing.NORTH,
            EnumFacing.EAST
    );

    private EnumFacing facing = EnumFacing.NORTH;
    private int progress = 0;

    public TileKilnMaster() {}

    public TileKilnMaster(EnumFacing facing) {
        this.facing = facing;
        markDirty();
    }

    @Override
    public void writeTagCompound(NBTTagCompound nbt) {
        nbt.setInteger("facing", facing.getHorizontalIndex());
        nbt.setInteger("progress", progress);
    }

    @Override
    public void readTagCompound(NBTTagCompound nbt) {
        facing = FACINGS.get(nbt.getInteger("facing"));
        progress = nbt.getInteger("progress");
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public int getProgress() {
        return progress;
    }

}
