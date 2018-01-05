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

import net.insomniakitten.glazed.block.BlockCrucible;
import net.insomniakitten.glazed.block.BlockGlass;
import net.insomniakitten.glazed.block.BlockGlassPane;
import net.insomniakitten.glazed.block.BlockKiln;
import net.insomniakitten.glazed.block.BlockMaterial;
import net.insomniakitten.glazed.client.model.ICustomMeshDefinition;
import net.insomniakitten.glazed.client.model.ICustomStateMapper;
import net.insomniakitten.glazed.client.model.IModelProvider;
import net.insomniakitten.glazed.client.render.tesr.RenderCrucibleFluid;
import net.insomniakitten.glazed.client.render.tesr.RenderKilnContents;
import net.insomniakitten.glazed.compat.chisel.ChiselHelper;
import net.insomniakitten.glazed.compat.chisel.item.ItemGlassChisel;
import net.insomniakitten.glazed.item.IItemProvider;
import net.insomniakitten.glazed.tile.TileCrucible;
import net.insomniakitten.glazed.tile.TileKilnMaster;
import net.insomniakitten.glazed.tile.TileKilnSlave;
import net.insomniakitten.glazed.util.CollectionHelper;
import net.insomniakitten.glazed.util.RegistryFactory;
import net.insomniakitten.glazed.util.TileHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Glazed.ID)
public final class GlazedRegistry {

    public static final RegistryFactory<Block> BLOCK_FACTORY = new RegistryFactory<>();
    public static final RegistryFactory<Item> ITEM_FACTORY = new RegistryFactory<>();

    private GlazedRegistry() {}

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        TileHelper.registerTileEntity(TileCrucible.class);
        TileHelper.registerTileEntity(TileKilnMaster.class);
        TileHelper.registerTileEntity(TileKilnSlave.class);
        TileHelper.bindTESR(RenderCrucibleFluid::new, TileCrucible.class);
        TileHelper.bindTESR(RenderKilnContents::new, TileKilnMaster.class);
        BLOCK_FACTORY.begin(event)
                .register(BlockGlass::new)
                .register(BlockGlassPane::new)
                .register(BlockCrucible::new)
                .register(BlockKiln::new)
                .register(BlockMaterial::new)
                .end();
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        ITEM_FACTORY.begin(event)
                .registerAll(IItemProvider.collectItemBlocks(BLOCK_FACTORY.entries()))
                .registerIf(ItemGlassChisel::new, ChiselHelper::isGlassChiselEnabled)
                .end();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        CollectionHelper.forThoseOf(
                BLOCK_FACTORY.entries(),
                ICustomStateMapper.class,
                ICustomStateMapper::registerStateMapper
        );
        CollectionHelper.forThoseOf(
                ITEM_FACTORY.entries(),
                IModelProvider.class,
                IModelProvider::registerModels
        );
        CollectionHelper.forThoseOf(
                ITEM_FACTORY.entries(),
                ICustomMeshDefinition.class,
                ICustomMeshDefinition::registerMeshDefinition
        );
    }

}