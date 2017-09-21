package net.insomniakitten.glazed.common;

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

import net.insomniakitten.glazed.Glazed;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Glazed.MOD_ID)
@Config(modid = Glazed.MOD_ID, name = Glazed.MOD_ID, category = "")
public class ConfigManager {

    @Config.Name("Glass")
    @Config.LangKey("config.glazed.glass")
    public static GlassConfig glassConfig = new GlassConfig();

    @Config.Name("Kiln")
    @Config.LangKey("config.glazed.kiln")
    public static KilnConfig kilnConfig = new KilnConfig();

    @Config.Name("Shards")
    @Config.LangKey("config.glazed.shards")
    public static ShardsConfig shardsConfig = new ShardsConfig();

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Glazed.MOD_ID)) {
            net.minecraftforge.common.config.ConfigManager.sync(Glazed.MOD_ID, Config.Type.INSTANCE);
        }
    }

    public static class GlassConfig {

        @Config.RequiresMcRestart
        @Config.Name("Enable Functional Glass")
        @Config.Comment({ "Determines whether functional glass should be registered. [default: true]" })
        @Config.LangKey("config.glazed.glass.enableFunctionalGlass")
        public boolean enableFunctionalGlass = true;

    }

    public static class KilnConfig {

        @Config.Name("Processing Speed")
        @Config.Comment({ "The speed at which items are processed, relative to vanilla furnace speed. [default: 1.0]" })
        @Config.LangKey("config.glazed.kiln.processingSpeed")
        public float processingSpeed = 1.0f;

        @Config.RequiresMcRestart
        @Config.Name("Dyed Glass Recipes")
        @Config.Comment({ "Determines whether the efficient dyed glass kiln recipes should be registered. [default: true]" })
        @Config.LangKey("config.glazed.kiln.dyedGlassRecipes")
        public boolean dyedGlassRecipes = true;

    }

    public static class ShardsConfig {

        @Config.RequiresMcRestart
        @Config.Name("Enable Shards")
        @Config.Comment({ "Determines whether glass shards for glass variants should be registered. [default: true]" })
        @Config.LangKey("config.glazed.shards.enableShards")
        public boolean enableShards = true;

        @Config.RequiresMcRestart
        @Config.Name("Anarchy Mode")
        @Config.Comment({ "Determines whether glass shards should be registered for each individual glass block in the ore dictionary. [default: false]" })
        @Config.LangKey("config.glazed.shards.anarchyMode")
        public boolean anarchyMode = false;

        @Config.Name("Tooltip Requires Shift")
        @Config.Comment({ "Determines whether holding shift is required to display the detailed tooltip [default: true]" })
        @Config.LangKey("config.glazed.shards.requireShift")
        public boolean requireShift = true;

    }

}
