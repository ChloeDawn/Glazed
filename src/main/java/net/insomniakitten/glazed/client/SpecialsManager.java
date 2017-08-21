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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.insomniakitten.glazed.Glazed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class SpecialsManager {

    public static final HashMap<Pair<UUID, String>, String> SPECIALS = new HashMap<>();

    private static final ResourceLocation PATH = new ResourceLocation(Glazed.MOD_ID, "data/specials.json");
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    @SubscribeEvent @SideOnly(Side.CLIENT)
    public static void onResourceListenerRegistry(ModelRegistryEvent event) {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
                    .registerReloadListener(SpecialsManager::parseSpecials);
    }

    @SideOnly(Side.CLIENT)
    public static void parseSpecials(IResourceManager rm) {
        JsonElement specials;

        try (InputStreamReader reader = new InputStreamReader(rm.getResource(PATH).getInputStream())) {
            specials = new JsonParser().parse(reader);
        } catch (IOException e) {
            Glazed.LOGGER.warn("Failed to parse specials.json!");
            e.printStackTrace();
            return;
        }

        SPECIALS.clear(); // Flush before population

        JsonObject json = specials.getAsJsonObject();
        for (Map.Entry<String, JsonElement> group : json.entrySet()) {
            JsonObject obj = group.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                SPECIALS.put(Pair.of(UUID.fromString(e.getKey()), group.getKey()), e.getValue().getAsString());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static UUID getUUID() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            return player.getGameProfile().getId();
        } else return EMPTY_UUID;
    }

}
