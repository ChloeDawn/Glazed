@file:JvmName("Capabilities")

package net.insomniakitten.glazed.extensions

/*
 *  Copyright 2018 InsomniaKitten
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

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

operator fun TileEntity.contains(capability: Capability<*>) = hasCapability(capability, null)

infix fun <T> TileEntity.has(capability: Capability<T>) = hasCapability(capability, null)

infix fun <T> TileEntity.has(pair: Pair<Capability<T>, EnumFacing?>) = hasCapability(pair.first, pair.second)

infix fun <T> Capability<T>.from(te: TileEntity) = if (te has this) te.getCapability(this, null) else null

infix fun <T> Capability<T>.from(pair: Pair<TileEntity, EnumFacing?>) =
        if (pair.first has (this on pair.second)) {
            pair.first.getCapability(this, pair.second)
        } else null
