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

import net.insomniakitten.glazed.client.ColorManager;
import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.kiln.BlockKiln;
import net.insomniakitten.glazed.kiln.GUIManagerKiln;
import net.insomniakitten.glazed.material.BlockMaterial;
import net.insomniakitten.glazed.shard.ItemGlassShard;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;

@Mod(   modid = Glazed.MOD_ID,
        name = Glazed.MOD_NAME,
        version = Glazed.VERSION,
        acceptedMinecraftVersions = Glazed.MC_VERSION,
        dependencies = Glazed.DEPENDENCIES)

public class Glazed {

    @Mod.Instance(Glazed.MOD_ID)
    public static Glazed instance;

    // Information
    public static final String MOD_ID = "glazed";
    public static final String MOD_NAME = "Glazed";
    public static final String VERSION = "%VERSION%";
    public static final String MC_VERSION = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";

    // Creative Tab
    public static final CreativeTabs CTAB = new CreativeTabs(Glazed.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Glazed.KILN);
        }
    };

    // Content
    public static final Block GLASS = new BlockGlass();
    public static final Block KILN = new BlockKiln();
    public static final Block MATERIAL = new BlockMaterial();
    public static final Item SHARD = new ItemGlassShard();

    @SidedProxy(clientSide = ColorManager.CLIENT, serverSide = ColorManager.SERVER)
    public static ColorManager colorManager;

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        colorManager.registerColorHandler();
        GUIManagerKiln.register();
    }

    public static class Logger {

        private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(MOD_NAME);
        private static final boolean DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        public static void info(boolean global, String msg, Object... params) {
            if (global || DEOBF) LOGGER.info(msg, params);
        }

        public static void warn(boolean global, String msg, Object... params) {
            if (global || DEOBF) LOGGER.warn(msg, params);
        }

    }

}
