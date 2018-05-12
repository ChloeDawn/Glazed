package net.insomniakitten.glazed.block.entity

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

import net.insomniakitten.glazed.extensions.has
import net.insomniakitten.glazed.extensions.on
import net.insomniakitten.glazed.extensions.spawnAsEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.oredict.OreDictionary.getOreID
import net.minecraftforge.oredict.OreDictionary.getOreIDs

class GlassKilnEntity(val items: ItemStackHandler = object : ItemStackHandler(4) {
    override fun getStackLimit(slot: Int, stack: ItemStack) = when (slot) {
        0 -> if (getOreID("sand") in getOreIDs(stack)) {
            stack.maxStackSize
        } else 0
        1 -> stack.maxStackSize
        2 -> if (getItemBurnTime(stack) > 0) {
            stack.maxStackSize
        } else 0
        else -> 0
    }
}) : TileEntity(), ITickable {
    var isActive = false
        private set

    fun dropItems(world: World, pos: BlockPos) {
        if (!world.isRemote && world.gameRules.getBoolean("doTileDrops")) {
            (0 until items.slots).map(items::getStackInSlot)
                    .forEach { it.spawnAsEntity(world, pos) }
        }
    }

    fun serializeToStack(stack: ItemStack) = stack.run {
        setTagInfo("BlockEntityTag", writeToNBT(NBTTagCompound()))
        setTagInfo("display", NBTTagCompound().apply {
            setTag("Lore", NBTTagList().apply {
                appendTag(NBTTagString("(+NBT)"))
            })
        })
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        isActive = nbt.getBoolean(NBT_KEY_ACTIVE)
        items.deserializeNBT(nbt.getCompoundTag(NBT_KEY_ITEMS))
        super.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        return super.writeToNBT(nbt).apply {
            setBoolean(NBT_KEY_ACTIVE, isActive)
            setTag(NBT_KEY_ITEMS, items.serializeNBT())
        }
    }

    override fun hasCapability(capability: Capability<*>, side: EnumFacing?) =
            capability == ITEM_HANDLER_CAPABILITY

    override fun <T> getCapability(capability: Capability<T>, side: EnumFacing?): T? =
            if (this has (capability on side)) {
                ITEM_HANDLER_CAPABILITY.cast(items)
            } else null

    override fun getUpdatePacket() = SPacketUpdateTileEntity(getPos(), -1, updateTag)

    override fun getUpdateTag() = NBTTagCompound().apply {
        setBoolean(NBT_KEY_ACTIVE, isActive)
    }

    override fun getDisplayName() = TextComponentTranslation("${getBlockType().unlocalizedName}.name")

    @SideOnly(Side.CLIENT)
    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        isActive = pkt.nbtCompound.getBoolean(NBT_KEY_ACTIVE)
    }

    override fun update() {

    }

    companion object {
        private const val NBT_KEY_ACTIVE = "active"
        private const val NBT_KEY_ITEMS = "items"
    }
}


