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

import net.insomniakitten.glazed.common.ConfigManager;
import net.insomniakitten.glazed.common.ProxyManager;
import net.insomniakitten.glazed.common.block.BlockGlass;
import net.insomniakitten.glazed.common.block.BlockMaterial;
import net.insomniakitten.glazed.common.kiln.BlockKiln;
import net.insomniakitten.glazed.common.util.Logger;
import net.insomniakitten.glazed.shard.ItemGlassShard;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.function.Supplier;

@Mod(modid = Glazed.MOD_ID,
     name = Glazed.MOD_NAME,
     version = Glazed.VERSION,
     acceptedMinecraftVersions = Glazed.MC_VERSION,
     dependencies = Glazed.DEPENDENCIES)

public class Glazed {

    public static final String MOD_ID = "glazed";
    public static final String MOD_NAME = "Glazed";
    public static final String VERSION = "%VERSION%";
    public static final String MC_VERSION = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";
    public static final Block GLASS = new BlockGlass();
    public static final Block KILN = new BlockKiln();
    public static final Block MATERIAL = new BlockMaterial();
    /**
     * Only access when {@link ConfigManager.ShardsConfig#enableShards} is true
     */
    public static final Supplier<Item> GLASS_SHARD = ItemGlassShard::new;
    public static final CreativeTabs CTAB = new CreativeTabs(Glazed.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Glazed.KILN);
        }
    };
    @Mod.Instance(Glazed.MOD_ID)
    public static Glazed instance;
    @SidedProxy(clientSide = ProxyManager.CLIENT, serverSide = ProxyManager.SERVER)
    public static ProxyManager proxyManager;

    public static boolean isDeobf() {
        return Logger.DEOBF;
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxyManager.onPostInit(event);
    }

}
