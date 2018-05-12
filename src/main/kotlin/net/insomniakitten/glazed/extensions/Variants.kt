@file:JvmName("Variants")

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

import net.insomniakitten.glazed.GlazedVariant
import net.insomniakitten.glazed.GlazedVariant.Variant
import net.minecraft.block.state.IBlockState
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

inline val IBlockState.variant: Variant get() = this[GlazedVariant]

inline val ItemStack.variant: Variant?
    get() = if (this in GlazedVariant) {
        GlazedVariant[this]
    } else null

inline val IBlockState.key get() = variant.key

inline val ItemStack.key: String? get() = variant?.key

inline val ItemStack.description: String
    @SideOnly(Side.CLIENT)
    get() = I18n.format("tooltip.glazed.variant.${key ?: "unknown"}")
