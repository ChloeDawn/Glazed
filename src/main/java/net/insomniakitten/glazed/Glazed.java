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

import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.glass.GlassType;
import net.insomniakitten.glazed.glass.ItemBlockGlass;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(   modid = Glazed.MOD_ID,
        name = Glazed.MOD_NAME,
        version = Glazed.VERSION,
        dependencies = Glazed.DEPS)

public class Glazed {

    private static final boolean DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    public static final String MOD_ID = "glazed";
    public static final String MOD_NAME = "Glazed";
    public static final String VERSION = "%mod_version%";

    public static final String MC_VERSION = "%mc_version%";
    public static final String DEPS = "required-after:forge@[14.21.1.2387,)";

    private static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final TabGlazed TAB = new TabGlazed();

    public static final BlockGlass BGLASS = new BlockGlass();
    public static final ItemBlockGlass IGLASS = new ItemBlockGlass(BGLASS);

    private static class TabGlazed extends CreativeTabs {
        TabGlazed() { super(Glazed.MOD_ID); }
        @Override @Nonnull
        public ItemStack getTabIconItem() { return new ItemStack(IGLASS, 1, 7); } // Auroric Glass
    }

    @Mod.EventBusSubscriber
    private static class RegistryManager {

        @SubscribeEvent
        public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(BGLASS);
        }

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new ItemBlockGlass(BGLASS));
        }

    }

    @SideOnly(Side.CLIENT)
    @Mod.EventBusSubscriber(Side.CLIENT)
    private static class ModelManager {

        @SubscribeEvent
        public static void onModelRegistry(ModelRegistryEvent event) {
            Glazed.LOGGER.info("ModelRegistryEvent");
            ResourceLocation rl = Glazed.BGLASS.getRegistryName();
            for (int i = 0; i < GlassType.values().length; ++i) {
                String type = GlassType.values()[i].getName();
                Glazed.LOGGER.info("<{}>, type={}", rl, type);
                ModelResourceLocation mrl = new ModelResourceLocation(rl, "type=" + type);
                ModelLoader.setCustomModelResourceLocation(IGLASS, i, mrl);
            }
        }

    }


}
