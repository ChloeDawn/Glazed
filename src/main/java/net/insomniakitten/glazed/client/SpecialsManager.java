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
import net.insomniakitten.glazed.common.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
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
import java.util.UUID;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Glazed.MOD_ID, value = Side.CLIENT)
public class SpecialsManager {

    public static final HashMap<Pair<UUID, String>, String> SPECIALS = new HashMap<>();

    private static final ResourceLocation PATH = new ResourceLocation(Glazed.MOD_ID, "data/specials.json");

    @SubscribeEvent
    public static void onSpecialsResourceListenerRegistry(ModelRegistryEvent event) {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
                .registerReloadListener(SpecialsManager::parseSpecials);
    }

    public static void parseSpecials(IResourceManager rm) {
        JsonElement specialsData;

        try (InputStreamReader reader = new InputStreamReader(rm.getResource(PATH).getInputStream())) {
            specialsData = new JsonParser().parse(reader);
        } catch (IOException exception) {
            Logger.error(true, "Failed to parse specials.json!");
            exception.printStackTrace();
            return;
        }

        SPECIALS.clear(); // Flush before population

        specialsData.getAsJsonObject().entrySet().forEach(group -> {
            JsonObject object = group.getValue().getAsJsonObject();
            object.entrySet().forEach(entry -> SPECIALS
                    .put(Pair.of(UUID.fromString(entry.getKey()), group.getKey()), entry.getValue().getAsString()));
        });
    }

}
