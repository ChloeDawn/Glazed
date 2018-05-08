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
import net.insomniakitten.glazed.gui.GlassKilnContainer;
import net.insomniakitten.glazed.gui.GlassKilnGui;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(modid = Glazed.ID, name = Glazed.NAME, version = Glazed.VERSION)
public final class Glazed {
    public static final String ID = "glazed";
    public static final String NAME = "Glazed";
    public static final String VERSION = "%VERSION%";

    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final CreativeTabs TAB = new CreativeTabs(ID) {
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
            final TileEntity te;
            if (id == 0 && world.isValid(pos = new BlockPos(x, y, z))) {
                if ((te = world.getTileEntity(pos)) instanceof GlassKilnEntity) {
                    return new GlassKilnContainer(te, player);
                }
            }
            return null;
        }

        @Override
        @Nullable
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            final BlockPos pos;
            final TileEntity te;
            if (id == 0 && world.isValid(pos = new BlockPos(x, y, z))) {
                if ((te = world.getTileEntity(pos)) instanceof GlassKilnEntity) {
                    return new GlassKilnGui(te, player);
                }
            }
            return null;
        }
    };

    @Mod.Instance
    private static Glazed instance = null;

    public static Glazed getInstance() {
        return instance;
    }

    @Mod.EventHandler
    protected void onPreInitialization(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ID, GUI_HANDLER);
        MinecraftForge.EVENT_BUS.register(GlazedRegistry.INSTANCE);
        GlazedProxy.getInstance().onPreInitialization(event);
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        GlazedProxy.getInstance().onInitialization(event);
    }

    @Mod.EventHandler
    protected void onPostInitialization(FMLPostInitializationEvent event) {
        GlazedProxy.getInstance().onPostInitialization(event);
    }
}
