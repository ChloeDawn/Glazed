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

import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public final class GlazedProxy {
    public static final String CLIENT = "net.insomniakitten.glazed.GlazedProxy$ClientImpl";
    public static final String SERVER = "net.insomniakitten.glazed.GlazedProxy$Impl";

    @SidedProxy(modId = Glazed.ID, clientSide = CLIENT, serverSide = SERVER)
    private static Impl instance = null;

    private GlazedProxy() {}

    protected static Impl getInstance() {
        return Objects.requireNonNull(instance, "Proxy instance has not been initialized!");
    }

    protected interface Impl {
        default void onPreInitialization(FMLPreInitializationEvent event) {}
        default void onInitialization(FMLInitializationEvent event) {}
        default void onPostInitialization(FMLPostInitializationEvent event) {}
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static final class ClientImpl implements Impl {
        @Override
        public void onPreInitialization(FMLPreInitializationEvent event) {
            MinecraftForge.EVENT_BUS.register(GlazedClient.INSTANCE);
        }

        @Override
        public void onInitialization(FMLInitializationEvent event) {
            final IResourceManager rm = FMLClientHandler.instance().getClient().getResourceManager();
            if (rm instanceof IReloadableResourceManager) {
                ((IReloadableResourceManager) rm).registerReloadListener(GlazedClient.INSTANCE);
            } else throw new IllegalStateException("Expected IReloadableResourceManager, found " + rm);
        }
    }
}
