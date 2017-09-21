package net.insomniakitten.glazed.client;

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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientHelper {

    private static final BlockPos.MutableBlockPos PLAYER_POS = new BlockPos.MutableBlockPos();
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }

    public static World getWorld() {
        return getPlayer().world;
    }

    public static boolean isPlayerLoaded() {
        return getPlayer() != null;
    }

    public static boolean isWorldLoaded() {
        return isPlayerLoaded() && getWorld() != null;
    }

    public static BlockPos getPlayerPos() {
        return isPlayerLoaded()
               ? PLAYER_POS.setPos(getPlayer().posX, getPlayer().posY, getPlayer().posZ)
               : BlockPos.ORIGIN;
    }

    public static RayTraceResult getRayTrace() {
        return Minecraft.getMinecraft().objectMouseOver;
    }

    public static UUID getPlayerUUID() {
        return isPlayerLoaded() ? getPlayer().getUniqueID() : EMPTY_UUID;
    }

}
