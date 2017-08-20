package net.insomniakitten.glazed;

/*
 *  Copyright 2017 InsomniaKitten
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

import net.insomniakitten.glazed.Glazed.ModBlocks;
import net.insomniakitten.glazed.Glazed.ModItems;
import net.insomniakitten.glazed.kiln.TileKiln;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "ConstantConditions"})
@Mod.EventBusSubscriber
public class RegistryManager {

    private static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();

    public static void registerItemBlock(ItemBlock iblock) {
        if (!ITEM_BLOCKS.contains(iblock)) {
            ITEM_BLOCKS.add(iblock);
        }
    }

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        GameRegistry.registerTileEntity(TileKiln.class, TileKiln.getKey());
        for (ModBlocks block : ModBlocks.values()) {
            event.getRegistry().register(block.get());
        }
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        if (!ITEM_BLOCKS.isEmpty()) {
            for (ItemBlock iblock : ITEM_BLOCKS) {
                event.getRegistry().register(iblock);
            }
        }
        for (ModItems item : ModItems.values()) {
            event.getRegistry().register(item.get());
        }
    }

}
