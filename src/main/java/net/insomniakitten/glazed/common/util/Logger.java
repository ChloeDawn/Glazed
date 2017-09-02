package net.insomniakitten.glazed.common.util;

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
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;

public class Logger {

    public static final boolean DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Glazed.MOD_NAME);

    public static void info(boolean global, String msg, Object... params) {
        if (global || DEOBF) {
            LOGGER.info(msg, params);
        }
    }

    public static void warn(boolean global, String msg, Object... params) {
        if (global || DEOBF) {
            LOGGER.warn(msg, params);
        }
    }

    public static void error(boolean global, String msg, Object... params) {
        if (global || DEOBF) {
            LOGGER.error(msg, params);
        }
    }

}
