package net.thenova.titan.spigot.module.obsidianbreaker.handler.data;

import org.bukkit.Chunk;
import org.bukkit.Location;

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
public final class BlockState {

    private final Location location;
    private final Chunk chunk;

    private float damage = 0.0F;
    private boolean modified = true;

    public BlockState(final Location location) {
        this.location = location;
        this.chunk = location.getChunk();
    }

    public final Location getLocation() {
        return location;
    }
    public final Chunk getChunk() {
        return chunk;
    }
    public float getDamage() {
        return this.damage;
    }
    public boolean isModified() {
        return this.modified;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
    public void setModified(boolean modified) {
        this.modified = modified;
    }

}
