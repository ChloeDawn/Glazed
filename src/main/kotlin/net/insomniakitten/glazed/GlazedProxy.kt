package net.insomniakitten.glazed

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

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class GlazedProxy private constructor() {
    interface Impl {
        val isCtrlKeyDown get() = false

        fun registerEventHandler() {}

        fun registerReloadListener() {}
    }

    @Suppress("unused")
    @SideOnly(Side.CLIENT)
    class ClientImpl : Impl {
        override val isCtrlKeyDown get() = GuiScreen.isCtrlKeyDown()

        override fun registerEventHandler() = MinecraftForge.EVENT_BUS.register(GlazedClient)

        override fun registerReloadListener() = FMLClientHandler.instance().client.resourceManager.let {
            (it as? IReloadableResourceManager)?.registerReloadListener(GlazedClient)
            ?: throw IllegalStateException("Expected IReloadableResourceManager, found $it")
        }
    }

    companion object {
        private const val CLIENT = "net.insomniakitten.glazed.GlazedProxy\$ClientImpl"
        private const val SERVER = "net.insomniakitten.glazed.GlazedProxy\$Impl"

        @SidedProxy(modId = Glazed.ID, clientSide = CLIENT, serverSide = SERVER)
        @JvmStatic lateinit var instance: Impl
            private set
    }
}
