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
import net.insomniakitten.glazed.block.BlockGlass;
import net.insomniakitten.glazed.block.BlockGlassPane;
import net.insomniakitten.glazed.compat.waila.provider.TooltipDataProvider;

@WailaPlugin(Glazed.ID)
public final class WailaProviderPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new TooltipDataProvider(), BlockGlass.class);
        registrar.registerBodyProvider(new TooltipDataProvider(), BlockGlassPane.class);
    }

}
