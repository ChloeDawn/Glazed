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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class TileKilnSlave extends TileEntityBase {

    private BlockPos.MutableBlockPos masterPos = new BlockPos.MutableBlockPos();

    public TileKilnSlave() {}

    public TileKilnSlave(BlockPos masterPos) {
        this.masterPos.setPos(masterPos);
        markDirty();
    }

    @Override
    public void writeTagCompound(NBTTagCompound nbt) {
        nbt.setInteger("master_x", masterPos.getX());
        nbt.setInteger("master_y", masterPos.getY());
        nbt.setInteger("master_z", masterPos.getZ());
    }

    @Override
    public void readTagCompound(NBTTagCompound nbt) {
        int x = nbt.getInteger("master_x");
        int y = nbt.getInteger("master_y");
        int z = nbt.getInteger("master_z");
        masterPos.setPos(x, y, z);
    }

    public BlockPos getMasterPos() {
        return masterPos.toImmutable();
    }

}
