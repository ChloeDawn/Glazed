package net.insomniakitten.glazed;

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

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Glazed.ID)
public final class GlazedConfig {

    private GlazedConfig() {}

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Glazed.ID)) {
            ConfigManager.sync(Glazed.ID, Config.Type.INSTANCE);
        }
    }

    @Config(modid = Glazed.ID, name = Glazed.ID + "/compat", category = "")
    public static class Compat {

        @Config.LangKey("config.glazed.compat.chisel")
        public static final Chisel CHISEL = new Chisel();

        @Config.LangKey("config.glazed.compat.tconstruct")
        public static final TConstruct TCONSTRUCT = new TConstruct();

        private Compat() {}

        public static class Chisel {

            @Config.RequiresMcRestart
            @Config.Comment("Should the glass chisel be registered when Chisel is present? [default: true]")
            @Config.LangKey("config.glazed.compat.chisel.glassChisel")
            public boolean enableGlassChisel = true;

            @Config.RequiresMcRestart
            @Config.Comment("The maximum attack damage of the glass chisel. [default: 3.0]")
            @Config.LangKey("config.glazed.compat.chisel.glassChiselDamage")
            public float glassChiselDamage = 3.0F;

            @Config.RequiresMcRestart
            @Config.Comment("The maximum durability of the glass chisel. [default: 256]")
            @Config.LangKey("config.glazed.compat.chisel.glassChiselDamage")
            public int glassChiselDurability = 256;

        }

        public static class TConstruct {

        }

    }

    @Config(modid = Glazed.ID, name = Glazed.ID + "/general", category = "")
    public static class General {

        @Config.LangKey("config.glazed.general.kiln")
        public static final Kiln KILN = new Kiln();

        private General() {}

        public static class Kiln {

            @Config.RequiresMcRestart
            @Config.Comment("Determines whether the dyed glass kiln recipes should be registered. [default: true]")
            @Config.LangKey("config.glazed.kiln.dyedGlassRecipes")
            public boolean dyedGlassRecipes = true;

            @Config.RequiresMcRestart
            @Config.Comment("The speed at which items are processed, relative to furnace speed. [default: 1.0]")
            @Config.LangKey("config.glazed.kiln.processingSpeed")
            public float processingSpeed = 1.0F;

        }

    }

}
