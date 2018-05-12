@file:JvmName("Capabilities")

package net.insomniakitten.glazed.extensions

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
