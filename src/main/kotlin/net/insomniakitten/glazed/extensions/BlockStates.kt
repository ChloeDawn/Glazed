@file:JvmName("BlockStates")

package net.insomniakitten.glazed.extensions

import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

operator fun IBlockState.invoke(access: IBlockAccess, pos: BlockPos): IBlockState = getActualState(access, pos)

operator fun <V : Comparable<V>, K : IProperty<V>> K.invoke(value: V): Pair<K, V> = this to value

operator fun <V : Comparable<V>> IProperty<V>.contains(value: V) = value in allowedValues

operator fun <V : Comparable<V>> IBlockState.contains(property: IProperty<V>) = property in propertyKeys

operator fun <V : Comparable<V>> Block.contains(property: IProperty<V>) = property in blockState.properties

operator fun <V : Comparable<V>> IBlockState.get(property: IProperty<V>): V = getValue(property)

operator fun <V : Comparable<V>> IBlockState.plus(entry: Pair<IProperty<V>, V>): IBlockState =
        withProperty(entry.first, entry.second)

infix fun <V : Comparable<V>> IProperty<V>.from(state: IBlockState): V = state.getValue(this)

infix fun <V : Comparable<V>> IBlockState.with(entry: Pair<IProperty<V>, V>): IBlockState =
        withProperty(entry.first, entry.second)

infix fun <V : Comparable<V>> IBlockState.cycle(property: IProperty<V>): IBlockState =
        cycleProperty(property)
