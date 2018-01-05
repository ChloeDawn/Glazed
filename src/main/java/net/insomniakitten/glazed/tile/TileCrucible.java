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

public final class TileCrucible extends TileEntityBase {

    private int fluidLevel = 0;
    private int progress = 0;

    public TileCrucible() {}

    @Override
    public void writeTagCompound(NBTTagCompound nbt) {
        nbt.setInteger("fluid_level", fluidLevel);
        nbt.setInteger("progress", progress);
    }

    @Override
    public void readTagCompound(NBTTagCompound nbt) {
        fluidLevel = nbt.getInteger("fluid_level");
        progress = nbt.getInteger("progress");
    }

    public int getFluidLevel() {
        return fluidLevel;
    }

    public int getProgress() {
        return progress;
    }

}
