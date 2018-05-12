@file:JvmName("BlockStates")

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

import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

typealias BlockState = IBlockState

operator fun BlockState.invoke(access: IBlockAccess, pos: BlockPos): BlockState = getActualState(access, pos)

operator fun <V : Comparable<V>, K : IProperty<V>> K.invoke(value: V): Pair<K, V> = this to value

operator fun <V : Comparable<V>> IProperty<V>.contains(value: V) = value in allowedValues

operator fun <V : Comparable<V>> BlockState.contains(property: IProperty<V>) = property in propertyKeys

operator fun <V : Comparable<V>> Block.contains(property: IProperty<V>) = property in blockState.properties

operator fun <V : Comparable<V>> BlockState.get(property: IProperty<V>): V = getValue(property)

operator fun <V : Comparable<V>> BlockState.plus(entry: Pair<IProperty<V>, V>): BlockState =
        withProperty(entry.first, entry.second)

infix fun <V : Comparable<V>> IProperty<V>.from(state: BlockState): V = state.getValue(this)

infix fun <V : Comparable<V>> BlockState.with(entry: Pair<IProperty<V>, V>): BlockState =
        withProperty(entry.first, entry.second)

infix fun <V : Comparable<V>> BlockState.cycle(property: IProperty<V>): BlockState =
        cycleProperty(property)
