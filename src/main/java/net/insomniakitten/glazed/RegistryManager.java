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

import net.insomniakitten.glazed.glass.GlassType;
import net.insomniakitten.glazed.kiln.TileKiln;
import net.insomniakitten.glazed.material.MaterialType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class RegistryManager {

    @SuppressWarnings("ConstantConditions")
    @Mod.EventBusSubscriber
    private static class ObjectRegistry {

        @SubscribeEvent
        public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(Glazed.Objects.BGLASS);
            event.getRegistry().register(Glazed.Objects.BKILN);
            String name = Glazed.Objects.BKILN.getRegistryName().toString();
            GameRegistry.registerTileEntity(TileKiln.class, name);
            event.getRegistry().register(Glazed.Objects.BMATERIAL);
        }

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(Glazed.Objects.IGLASS);
            event.getRegistry().register(Glazed.Objects.IKILN);
            event.getRegistry().register(Glazed.Objects.IMATERIAL);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Mod.EventBusSubscriber(Side.CLIENT)
    public static class ModelRegistry {

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelRegistry(ModelRegistryEvent event) {
            Glazed.LOGGER.info("ModelRegistryEvent");
            ResourceLocation glass = Glazed.Objects.BGLASS.getRegistryName();
            ResourceLocation kiln = Glazed.Objects.BKILN.getRegistryName();
            ResourceLocation material = Glazed.Objects.BMATERIAL.getRegistryName();

            OBJLoader.INSTANCE.addDomain(Glazed.MOD_ID);

            for (int i = 0; i < GlassType.values().length; ++i) {
                String type = GlassType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Glazed.Objects.IGLASS, i,
                        new ModelResourceLocation(glass, "type=" + type));
            }

            ModelLoader.setCustomModelResourceLocation(Glazed.Objects.IKILN, 0,
                    new ModelResourceLocation(kiln, "inventory"));

            for (int i = 0; i < MaterialType.values().length; ++i) {
                String type = MaterialType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Glazed.Objects.IMATERIAL, i,
                        new ModelResourceLocation(material, "type=" + type));
            }
        }

    }

}
