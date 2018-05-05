package net.insomniakitten.glazed;

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

import net.insomniakitten.glazed.block.GlassBlock;
import net.insomniakitten.glazed.block.GlassKilnBlock;
import net.insomniakitten.glazed.block.GlassPaneBlock;
import net.insomniakitten.glazed.block.KilnBricksBlock;
import net.insomniakitten.glazed.block.entity.GlassKilnEntity;
import net.insomniakitten.glazed.item.BlockItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;

import static net.insomniakitten.glazed.GlazedVariant.NAME_MAPPER;
import static net.insomniakitten.glazed.GlazedVariant.STACK_MAPPER;
import static net.insomniakitten.glazed.GlazedVariant.STATE_MAPPER;

enum GlazedRegistry {
    INSTANCE;

    @ObjectHolder(Glazed.ID + ":kiln_bricks")
    protected static final Block KILN_BRICKS = Blocks.AIR;

    @ObjectHolder(Glazed.ID + ":glass_kiln")
    protected static final Block GLASS_KILN = Blocks.AIR;

    @ObjectHolder(Glazed.ID + ":glass_block")
    protected static final Block GLASS_BLOCK = Blocks.AIR;

    @ObjectHolder(Glazed.ID + ":glass_pane")
    protected static final Block GLASS_PANE = Blocks.AIR;

    @ObjectHolder(Glazed.ID + ":kiln_bricks")
    protected static final Item KILN_BRICKS_ITEM = Items.AIR;

    @ObjectHolder(Glazed.ID + ":glass_kiln")
    protected static final Item GLASS_KILN_ITEM = Items.AIR;

    @ObjectHolder(Glazed.ID + ":glass_block")
    protected static final Item GLASS_BLOCK_ITEM = Items.AIR;

    @ObjectHolder(Glazed.ID + ":glass_pane")
    protected static final Item GLASS_PANE_ITEM = Items.AIR;

    @SubscribeEvent
    protected void onBlockRegistry(Register<Block> event) {
        register(event, new KilnBricksBlock(), "kiln_bricks");
        register(event, new GlassKilnBlock(), "glass_kiln");
        register(event, new GlassBlock(), "glass_block");
        register(event, new GlassPaneBlock(), "glass_pane");
        register(GlassKilnEntity.class, "glass_kiln");
    }

    @SubscribeEvent
    protected void onItemRegistry(Register<Item> event) {
        register(event, new BlockItem(KILN_BRICKS), "kiln_bricks");
        register(event, new BlockItem(GLASS_KILN), "glass_kiln");
        register(event, new BlockItem(GLASS_BLOCK, NAME_MAPPER), "glass_block");
        register(event, new BlockItem(GLASS_PANE, NAME_MAPPER), "glass_pane");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected void onModelRegistry(ModelRegistryEvent event) {
        register(KILN_BRICKS_ITEM, "normal");
        register(GLASS_KILN_ITEM, "inventory");
        register(STATE_MAPPER, GLASS_BLOCK, GLASS_PANE);
        register(STACK_MAPPER, GLASS_BLOCK_ITEM, GLASS_PANE_ITEM);
    }

    private <V extends IForgeRegistryEntry<V>> void register(Register<V> event, V entry, String name) {
        final ResourceLocation id = new ResourceLocation(Glazed.ID, name);
        Glazed.LOGGER.debug("Registering entry {} to registry {}", id, event.getName());
        event.getRegistry().register(entry.setRegistryName(id));
    }

    private void register(Class<? extends TileEntity> blockEntity, String name) {
        final ResourceLocation id = new ResourceLocation(Glazed.ID, name);
        Glazed.LOGGER.debug("Registering block entity class {} with key {}", blockEntity, id);
        GameRegistry.registerTileEntity(blockEntity, id.toString());
    }

    private void register(IStateMapper stateMapper, Block... blocks) {
        for (final Block block : blocks) {
            final ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
            Glazed.LOGGER.debug("Registering block state mapper for {}", id);
            ModelLoader.setCustomStateMapper(block, stateMapper);
        }
    }

    private void register(ItemMeshDefinition meshDefinition, Item... items) {
        for (final Item item : items) {
            final ResourceLocation id = Objects.requireNonNull(item.getRegistryName());
            Glazed.LOGGER.debug("Registering item model definition for {}", id);
            ModelLoader.setCustomMeshDefinition(item, meshDefinition);
            ModelBakery.registerItemVariants(item, id);
        }
    }

    private void register(Item item, String variant) {
        final ResourceLocation id = Objects.requireNonNull(item.getRegistryName());
        register((ItemStack stack) -> new ModelResourceLocation(id, variant), item);
    }
}
