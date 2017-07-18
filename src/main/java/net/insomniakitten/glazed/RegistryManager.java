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

import net.insomniakitten.glazed.Glazed.Objects;
import net.insomniakitten.glazed.glass.GlassBlockType;
import net.insomniakitten.glazed.kiln.TileKiln;
import net.insomniakitten.glazed.material.MaterialBlockType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RegistryManager {

    @Mod.EventBusSubscriber
    public static class ShardRegistry {

        public static final Map<ItemStack, ItemStack> SHARDS = new HashMap<ItemStack, ItemStack>();

        @SuppressWarnings("ConstantConditions")
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onShardRegistry(Register<Item> event) {

            event.getRegistry().register(Objects.ISHARD);

            for (String ore : OreDictionary.getOreNames()) {
                if (ore.startsWith("blockGlass") && !ore.equals("blockGlassColorless")) {
                    for (ItemStack glass : OreDictionary.getOres(ore)) {
                        ItemStack shard = new ItemStack(Objects.ISHARD);
                        String name = glass.getItem().getRegistryName().toString();

                        shard.setTagCompound(new NBTTagCompound());
                        shard.getTagCompound().setString("name", name);

                        if (glass.getHasSubtypes()) {
                            shard.getTagCompound().setInteger(
                                    "metadata", glass.getMetadata());
                        }

                        if (!SHARDS.containsKey(shard) && glass.getMetadata() <= 15) {
                            Glazed.LOGGER.info("Registering {} <{}#{}> from {}",
                                    glass.getDisplayName(), name,
                                    glass.getMetadata(), ore);
                            SHARDS.put(shard, glass);
                        }
                    }
                }
            }
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Mod.EventBusSubscriber
    private static class ObjectRegistry {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onBlockRegistry(Register<Block> event) {
            event.getRegistry().register(Objects.BGLASS);
            event.getRegistry().register(Objects.BKILN);
            String name = Objects.BKILN.getRegistryName().toString();
            GameRegistry.registerTileEntity(TileKiln.class, name);
            event.getRegistry().register(Objects.BMATERIAL);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onItemRegistry(Register<Item> event) {
            event.getRegistry().register(Objects.IGLASS);
            event.getRegistry().register(Objects.IKILN);
            event.getRegistry().register(Objects.IMATERIAL);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Mod.EventBusSubscriber(Side.CLIENT)
    public static class ModelRegistry {

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelRegistry(ModelRegistryEvent event) {
            Glazed.LOGGER.info("ModelRegistryEvent");
            ResourceLocation glass = Objects.BGLASS.getRegistryName();
            ResourceLocation kiln = Objects.BKILN.getRegistryName();
            ResourceLocation material = Objects.BMATERIAL.getRegistryName();
            ResourceLocation shard = Objects.ISHARD.getRegistryName();

            OBJLoader.INSTANCE.addDomain(Glazed.MOD_ID);

            for (int i = 0; i < GlassBlockType.values().length; ++i) {
                String type = GlassBlockType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Objects.IGLASS, i,
                        new ModelResourceLocation(glass, "type=" + type));
            }

            ModelLoader.setCustomModelResourceLocation(Objects.IKILN, 0,
                    new ModelResourceLocation(kiln, "inventory"));

            for (int i = 0; i < MaterialBlockType.values().length; ++i) {
                String type = MaterialBlockType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Objects.IMATERIAL, i,
                        new ModelResourceLocation(material, "type=" + type));
            }

            ModelLoader.setCustomModelResourceLocation(Objects.ISHARD, 0,
                    new ModelResourceLocation(shard, "inventory"));

        }

    }

}
