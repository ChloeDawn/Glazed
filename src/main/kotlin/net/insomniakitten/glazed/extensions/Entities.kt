@file:JvmName("Entities")

package net.insomniakitten.glazed.extensions

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

fun EntityPlayer.giveItem(stack: ItemStack) = giveItem(stack, -1)

operator fun EntityLivingBase.get(hand: EnumHand): ItemStack = getHeldItem(hand)

operator fun EntityLivingBase.set(hand: EnumHand, stack: ItemStack) = setHeldItem(hand, stack)

operator fun EntityLivingBase.get(slot: EntityEquipmentSlot): ItemStack = getItemStackFromSlot(slot)

operator fun EntityLivingBase.set(slot: EntityEquipmentSlot, stack: ItemStack) = setItemStackToSlot(slot, stack)
