package net.insomniakitten.glazed.block

import net.insomniakitten.glazed.Glazed
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material

class KilnBricksBlock : Block(Material.ROCK, MapColor.ADOBE) {
    init {
        soundType = SoundType.STONE
        setHardness(2.0f)
        setResistance(30.0f)
    }

    override fun getUnlocalizedName() = "block.${Glazed.ID}.kiln_bricks"

    override fun getCreativeTabToDisplayOn() = Glazed.TAB
}
