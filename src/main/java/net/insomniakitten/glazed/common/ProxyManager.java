package net.insomniakitten.glazed.common;

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
import net.insomniakitten.glazed.common.util.ColorHelper;
import net.insomniakitten.glazed.common.util.Logger;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class ProxyManager {

    public static final String CLIENT = "net.insomniakitten.glazed.common.ProxyManager$ClientWrapper";
    public static final String SERVER = "net.insomniakitten.glazed.common.ProxyManager$ServerWrapper";

    public void onPostInit(FMLPostInitializationEvent event) {
        GuiManager.register();
    }

    @SideOnly(Side.CLIENT)
    public static class ClientWrapper extends ProxyManager {

        @Override
        public void onPostInit(FMLPostInitializationEvent event) {
            super.onPostInit(event);
            Logger.info(true, "Client instance - registering color handlers");
            ColorHelper.registerBiomeColorizer(Glazed.GLASS);
        }

    }

    @SideOnly(Side.SERVER)
    public static class ServerWrapper extends ProxyManager {

        @Override
        public void onPostInit(FMLPostInitializationEvent event) {
            super.onPostInit(event);
            Logger.info(true, "Server instance - skipping color handler registration");
        }

    }

}
