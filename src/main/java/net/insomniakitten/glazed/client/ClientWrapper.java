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
            public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                GlassBlockType type = GlassBlockType.getType(state);
                if (type.equals(GlassBlockType.GAIA)) {
                    if (world != null && pos != null ) {
                        return BiomeColorHelper.getGrassColorAtPos(world, pos);
                    } else {
                        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                    }
                } else {
                    return -1;
                }
            }
        }, Glazed.Objects.BGLASS);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override @SideOnly(Side.CLIENT)
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                GlassBlockType type = GlassBlockType.getType(stack.getMetadata());
                if (player != null && type.equals(GlassBlockType.GAIA)) {
                    BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
                    if (player.world != null) {
                        return BiomeColorHelper.getGrassColorAtPos(player.world, pos);
                    } else {
                        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                    }
                } else {
                    return -1;
                }
            }
        }, Glazed.Objects.IGLASS); // sand and glass itemblocks
    }

    @Override @SideOnly(Side.CLIENT)
    public void parseSpecials() {
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
        ResourceLocation rl = new ResourceLocation(Glazed.MOD_ID, "data/specials.json");
        Gson gson = new Gson(); JsonElement element;
        try {
            InputStreamReader reader = new InputStreamReader(manager.getResource(rl).getInputStream());
            element = gson.fromJson(reader, JsonElement.class);
            reader.close();
        } catch (Exception ignored) {
            Glazed.LOGGER.warn("Failed to parse specials.json!");
            return;
        }

        JsonObject json = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> group : json.entrySet()) {
            JsonObject obj = group.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                SPECIALS.put(Pair.of(UUID.fromString(e.getKey()), group.getKey()), e.getValue().getAsString());
            }
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public UUID getUUID() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            return player.getGameProfile().getId();
        } else return EMPTY_UUID;
    }

}
