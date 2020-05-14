package net.thenova.titan.spigot.module.obsidianbreaker;

import net.thenova.titan.library.command.data.Command;
import net.thenova.titan.library.database.connection.IDatabase;
import net.thenova.titan.library.database.sql.table.DatabaseTable;
import net.thenova.titan.spigot.data.message.MessageHandler;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.OBHandler;
import net.thenova.titan.spigot.module.obsidianbreaker.listeners.BlockEvent;
import net.thenova.titan.spigot.module.obsidianbreaker.listeners.EntityEvent;
import net.thenova.titan.spigot.plugin.IPlugin;
import net.thenova.titan.spigot.users.user.module.UserModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2019 ipr0james
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class ObsidianBreaker implements IPlugin {
    @Override
    public String name() {
        return "ObsidianBreaker";
    }

    @Override
    public void load() {
        OBHandler.INSTANCE.load();
    }

    @Override
    public void messages(final MessageHandler handler) {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public IDatabase database() {
        return null;
    }

    @Override
    public List<DatabaseTable> tables() {
        return null;
    }

    @Override
    public List<Listener> listeners() {
        return Arrays.asList(
                new BlockEvent(),
                new EntityEvent()
        );
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Command> commands() {
        return null;
    }

    @Override
    public List<Class<? extends UserModule>> user() {
        return null;
    }
}
