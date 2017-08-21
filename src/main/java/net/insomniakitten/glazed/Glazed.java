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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(   modid = Glazed.MOD_ID,
        name = Glazed.MOD_NAME,
        version = Glazed.VERSION,
        acceptedMinecraftVersions = Glazed.MC_VERSION,
        dependencies = Glazed.DEPENDENCIES)

public class Glazed {

    @Mod.Instance(Glazed.MOD_ID)
    public static Glazed instance;

    public static final String MOD_ID = "glazed";
    public static final String MOD_NAME = "Glazed";
    public static final String VERSION = "%VERSION%";
    public static final String MC_VERSION = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final CreativeTabs CTAB = new CreativeTabs(Glazed.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModBlocks.KILN.get());
        }
    };

    @SidedProxy(clientSide = ColorManager.CLIENT, serverSide = ColorManager.SERVER)
    public static ColorManager colorManager;

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        colorManager.registerColorHandler();
        GUIManagerKiln.register();
    }

    public enum ModBlocks {
        GLASS(new BlockGlass()),
        KILN(new BlockKiln()),
        MATERIAL(new BlockMaterial()),
        ;

        private final Block block;

        ModBlocks(Block block) {
            this.block = block;
        }

        public Block get() {
            return block;
        }
    }

    public enum ModItems {
        SHARD(new ItemGlassShard()),
        ;

        private final Item item;

        ModItems(Item item) {
            this.item = item;
        }

        public Item get() {
            return item;
        }
    }

}
