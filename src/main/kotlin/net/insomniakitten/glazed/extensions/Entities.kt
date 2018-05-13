@file:JvmName("Entities")

package net.insomniakitten.glazed.extensions

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.Vec3d
import net.minecraftforge.items.ItemHandlerHelper

inline val Entity.mirroredFacing: EnumFacing get() = horizontalFacing.opposite

fun Entity.getPosition(partialTicks: Float) = Vec3d(
        lastTickPosX + (posX - lastTickPosX) * partialTicks,
        lastTickPosY + (posY - lastTickPosY) * partialTicks,
        lastTickPosZ + (posZ - lastTickPosZ) * partialTicks
)

fun EntityPlayer.giveItem(stack: ItemStack, preferredSlot: Int) =
        ItemHandlerHelper.giveItemToPlayer(this, stack, preferredSlot)

fun EntityPlayer.giveItem(stack: ItemStack) =
        ItemHandlerHelper.giveItemToPlayer(this, stack)

operator fun EntityLivingBase.get(hand: EnumHand): ItemStack = getHeldItem(hand)

operator fun EntityLivingBase.set(hand: EnumHand, stack: ItemStack) = setHeldItem(hand, stack)

operator fun EntityLivingBase.get(slot: EntityEquipmentSlot): ItemStack = getItemStackFromSlot(slot)

operator fun EntityLivingBase.set(slot: EntityEquipmentSlot, stack: ItemStack) = setItemStackToSlot(slot, stack)
