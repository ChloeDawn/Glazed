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

import com.google.common.collect.ImmutableList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class RegistryFactory<T extends IForgeRegistryEntry<T>> {

    private State state = new StateNew();

    public ImmutableList<T> entries() {
        return state.entries();
    }

    public Builder begin(RegistryEvent.Register<T> event) {
        return transition(state.begin(event));
    }

    private <R> R transition(StateTransition<R> transition) {
        state = transition.getState();
        return transition.getResult();
    }

    public final class Builder {

        private final IForgeRegistry<T> registry;

        private final ImmutableList.Builder<T> entries = ImmutableList.builder();

        private Builder(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        public Builder register(Supplier<T> supplier) {
            T obj = supplier.get();
            registry.register(obj);
            entries.add(obj);
            return this;
        }

        public Builder registerAll(Collection<Supplier<T>> suppliers) {
            suppliers.forEach(this::register);
            return this;
        }

        public Builder registerIf(Supplier<T> supplier, BooleanSupplier condition) {
            if (condition.getAsBoolean()) register(supplier);
            return this;
        }

        public RegistryFactory<T> end() {
            return transition(new StateTransition<>(new StateBuilt(entries.build()), RegistryFactory.this));
        }

    }

    private final class StateTransition<R> {

        private final State state;

        private final R result;

        private StateTransition(State state, R result) {
            this.state = state;
            this.result = result;
        }

        private State getState() {
            return state;
        }

        private R getResult() {
            return result;
        }

    }

    private abstract class State {

        public abstract ImmutableList<T> entries();

        public abstract StateTransition<Builder> begin(RegistryEvent.Register<T> event);

    }

    private final class StateNew extends StateBuilt {

        private StateNew() {
            super(ImmutableList.of());
        }

    }

    private final class StateBuilding extends State {

        @Override
        public ImmutableList<T> entries() {
            throw new ConcurrentModificationException();
        }

        @Override
        public StateTransition<Builder> begin(RegistryEvent.Register<T> event) {
            throw new ConcurrentModificationException();
        }

    }

    private class StateBuilt extends State {

        private final ImmutableList<T> entries;

        private StateBuilt(ImmutableList<T> entries) {
            this.entries = entries;
        }

        @Override
        public final ImmutableList<T> entries() {
            return entries;
        }

        @Override
        public final StateTransition<Builder> begin(RegistryEvent.Register<T> event) {
            return new StateTransition<>(new StateBuilding(), new Builder(event.getRegistry()));
        }

    }

}

