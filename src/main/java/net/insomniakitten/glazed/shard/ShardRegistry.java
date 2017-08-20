package net.insomniakitten.glazed.shard;

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
import net.insomniakitten.glazed.Glazed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class ShardRegistry {

    public static final Map<Equivalence.Wrapper<ItemStack>, Equivalence.Wrapper<ItemStack>> SHARDS = new HashMap<>();

    private static final Equivalence<ItemStack> EQV = new Equivalence<ItemStack>() {
        @Override
        protected boolean doEquivalent(ItemStack a, ItemStack b) {
            return ItemStack.areItemStackShareTagsEqual(a, b);
        }
        @Override @SuppressWarnings("ConstantConditions")
        protected int doHash(ItemStack stack) {
            int result = stack.getItem().getRegistryName().hashCode();
            result = 31 * result + stack.getItemDamage();
            result = 31 * result + (stack.hasTagCompound() ?
                    stack.getTagCompound().hashCode() : 0);
            return result;
        }
    };

    public static ItemStack getGlassFromShard(ItemStack shard) {
        Equivalence.Wrapper<ItemStack> eqv = ShardRegistry.EQV.wrap(shard);
        if (ShardRegistry.SHARDS.containsKey(eqv)) {
            return ShardRegistry.SHARDS.get(eqv).get();
        } else return ItemStack.EMPTY;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onShardRegistry(RegistryEvent.Register<Item> event) {
        for (String ore : OreDictionary.getOreNames()) {
            if (ore.startsWith("blockGlass") && !ore.equals("blockGlassColorless")) {
                for (ItemStack glass : OreDictionary.getOres(ore)) {
                    ItemStack shard = new ItemStack(Glazed.ModItems.SHARD.get());
                    String name = glass.getItem().getRegistryName().toString();

                    shard.setTagInfo("name", new NBTTagString(name));
                    shard.setTagInfo("metadata", new NBTTagInt(glass.getMetadata()));

                    if (!SHARDS.containsKey(EQV.wrap(shard)) && glass.getMetadata() <= 15) {
                        Glazed.LOGGER.info("Registering glass shard for {} <{}#{}>",
                                glass.getDisplayName(), name, glass.getMetadata());
                        SHARDS.put(EQV.wrap(shard), EQV.wrap(glass));
                    }
                }
            }
        }
    }

}
