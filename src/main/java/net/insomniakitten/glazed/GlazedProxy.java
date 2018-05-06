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

import java.util.function.Function;

final class GlazedProxy {
    private static final String CLIENT = "net.insomniakitten.glazed.GlazedProxy$EventConsumer$Client";
    private static final String SERVER = "net.insomniakitten.glazed.GlazedProxy$EventConsumer$Server";

    @SidedProxy(modId = Glazed.ID, clientSide = CLIENT, serverSide = SERVER)
    private static EventConsumer consumerProxy = null;

    private static final Function<String, EventConsumer> EVENT_CONSUMER = type -> {
        if (consumerProxy == null) {
            throw new IllegalStateException("EventConsumer proxy instance has not been initialized!");
        } else {
            Glazed.LOGGER.debug("Delegating {} to EventConsumer proxy instance", type);
            return consumerProxy;
        }
    };

    private GlazedProxy() {}

    protected static void onPreInitialization(FMLPreInitializationEvent event) {
        EVENT_CONSUMER.apply(event.getEventType()).accept(event);
    }

    protected static void onInitialization(FMLInitializationEvent event) {
        EVENT_CONSUMER.apply(event.getEventType()).accept(event);
    }

    protected static void onPostInitialization(FMLPostInitializationEvent event) {
        EVENT_CONSUMER.apply(event.getEventType()).accept(event);
    }

    @SuppressWarnings("unused")
    private interface EventConsumer {
        default void accept(FMLPreInitializationEvent event) {}

        default void accept(FMLInitializationEvent event) {}

        default void accept(FMLPostInitializationEvent event) {}

        @SideOnly(Side.CLIENT)
        final class Client implements EventConsumer {
            @Override
            public void accept(FMLPreInitializationEvent event) {
                MinecraftForge.EVENT_BUS.register(GlazedClient.INSTANCE);
            }

            @Override
            public void accept(FMLInitializationEvent event) {
                final IResourceManager rm = FMLClientHandler.instance().getClient().getResourceManager();
                if (rm instanceof IReloadableResourceManager) {
                    ((IReloadableResourceManager) rm).registerReloadListener(GlazedClient.INSTANCE);
                } else throw new IllegalStateException("Expected IReloadableResourceManager, found " + rm);
            }
        }

        @SideOnly(Side.SERVER)
        final class Server implements EventConsumer {}
    }
}
