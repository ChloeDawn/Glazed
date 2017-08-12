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

import com.google.common.base.Equivalence;
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

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "ConstantConditions"})
public class RegistryManager {

    @Mod.EventBusSubscriber
    public static class ShardRegistry {

        public static final Map<Equivalence.Wrapper<ItemStack>, Equivalence.Wrapper<ItemStack>> SHARDS = new HashMap<>();

        public static final Equivalence<ItemStack> EQV = new Equivalence<ItemStack>() {
            @Override protected boolean doEquivalent(@Nonnull ItemStack a, @Nonnull ItemStack b) {
                return ItemStack.areItemStackShareTagsEqual(a, b);
            }
            @Override protected int doHash(@Nonnull ItemStack stack) {
                int result = stack.getItem().getRegistryName().hashCode();
                result = 31 * result + stack.getItemDamage();
                //result = 31 * result + stack.getCount();
                result = 31 * result + (stack.hasTagCompound() ? stack.getTagCompound().hashCode() : 0);
                return result;
            }
        };

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
                            shard.getTagCompound().setInteger("metadata", glass.getMetadata());
                        }

                        if (!SHARDS.containsKey(EQV.wrap(shard)) && glass.getMetadata() <= 15) {
                            Glazed.LOGGER.info("Registering {} <{}#{}> from {}",
                                    glass.getDisplayName(), name, glass.getMetadata(), ore);
                            SHARDS.put(EQV.wrap(shard), EQV.wrap(glass));
                        }
                    }
                }
            }
        }

    }

    @Mod.EventBusSubscriber
    private static class ObjectRegistry {

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onBlockRegistry(Register<Block> event) {
            event.getRegistry().register(Objects.BGLASS);
            event.getRegistry().register(Objects.BKILN);
            event.getRegistry().register(Objects.BMATERIAL);
            GameRegistry.registerTileEntity(TileKiln.class, "kiln");
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onItemRegistry(Register<Item> event) {
            event.getRegistry().register(Objects.IGLASS);
            event.getRegistry().register(Objects.IKILN);
            event.getRegistry().register(Objects.IMATERIAL);
        }

    }

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
                ModelResourceLocation glassMRL = new ModelResourceLocation(glass, "type=" + type);
                ModelLoader.setCustomModelResourceLocation(Objects.IGLASS, i, glassMRL);
            }

            ModelResourceLocation kilnMRL = new ModelResourceLocation(kiln, "inventory");
            ModelLoader.setCustomModelResourceLocation(Objects.IKILN, 0, kilnMRL);

            for (int i = 0; i < MaterialBlockType.values().length; ++i) {
                String type = MaterialBlockType.values()[i].getName();
                ModelResourceLocation materialMRL = new ModelResourceLocation(material, "type=" + type);
                ModelLoader.setCustomModelResourceLocation(Objects.IMATERIAL, i, materialMRL);
            }

            ModelResourceLocation shardMRL = new ModelResourceLocation(shard, "inventory");
            ModelLoader.setCustomModelResourceLocation(Objects.ISHARD, 0, shardMRL);

        }

    }

}
