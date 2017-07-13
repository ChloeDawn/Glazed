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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.glass.GlassType;
import net.insomniakitten.glazed.glass.ItemBlockGlass;
import net.insomniakitten.glazed.kiln.BlockKiln;
import net.insomniakitten.glazed.kiln.ItemBlockKiln;
import net.insomniakitten.glazed.kiln.TileKiln;
import net.insomniakitten.glazed.material.BlockMaterial;
import net.insomniakitten.glazed.material.ItemBlockMaterial;
import net.insomniakitten.glazed.material.MaterialType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(modid = Glazed.MOD_ID,
        name = Glazed.MOD_NAME,
        version = Glazed.VERSION,
        acceptedMinecraftVersions = Glazed.MC_VERSION,
        dependencies = Glazed.DEPS)

public class Glazed {

    @Mod.Instance(Glazed.MOD_ID)
    public static Glazed instance;

    public static final String MOD_ID = "glazed";
    public static final String MOD_NAME = "Glazed";
    public static final String VERSION = "%mod_version%";
    public static final String MC_VERSION = "%mc_version%";
    public static final String DEPS = "required-after:forge@[14.21.1.2387,)";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final TabGlazed TAB = new TabGlazed();

    public static class Objects {

        public static final HashMap<Pair<UUID, String>, String> SPECIALS = new HashMap<>();

        public static final BlockGlass BGLASS = new BlockGlass();
        public static final ItemBlockGlass IGLASS = new ItemBlockGlass(BGLASS);

        public static final BlockKiln BKILN = new BlockKiln();
        public static final ItemBlockKiln IKILN = new ItemBlockKiln(BKILN);

        public static final BlockMaterial BMATERIAL = new BlockMaterial();
        public static final ItemBlockMaterial IMATERIAL = new ItemBlockMaterial(BMATERIAL);


    }

    @SidedProxy(
            clientSide = "net.insomniakitten.glazed.Glazed$ClientWrapper",
            serverSide = "net.insomniakitten.glazed.Glazed$ProxyWrapper")
    public static ProxyWrapper proxy;

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.parseSpecials();
        proxy.registerColorHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(Glazed.MOD_ID, new GuiHandler());
    }

    public static class ProxyWrapper {

        protected static final UUID EMPTY_UUID = new UUID(0, 0);
        public void registerColorHandler() {}
        public void parseSpecials() {}
        public UUID getUUID() { return EMPTY_UUID; }

    }

    private static class TabGlazed extends CreativeTabs {

        TabGlazed() { super(Glazed.MOD_ID); }
        @Override @Nonnull
        public ItemStack getTabIconItem() {
            return new ItemStack(Objects.IGLASS, 1, 3);
        } // Energetic Glass

    }

    @Mod.EventBusSubscriber
    private static class RegistryManager {


        @SubscribeEvent @SuppressWarnings("ConstantConditions")
        public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(Objects.BGLASS);
            event.getRegistry().register(Objects.BKILN);
            GameRegistry.registerTileEntity(TileKiln.class, Objects.BKILN.getRegistryName().toString());
            event.getRegistry().register(Objects.BMATERIAL);
        }

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(Objects.IGLASS);
            event.getRegistry().register(Objects.IKILN);
            event.getRegistry().register(Objects.IMATERIAL);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Mod.EventBusSubscriber(Side.CLIENT)
    private static class ModelManager {

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelRegistry(ModelRegistryEvent event) {
            Glazed.LOGGER.info("ModelRegistryEvent");
            ResourceLocation glass = Glazed.Objects.BGLASS.getRegistryName();
            ResourceLocation kiln = Objects.BKILN.getRegistryName();
            ResourceLocation material = Glazed.Objects.BMATERIAL.getRegistryName();

            OBJLoader.INSTANCE.addDomain(Glazed.MOD_ID);

            for (int i = 0; i < GlassType.values().length; ++i) {
                String type = GlassType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Objects.IGLASS, i,
                        new ModelResourceLocation(glass, "type=" + type));
            }

            ModelLoader.setCustomModelResourceLocation(Objects.IKILN, 0,
                    new ModelResourceLocation(kiln, "inventory"));

            for (int i = 0; i < MaterialType.values().length; ++i) {
                String type = MaterialType.values()[i].getName();
                ModelLoader.setCustomModelResourceLocation(Objects.IMATERIAL, i,
                        new ModelResourceLocation(material, "type=" + type));
            }
        }

    }

    @Mod.EventBusSubscriber
    public static class ClientWrapper extends Glazed.ProxyWrapper {

        @Override
        @SideOnly(Side.CLIENT)
        public void registerColorHandler() {
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
                @Override @SideOnly(Side.CLIENT)
                public int colorMultiplier(
                        IBlockState state, @Nullable IBlockAccess world,
                        @Nullable BlockPos pos, int tintIndex) {
                    if (!GlassType.getType(state).equals(GlassType.GAIA)) return -1;
                    return world != null && pos != null ?
                            BiomeColorHelper.getGrassColorAtPos(world, pos)
                            : ColorizerGrass.getGrassColor(0.5D, 1.0D);
                }
            }, Objects.BGLASS);

            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
                @Override @SideOnly(Side.CLIENT)
                public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                    EntityPlayer plr = Minecraft.getMinecraft().player;
                    if (plr == null) return -1;
                    if (!GlassType.getType(stack.getMetadata()).equals(GlassType.GAIA)) return -1;
                    BlockPos pos = new BlockPos(plr.posX, plr.posY, plr.posZ);
                    return plr.world != null ?
                            BiomeColorHelper.getGrassColorAtPos(plr.world, pos)
                            : ColorizerGrass.getGrassColor(0.5D, 1.0D);
                }
            }, Objects.IGLASS); // sand and glass itemblocks
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void parseSpecials() {
            IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
            ResourceLocation rl = new ResourceLocation(Glazed.MOD_ID, "data/specials.json");
            Gson gson = new Gson(); JsonElement ele;

            try {
                IResource resource = manager.getResource(rl);
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                ele = gson.fromJson(reader, JsonElement.class);
                reader.close();
            } catch (Exception ignored) {
                Glazed.LOGGER.warn("Failed to parse specials.json!");
                return;
            }

            JsonObject json = ele.getAsJsonObject();
            for (Map.Entry<String, JsonElement> group : json.entrySet()) {
                JsonObject obj = group.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                    Objects.SPECIALS.put(Pair.of(
                            UUID.fromString(e.getKey()), group.getKey()),
                            e.getValue().getAsString());
                }
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public UUID getUUID() {
            EntityPlayer plr = Minecraft.getMinecraft().player;
            return plr != null ? plr.getGameProfile().getId() : EMPTY_UUID;
        }

    }

}
