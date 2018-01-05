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

import net.insomniakitten.glazed.proxy.GlazedProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Glazed.ID,
     name = Glazed.NAME,
     version = Glazed.VERSION,
     acceptedMinecraftVersions = Glazed.MC_VERSIONS,
     dependencies = Glazed.DEPENDENCIES)
public final class Glazed {

    public static final String ID = "glazed";
    public static final String NAME = "Glazed";
    public static final String VERSION = "%VERSION%";
    public static final String MC_VERSIONS = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,);after:chisel;after:tconstruct;";

    public static final Logger LOGGER = LogManager.getLogger(Glazed.NAME);
    @GameRegistry.ObjectHolder("glazed:glass")
    public static final Block GLASS = Blocks.AIR;
    @GameRegistry.ObjectHolder("glazed:glass_pane")
    public static final Block GLASS_PANE = Blocks.AIR;
    @GameRegistry.ObjectHolder("glazed:kiln")
    public static final Block KILN = Blocks.AIR;
    public static final CreativeTabs CTAB = new CreativeTabs(Glazed.ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Glazed.KILN);
        }
    };
    @GameRegistry.ObjectHolder("glazed:material")
    public static final Block MATERIAL = Blocks.AIR;

    @GameRegistry.ObjectHolder("glazed:chisel")
    public static final Item CHISEL = Items.AIR;

    @Mod.Instance(Glazed.ID)
    public static Glazed instance;

    @SidedProxy(clientSide = GlazedProxy.CLIENT, serverSide = GlazedProxy.SERVER)
    public static GlazedProxy proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInit(event);
    }

}
