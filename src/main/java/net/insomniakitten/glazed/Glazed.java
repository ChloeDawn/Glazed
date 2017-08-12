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

import net.insomniakitten.glazed.client.GUIManager;
import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.glass.ItemBlockGlass;
import net.insomniakitten.glazed.glass.ItemGlassShard;
import net.insomniakitten.glazed.kiln.BlockKiln;
import net.insomniakitten.glazed.kiln.ItemBlockKiln;
import net.insomniakitten.glazed.kiln.RecipesKiln;
import net.insomniakitten.glazed.material.BlockMaterial;
import net.insomniakitten.glazed.material.ItemBlockMaterial;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

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
    public static final String VERSION = "%mod_version%";
    public static final String MC_VERSION = "%mc_version%";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";
    public static final String CLIENT_PROXY = "net.insomniakitten.glazed.client.ClientWrapper";
    public static final String SERVER_PROXY = "net.insomniakitten.glazed.Glazed$ProxyWrapper";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final CreativeTabs TAB = new CreativeTabs(Glazed.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Objects.BKILN);
        }
    };

    public static class Objects {
        public static final BlockGlass BGLASS = new BlockGlass();
        public static final ItemBlockGlass IGLASS = new ItemBlockGlass(BGLASS);
        public static final BlockKiln BKILN = new BlockKiln();
        public static final ItemBlockKiln IKILN = new ItemBlockKiln(BKILN);
        public static final BlockMaterial BMATERIAL = new BlockMaterial();
        public static final ItemBlockMaterial IMATERIAL = new ItemBlockMaterial(BMATERIAL);
        public static final ItemGlassShard ISHARD = new ItemGlassShard();
    }

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static ProxyWrapper proxy;

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(
                Glazed.MOD_ID, new GUIManager());
        proxy.registerColorHandler();
        proxy.parseSpecials();

        RecipesKiln.addKilnRecipe(
                new ItemStack(Blocks.SAND, 16),
                new ItemStack(Blocks.REDSTONE_BLOCK),
                new ItemStack(Objects.BGLASS, 16, 3));

        RecipesKiln.addKilnRecipe(
                new ItemStack(Blocks.SAND),
                ItemStack.EMPTY, // TODO: Figure out why this isn't working
                new ItemStack(Blocks.GLASS));
        // Also don't actually add this recipe, it makes everything else a nightmare to process
        // Probably should make catalyst required (non-empty)

        RecipesKiln.addKilnRecipe(
                new ItemStack(Blocks.SAND),
                new ItemStack(Blocks.SAND),
                new ItemStack(Blocks.GLASS, 2));
    }

    public static class ProxyWrapper {
        protected static final UUID EMPTY_UUID = new UUID(0, 0);
        public void registerColorHandler() {}
        public void parseSpecials() {}
        public UUID getUUID() { return EMPTY_UUID; }
    }

}
