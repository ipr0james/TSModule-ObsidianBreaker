package net.thenova.titan.spigot.module.obsidianbreaker.handler.tasks;

import net.thenova.titan.spigot.module.obsidianbreaker.handler.OBHandler;
import org.bukkit.scheduler.BukkitRunnable;

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
public final class TaskEffect extends BukkitRunnable {

    @Override
    public void run() {
        OBHandler.INSTANCE.getBlocks()
                .entrySet()
                .stream()
                .filter(val -> val.getKey().isLoaded())
                .forEach(val -> val.getValue()
                        .forEach((loc, state) -> OBHandler.INSTANCE.renderCracks(loc.getBlock())));
    }
}
