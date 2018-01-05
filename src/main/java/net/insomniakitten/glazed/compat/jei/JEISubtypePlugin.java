package net.insomniakitten.glazed.compat.jei;

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

import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import net.insomniakitten.glazed.Glazed;
import net.insomniakitten.glazed.compat.chisel.ChiselHelper;

@JEIPlugin
public final class JEISubtypePlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        if (ChiselHelper.isChiselLoaded()) {
            registry.useNbtForSubtypes(Glazed.CHISEL);
        }
    }

}
