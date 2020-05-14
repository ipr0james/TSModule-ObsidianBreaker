package net.thenova.titan.spigot.module.obsidianbreaker.handler;

import de.arraying.kotys.JSON;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import net.thenova.titan.library.file.FileHandler;
import net.thenova.titan.spigot.TitanSpigot;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.data.BlockState;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.data.OBDataFile;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.tasks.TaskEffect;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.tasks.TaskRegen;
import net.thenova.titan.spigot.util.task.TaskHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.concurrent.ConcurrentHashMap;

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
public enum OBHandler {
    INSTANCE;

    private final ConcurrentHashMap<Chunk, ConcurrentHashMap<Location, BlockState>> blocks = new ConcurrentHashMap<>();

    private int durability;
    private int radius;

    public void load() {
        final JSON json = FileHandler.INSTANCE.loadJSONFile(OBDataFile.class).getJSON();

        this.durability = json.integer("durability");
        this.radius = json.integer("radius");

        TaskHandler.INSTANCE.addTask(new TaskEffect().runTaskTimerAsynchronously(TitanSpigot.INSTANCE.getPlugin(), 0, 5 * 20).getTaskId());
        TaskHandler.INSTANCE.addTask(new TaskRegen().runTaskTimerAsynchronously(TitanSpigot.INSTANCE.getPlugin(), 0, 10 * 20).getTaskId());
    }

    public boolean addDamage(Block block) {
        BlockState state = getBlockStatus(block, false);

        if (state == null
                && (state = getBlockStatus(block, true)) == null) {
            return false;
        }

        state.setDamage(state.getDamage() + 1);
        if (state.getDamage() >= this.durability - 0.001F) {
            removeBlockState(state);
            return true;
        }
        return false;
    }

    public BlockState getBlockStatus(final Block block, final boolean create) {
        final Chunk chunk = block.getChunk();
        ConcurrentHashMap<Location, BlockState> chunkMap;

        if (this.blocks.containsKey(chunk)) {
            chunkMap = this.blocks.get(chunk);
        } else if (create) {
            this.blocks.put(chunk, new ConcurrentHashMap<>());
            chunkMap = this.blocks.get(chunk);
        } else {
            return null;
        }

        final Location location = block.getLocation();

        if (chunkMap.containsKey(location)) {
            return chunkMap.get(location);
        }

        if (create) {
            chunkMap.put(location, new BlockState(location));
            return chunkMap.get(location);
        }

        return null;
    }

    public void removeBlockState(final BlockState state) {
        final Chunk chunk = state.getChunk();
        final ConcurrentHashMap<Location, BlockState> data = this.blocks.get(chunk);

        data.remove(state.getLocation());
        if (data.isEmpty()) {
            this.blocks.remove(chunk);
        }
    }

    public void sendCrackEffect(final Location location, final int damage) {
        final int x = location.getBlockX();
        final int y = location.getBlockY();
        final int z = location.getBlockZ();
        final int dimension = ((CraftWorld)location.getWorld()).getHandle().dimension;

        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(x, y, z, 30.0D, dimension,
                new PacketPlayOutBlockBreakAnimation(location.hashCode(), new BlockPosition(x, y, z), damage));
    }

    public void renderCracks(final Block block) {
        final BlockState state = getBlockStatus(block, false);
        if (state == null || this.durability <= 0.0F) {
            return;
        }

        sendCrackEffect(block.getLocation(), 10 - (int)Math.ceil((this.durability - state.getDamage()) / this.durability * 10.0F));
    }

    public final ConcurrentHashMap<Chunk, ConcurrentHashMap<Location, BlockState>> getBlocks() {
        return this.blocks;
    }
    public final int getDurability() {
        return this.durability;
    }
    public final int getRadius() {
        return this.radius;
    }
}
