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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public final class SpecialsParser {

    public static final Map<Pair<UUID, String>, String> DATA = new HashMap<>();

    private static final ResourceLocation PATH = new ResourceLocation(Glazed.ID, "data/specials.json");

    private SpecialsParser() {}

    public static void registerResourceListener() {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
                .registerReloadListener(SpecialsParser::parseSpecials);
    }

    private static void parseSpecials(IResourceManager rm) {
        JsonElement specialsData;

        try (InputStreamReader reader = new InputStreamReader(rm.getResource(PATH).getInputStream())) {
            specialsData = new JsonParser().parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DATA.clear(); // Flush before population

        specialsData.getAsJsonObject().entrySet().forEach(group -> {
            JsonObject object = group.getValue().getAsJsonObject();
            object.entrySet().forEach(entry -> DATA.put(
                    Pair.of(UUID.fromString(entry.getKey()), group.getKey()),
                    entry.getValue().getAsString()));
        });
    }

}
