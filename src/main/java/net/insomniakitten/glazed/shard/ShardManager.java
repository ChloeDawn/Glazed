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
import com.google.common.base.Equivalence.Wrapper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.common.ConfigManager;
import net.insomniakitten.glazed.common.util.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ShardManager {

    public static class Data {

        private static final BiMap<Wrapper<ItemStack>, Wrapper<ItemStack>> SHARDS = HashBiMap.create();
        private static final Map<Wrapper<ItemStack>, String> ORE_CACHE = new HashMap<>();

        private static final Equivalence<ItemStack> SHARD_EQV = new Equivalence<ItemStack>() {

            @Override
            protected boolean doEquivalent(ItemStack a, ItemStack b) {
                return ItemStack.areItemStackShareTagsEqual(a, b);
            }

            @Override
            @SuppressWarnings("ConstantConditions")
            protected int doHash(ItemStack stack) {
                int result = stack.getItem().getRegistryName().hashCode();
                result = 31 * result + stack.getItemDamage();
                result = 31 * result + (stack.hasTagCompound() ? stack.getTagCompound().hashCode() : 0);
                return result;
            }

        };

        public static Map<Wrapper<ItemStack>, Wrapper<ItemStack>> getShards() {
            return ImmutableMap.copyOf(SHARDS);
        }

        public static ItemStack getGlassFromShard(ItemStack shard) {
            Wrapper<ItemStack> wrapped = SHARD_EQV.wrap(shard);
            if (SHARDS.containsKey(wrapped)) {
                return SHARDS.get(wrapped).get();
            } else return ItemStack.EMPTY;
        }

    }

    @Mod.EventBusSubscriber(modid = Glazed.MOD_ID)
    @SuppressWarnings("deprecation")
    public static class RegistryHandler {

        private static final Stopwatch STOPWATCH = Stopwatch.createUnstarted();

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onShardRegistry(Register<Item> event) {
            if (ConfigManager.shardsConfig.enableShards) {
                populateShardMaps();
                event.getRegistry().register(Glazed.GLASS_SHARD.get());
                Logger.info(true, "Registered {} glass shards", Data.SHARDS.size());
                registerOreEntries();
            }
        }

        private static void populateShardMaps() {
            STOPWATCH.start();
            Arrays.stream(OreDictionary.getOreNames()).filter(ore -> ore.startsWith("blockGlass")).forEach(ore -> {
                NonNullList<ItemStack> list = OreDictionary.getOres(ore);
                if (!ConfigManager.shardsConfig.anarchyMode) {
                    cacheGlassBlock(list.get(0));
                } else {
                    list.forEach(RegistryHandler::cacheGlassBlock);
                }
            });
            STOPWATCH.stop();
            String msg = "Cached {} item stacks as glass shard variants in {}ms";
            Logger.info(false, msg, Data.SHARDS.size(), getElapsedTime());
            STOPWATCH.reset();
        }

        private static void cacheGlassBlock(ItemStack glass) {
            ItemStack stack = new ItemStack(Glazed.GLASS_SHARD.get());
            stack.setTagInfo("name", new NBTTagString(glass.getItem().getRegistryName().toString()));
            stack.setTagInfo("metadata", new NBTTagInt(glass.getMetadata()));
            Wrapper<ItemStack> shard = Data.SHARD_EQV.wrap(stack);
            if (glass.getMetadata() != OreDictionary.WILDCARD_VALUE) {
                //Data.SHARDS.putIfAbsent(shard, Data.SHARD_EQV.wrap(glass));
                //Data.ORE_CACHE.putIfAbsent(shard, getShardOreName(glass));
                Data.SHARDS.computeIfAbsent(shard, key -> Data.SHARD_EQV.wrap(glass));
                Data.ORE_CACHE.computeIfAbsent(shard, key -> getShardOreName(glass));
                IBlockState state = Block.getBlockFromItem(glass.getItem()).getStateFromMeta(glass.getMetadata());
                DropEventHandler.STATIC_STATE_CACHE.putIfAbsent(state, shard);
            }
        }

        private static void registerOreEntries() {
            STOPWATCH.start();
            Data.ORE_CACHE.forEach((stack, ore) -> OreDictionary.registerOre(ore, stack.get()));
            STOPWATCH.stop();
            String msg = "Registered and populated {} unique ore entries for {} glass shards in {}ms";
            long ores = Data.ORE_CACHE.values().stream().distinct().count();
            Logger.info(false, msg, ores, Data.ORE_CACHE.size(), getElapsedTime());
            STOPWATCH.reset();
        }

        private static String getShardOreName(ItemStack stack) {
            return "shard" + stack.getDisplayName().replaceAll(" ", "");
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onRecipeRegistry(Register<IRecipe> event) {
            STOPWATCH.start();
            Data.SHARDS.forEach((shard, glass) -> addShardRecipe(event, shard.get(), glass.get()));
            STOPWATCH.stop();
            String msg = "Registered {} glass shard -> glass block recipes in {}ms";
            Logger.info(false, msg, Data.SHARDS.size(), getElapsedTime());
            STOPWATCH.reset();
        }

        private static void addShardRecipe(Register<IRecipe> event, ItemStack shard, ItemStack glass) {
            ResourceLocation group = new ResourceLocation(Glazed.MOD_ID, "shard");
            String shardName = "shard_" + format(glass.getUnlocalizedName());
            ResourceLocation name = new ResourceLocation(Glazed.MOD_ID, shardName);
            if (event.getRegistry().containsKey(name))
                name = new ResourceLocation(Glazed.MOD_ID, shardName + "_" + glass.getMetadata());
            GameRegistry.addShapedRecipe(name, group, glass, "SS", "SS", 'S', new IngredientGlassShard(shard));
        }

        private static long getElapsedTime() {
            return STOPWATCH.elapsed(TimeUnit.MILLISECONDS);
        }

        private static String format(String string) {
            return string.replaceFirst("tile\\.", "").replaceAll("[^a-zA-Z0-9_]", "_");
        }

    }

    @Mod.EventBusSubscriber
    public static class DropEventHandler {

        private static final Map<IBlockState, Wrapper<ItemStack>> STATIC_STATE_CACHE = new HashMap<>();
        private static final Map<IBlockState, Wrapper<ItemStack>> DYNAMIC_STATE_CACHE = new HashMap<>();

        @SubscribeEvent
        public static void onHarvestDrops(HarvestDropsEvent event) throws IOException {
            if (!ConfigManager.shardsConfig.enableShards || !event.getDrops().isEmpty() || event.isSilkTouching()) {
                return;
            }

            ItemStack dropStack = ItemStack.EMPTY;

            if (ConfigManager.shardsConfig.anarchyMode || STATIC_STATE_CACHE.containsKey(event.getState())) {
                dropStack = STATIC_STATE_CACHE.get(event.getState()).get().copy();
            } else {
                if (DYNAMIC_STATE_CACHE.containsKey(event.getState())) {
                    dropStack = DYNAMIC_STATE_CACHE.get(event.getState()).get().copy();
                } else {
                    ItemStack pickBlock = event.getState().getBlock()
                            .getPickBlock(event.getState(), null, event.getWorld(), event.getPos(),
                                    event.getHarvester());
                    if (!pickBlock.isEmpty()) {
                        int[] stackOreIds = OreDictionary.getOreIDs(pickBlock);
                        for (int oreId : stackOreIds) {
                            String oreName = OreDictionary.getOreName(oreId);
                            Wrapper<ItemStack> glass = Data.SHARD_EQV.wrap(OreDictionary.getOres(oreName).get(0));
                            if (Data.SHARDS.containsValue(glass) && isValidEntry(stackOreIds, oreName)) {
                                Wrapper<ItemStack> shard = Data.SHARDS.inverse().get(glass);
                                DYNAMIC_STATE_CACHE.put(event.getState(), shard);
                                dropStack = shard.get().copy();
                                break;
                            }
                        }
                    }
                }
            }

            if (!dropStack.isEmpty()) {
                dropStack.setCount(getDropChance(event.getWorld().rand, event.getFortuneLevel()));
                event.getDrops().add(dropStack);
            }

        }

        private static boolean isValidEntry(int[] oreIds, String oreName) {
            boolean hasSingleEntry = oreIds.length == 1;
            boolean isBlockGlass = oreName.equals("blockGlass");
            boolean isGlassInstance = oreName.startsWith("blockGlass");
            return isGlassInstance && (hasSingleEntry || !isBlockGlass);
        }

        private static int getDropChance(Random rand, int fortune) {
            return MathHelper.clamp(2 + rand.nextInt(3) + rand.nextInt(fortune + 1), 1, 4);
        }

    }

    private static final class IngredientGlassShard extends Ingredient {

        private final ItemStack shard;

        IngredientGlassShard(ItemStack shard) {
            this.shard = shard;
        }

        @Override
        public ItemStack[] getMatchingStacks() {
            return new ItemStack[] { shard };
        }

        @Override
        public boolean apply(ItemStack stack) {
            return ItemStack.areItemStackTagsEqual(stack, shard);
        }

    }

}
