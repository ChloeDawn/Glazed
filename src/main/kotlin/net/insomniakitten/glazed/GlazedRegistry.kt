package net.insomniakitten.glazed

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

import net.insomniakitten.glazed.block.GlassBlock
import net.insomniakitten.glazed.block.GlassKilnBlock
import net.insomniakitten.glazed.block.GlassPaneBlock
import net.insomniakitten.glazed.block.KilnBricksBlock
import net.insomniakitten.glazed.block.entity.GlassKilnEntity
import net.insomniakitten.glazed.item.VariantBlockItem
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder
import net.minecraftforge.registries.IForgeRegistryEntry

object GlazedRegistry {
    @ObjectHolder(Glazed.ID + ":kiln_bricks")
    @JvmField val KILN_BRICKS: Block = Blocks.AIR

    @ObjectHolder(Glazed.ID + ":glass_kiln")
    @JvmField val GLASS_KILN: Block = Blocks.AIR

    @ObjectHolder(Glazed.ID + ":glass_block")
    @JvmField val GLASS_BLOCK: Block = Blocks.AIR

    @ObjectHolder(Glazed.ID + ":glass_pane")
    @JvmField val GLASS_PANE: Block = Blocks.AIR

    @ObjectHolder(Glazed.ID + ":kiln_bricks")
    @JvmField val KILN_BRICKS_ITEM: Item = Items.AIR

    @ObjectHolder(Glazed.ID + ":glass_kiln")
    @JvmField val GLASS_KILN_ITEM: Item = Items.AIR

    @ObjectHolder(Glazed.ID + ":glass_block")
    @JvmField val GLASS_BLOCK_ITEM: Item = Items.AIR

    @ObjectHolder(Glazed.ID + ":glass_pane")
    @JvmField val GLASS_PANE_ITEM: Item = Items.AIR

    @SubscribeEvent
    fun onBlockRegistry(event: Register<Block>) {
        register(event, KilnBricksBlock(), "kiln_bricks")
        register(event, GlassKilnBlock(), "glass_kiln")
        register(event, GlassBlock(), "glass_block")
        register(event, GlassPaneBlock(), "glass_pane")
        register(GlassKilnEntity::class.java, "glass_kiln")
    }

    @SubscribeEvent
    fun onItemRegistry(event: Register<Item>) {
        register(event, ItemBlock(KILN_BRICKS), "kiln_bricks")
        register(event, ItemBlock(GLASS_KILN), "glass_kiln")
        register(event, VariantBlockItem(GLASS_BLOCK), "glass_block")
        register(event, VariantBlockItem(GLASS_PANE), "glass_pane")
    }

    private fun <V : IForgeRegistryEntry<V>> register(event: Register<V>, entry: V, name: String) {
        val id = ResourceLocation(Glazed.ID, name)
        Glazed.LOGGER.debug("Registering entry {} to registry {}", id, event.name)
        event.registry.register(entry.setRegistryName(id))
    }

    private fun register(blockEntity: Class<out TileEntity>, name: String) {
        val id = ResourceLocation(Glazed.ID, name)
        val cls = blockEntity.simpleName
        Glazed.LOGGER.debug("Registering block entity class {} with key {}", cls, id)
        GameRegistry.registerTileEntity(blockEntity, id.toString())
    }
}
