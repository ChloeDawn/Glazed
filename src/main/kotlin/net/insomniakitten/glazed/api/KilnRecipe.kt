package net.insomniakitten.glazed.api

import net.minecraft.item.ItemStack
import net.minecraftforge.registries.IForgeRegistryEntry

class KilnRecipe(
        input: ItemStack,
        output: ItemStack,
        vararg catalysts: ItemStack
) : IForgeRegistryEntry.Impl<KilnRecipe>() {
    val input = input.copy()
    val output = output.copy()
    val catalysts = catalysts.map(ItemStack::copy).toSet()
}
