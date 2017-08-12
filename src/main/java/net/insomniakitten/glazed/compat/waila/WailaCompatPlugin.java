package net.insomniakitten.glazed.compat.waila;

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

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.compat.waila.provider.GlassDataProvider;
import net.insomniakitten.glazed.compat.waila.provider.KilnDataProvider;
import net.insomniakitten.glazed.glass.BlockGlass;
import net.insomniakitten.glazed.kiln.BlockKiln;

@WailaPlugin(Glazed.MOD_ID)
public class WailaCompatPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new GlassDataProvider(), BlockGlass.class);
        registrar.registerBodyProvider(new KilnDataProvider(), BlockKiln.class);
    }

}
