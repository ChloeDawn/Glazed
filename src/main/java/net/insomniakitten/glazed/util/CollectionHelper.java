package net.insomniakitten.glazed.util;

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

import java.util.Collection;
import java.util.function.BiConsumer;

public final class CollectionHelper {

    private CollectionHelper() {}

    public static <T, U> void forThoseOf(Collection<? extends T> objects, Class<U> type, BiConsumer<? super T, ? super U> action) {
        objects.stream().filter(type::isInstance).forEach(e -> action.accept(e, type.cast(e)));
    }

}


