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

import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper

inline val IItemHandler.comparatorOutput get() = ItemHandlerHelper.calcRedstoneFromInventory(this)

operator fun <T> ICapabilityProvider.get(capability: Capability<T>, side: EnumFacing? = null) =
        getCapability(capability, side)

operator fun <T> ICapabilityProvider.contains(capability: Capability<T>) =
        hasCapability(capability, null)

operator fun <T> ICapabilityProvider.contains(pair: Pair<Capability<T>, EnumFacing?>) =
        hasCapability(pair.first, pair.second)

infix fun <T> ICapabilityProvider.has(capability: Capability<T>) =
        hasCapability(capability, null)

infix fun <T> ICapabilityProvider.has(pair: Pair<Capability<T>, EnumFacing?>) =
        hasCapability(pair.first, pair.second)

infix fun <T> Capability<T>.from(provider: ICapabilityProvider) =
        provider.getCapability(this, null)

infix fun <T> Capability<T>.from(pair: Pair<ICapabilityProvider, EnumFacing?>) =
        pair.first.getCapability(this, pair.second)

fun IItemHandler.insertItem(stack: ItemStack, simulate: Boolean) =
        ItemHandlerHelper.insertItem(this, stack, simulate)

fun IItemHandler.insertItemStacked(stack: ItemStack, simulate: Boolean) =
        ItemHandlerHelper.insertItemStacked(this, stack, simulate)
