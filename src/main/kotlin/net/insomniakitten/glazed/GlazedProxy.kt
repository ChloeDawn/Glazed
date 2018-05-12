package net.insomniakitten.glazed

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

    @Suppress("unused")
    @SideOnly(Side.SERVER)
    class ServerImpl : Impl

    companion object {
        private const val CLIENT = "net.insomniakitten.glazed.GlazedProxy\$ClientImpl"
        private const val SERVER = "net.insomniakitten.glazed.GlazedProxy\$ServerImpl"

        @SidedProxy(modId = Glazed.ID, clientSide = CLIENT, serverSide = SERVER)
        @JvmStatic lateinit var instance: Impl
            private set
    }
}
