package net.insomniakitten.glazed.proxy;

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

import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.compat.chisel.ChiselHelper;
import net.insomniakitten.glazed.compat.tconstruct.TiConHelper;
import net.insomniakitten.glazed.compat.tconstruct.TiConRegistry;
import net.insomniakitten.glazed.tile.TileEntityBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.function.Supplier;

public class GlazedProxy {

    public static final String CLIENT = "net.insomniakitten.glazed.proxy.ClientProxy";
    public static final String SERVER = "net.insomniakitten.glazed.proxy.ServerProxy";

    protected GlazedProxy() {}

    public void onPreInit(FMLPreInitializationEvent event) {
        if (ChiselHelper.isChiselLoaded()) {
            Glazed.LOGGER.info("Chisel is present, registering additional features");
        }
        if (TiConHelper.isTiConLoaded()) {
            Glazed.LOGGER.info("Tinkers' Construct is present, registering additional features");
            TiConRegistry.onPreInit(event);
        }
    }

    public void onInit(FMLInitializationEvent event) {

    }

    public void onPostInit(FMLPostInitializationEvent event) {

    }

    public <T extends TileEntityBase> void bindTESR(Supplier<TileEntitySpecialRenderer<T>> render, Class<T> tile) {

    }

}


