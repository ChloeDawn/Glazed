@file:JvmName("Variants")

package net.insomniakitten.glazed.extensions

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
