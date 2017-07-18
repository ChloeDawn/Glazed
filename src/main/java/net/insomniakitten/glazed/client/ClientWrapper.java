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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.glass.GlassBlockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ClientWrapper extends Glazed.ProxyWrapper {

    public static final HashMap<Pair<UUID, String>, String> SPECIALS = new HashMap<>();

    @Override
    @SideOnly(Side.CLIENT)
    public void registerColorHandler() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override @SideOnly(Side.CLIENT)
            public int colorMultiplier(
                    IBlockState state, @Nullable IBlockAccess world,
                    @Nullable BlockPos pos, int tintIndex) {
                if (!GlassBlockType.getType(state).equals(GlassBlockType.GAIA)) return -1;
                return world != null && pos != null ?
                        BiomeColorHelper.getGrassColorAtPos(world, pos)
                        : ColorizerGrass.getGrassColor(0.5D, 1.0D);
            }
        }, Glazed.Objects.BGLASS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override @SideOnly(Side.CLIENT)
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                EntityPlayer plr = Minecraft.getMinecraft().player;
                if (plr == null) return -1;
                if (!GlassBlockType.getType(stack.getMetadata()).equals(GlassBlockType.GAIA)) return -1;
                BlockPos pos = new BlockPos(plr.posX, plr.posY, plr.posZ);
                return plr.world != null ?
                        BiomeColorHelper.getGrassColorAtPos(plr.world, pos)
                        : ColorizerGrass.getGrassColor(0.5D, 1.0D);
            }
        }, Glazed.Objects.IGLASS); // sand and glass itemblocks
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void parseSpecials() {
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
        ResourceLocation rl = new ResourceLocation(Glazed.MOD_ID, "data/specials.json");
        Gson gson = new Gson(); JsonElement ele;

        try {
            IResource resource = manager.getResource(rl);
            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            ele = gson.fromJson(reader, JsonElement.class);
            reader.close();
        } catch (Exception ignored) {
            Glazed.LOGGER.warn("Failed to parse specials.json!");
            return;
        }

        JsonObject json = ele.getAsJsonObject();
        for (Map.Entry<String, JsonElement> group : json.entrySet()) {
            JsonObject obj = group.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                SPECIALS.put(Pair.of(
                        UUID.fromString(e.getKey()), group.getKey()),
                        e.getValue().getAsString());
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public UUID getUUID() {
        EntityPlayer plr = Minecraft.getMinecraft().player;
        return plr != null ? plr.getGameProfile().getId() : EMPTY_UUID;
    }

}
