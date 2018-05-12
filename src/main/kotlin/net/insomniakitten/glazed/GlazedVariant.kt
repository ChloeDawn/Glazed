package net.insomniakitten.glazed

import net.minecraft.block.SoundType
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.BlockRenderLayer.CUTOUT
import net.minecraft.util.BlockRenderLayer.TRANSLUCENT
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import java.util.Locale

object GlazedVariant : PropertyEnum<GlazedVariant.Variant>("variant", Variant::class.java, Variant.values().toSet()) {
    val VARIANTS = Variant.values()

    operator fun invoke(ordinal: Int) = this to this[ordinal]

    operator fun contains(ordinal: Int) = ordinal >= 0 && ordinal < VARIANTS.size

    operator fun contains(stack: ItemStack) = stack.metadata in this

    operator fun iterator() = VARIANTS.iterator()

    operator fun get(ordinal: Int) = if (ordinal in this) VARIANTS[ordinal] else {
        throw IndexOutOfBoundsException("No enum constant for ordinal $ordinal")
    }

    operator fun get(stack: ItemStack) = this[stack.metadata]

    enum class Variant(
            val renderLayer: BlockRenderLayer = CUTOUT,
            val hardness: Float = 0.3F,
            val resistance: Float = 1.5F,
            val lightLevel: Int = 0,
            val redstoneLevel: Int = 0,
            val soundType: SoundType = SoundType.GLASS,
            val color: (IBlockAccess, BlockPos) -> Int = { _, _ -> -1 }
    ) : IStringSerializable {
        GAIA(color = { access, pos ->
            access.getBiome(pos).getFoliageColorAtPos(pos)
        }),

        RADIANT(lightLevel = 15),

        IRIDESCENT(lightLevel = 8),

        ENERGETIC(renderLayer = TRANSLUCENT, redstoneLevel = 15),

        SHADOWED(TRANSLUCENT, 0.3f, 1.5f),

        VOIDIC(TRANSLUCENT, 0.3f, 1.5f),

        QUILTED(soundType = SoundType(
                1.0f, 1.0f,
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundEvents.BLOCK_CLOTH_STEP,
                SoundEvents.BLOCK_CLOTH_PLACE,
                SoundEvents.BLOCK_CLOTH_HIT,
                SoundEvents.BLOCK_CLOTH_FALL
        )),

        REINFORCED(soundType = SoundType.METAL),

        SLIMY(renderLayer = TRANSLUCENT, soundType = SoundType(
                1.0f, 1.0f,
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundEvents.BLOCK_SLIME_STEP,
                SoundEvents.BLOCK_GLASS_PLACE,
                SoundEvents.BLOCK_SLIME_HIT,
                SoundEvents.BLOCK_SLIME_FALL
        )),

        AURORIC(TRANSLUCENT, 0.3f, 1.5f, 10);

        val key get() = getName()

        override fun getName() = name.toLowerCase(Locale.ROOT)
    }
}