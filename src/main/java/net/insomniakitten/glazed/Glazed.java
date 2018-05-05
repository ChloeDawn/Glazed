package net.insomniakitten.glazed;

/*
 *  Copyright 2018 InsomniaKitten
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

import net.insomniakitten.glazed.block.entity.GlassKilnEntity;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod(modid = Glazed.ID, name = Glazed.NAME, version = Glazed.VERSION)
public final class Glazed {
    public static final String ID = "glazed";
    public static final String NAME = "Glazed";
    public static final String VERSION = "%VERSION%";

    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final CreativeTabs TAB = new CreativeTabs(Glazed.ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel() {
            return "item_group." + ID + ".label";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(GlazedRegistry.GLASS_KILN_ITEM);
        }
    };

    private static final IGuiHandler GUI_HANDLER = new IGuiHandler() {
        @Override
        @Nullable
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            final BlockPos pos;
            final TileEntity tile;
            if (id == 0 && world.isValid(pos = new BlockPos(x, y, z))) {
                if ((tile = world.getTileEntity(pos)) instanceof GlassKilnEntity) {
                    throw new UnsupportedOperationException("Kiln container not yet implemented");
                }
            }
            return null;
        }

        @Override
        @Nullable
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            final BlockPos pos;
            final TileEntity tile;
            if (id == 0 && world.isValid(pos = new BlockPos(x, y, z))) {
                if ((tile = world.getTileEntity(pos)) instanceof GlassKilnEntity) {
                    throw new UnsupportedOperationException("Kiln GUI not yet implemented");
                }
            }
            return null;
        }
    };

    @Mod.EventHandler
    protected void onPreInitialization(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ID, GUI_HANDLER);
        MinecraftForge.EVENT_BUS.register(GlazedRegistry.INSTANCE);
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        ProxyImpl.eventConsumer.accept(event);
    }

    @SuppressWarnings("unused")
    private static final class ProxyImpl {
        @SidedProxy
        private static Consumer<FMLInitializationEvent> eventConsumer = it -> {
            throw new IllegalStateException("Sided proxy instance has not been initialized!");
        };

        @SideOnly(Side.CLIENT)
        public static final class ClientProxy implements Consumer<FMLInitializationEvent> {
            @Override
            public void accept(FMLInitializationEvent event) {
                MinecraftForge.EVENT_BUS.register(GlazedClient.INSTANCE);
                final IResourceManager rm = FMLClientHandler.instance().getClient().getResourceManager();
                if (rm instanceof IReloadableResourceManager) {
                    ((IReloadableResourceManager) rm).registerReloadListener(GlazedClient.INSTANCE);
                } else throw new IllegalStateException("Expected IReloadableResourceManager, found " + rm);
            }
        }

        @SideOnly(Side.SERVER)
        public static final class ServerProxy implements Consumer<FMLInitializationEvent> {
            @Override
            public void accept(FMLInitializationEvent event) {}
        }
    }
}
