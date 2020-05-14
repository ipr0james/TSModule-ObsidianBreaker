package net.thenova.titan.spigot.module.obsidianbreaker.listeners;

import net.thenova.titan.spigot.module.obsidianbreaker.handler.OBHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
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
public final class EntityEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.isCancelled()) {
            return;
        }

        final Location location = event.getLocation();
        final World world = location.getWorld();
        final int radius = OBHandler.INSTANCE.getRadius();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Location loc = new Location(world, location.getX() + x, location.getY() + y, location.getZ() + z);

                    if (location.distance(loc) <= radius) {
                        if(!loc.getChunk().isLoaded() || loc.getBlockY() == 0) {
                            return;
                        }

                        final Block block = world.getBlockAt(loc);
                        if(block.getType() == Material.OBSIDIAN) {
                            final boolean isLiquid = getIntersectingBlocks(location, loc).stream().anyMatch(Block::isLiquid);

                            if(!isLiquid) {
                                if(OBHandler.INSTANCE.addDamage(block)) {
                                    OBHandler.INSTANCE.sendCrackEffect(block.getLocation(), -1);
                                    block.setType(Material.AIR);
                                } else {
                                    OBHandler.INSTANCE.renderCracks(block);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<Block> getIntersectingBlocks(final Location explosionSource, final Location block) {
        List<Block> list = new ArrayList<>();

        int pixelX = explosionSource.getBlockX();
        int pixelY = explosionSource.getBlockY();
        int pixelZ = explosionSource.getBlockZ();
        int dx = block.getBlockX() - explosionSource.getBlockX();
        int dy = block.getBlockY() - explosionSource.getBlockY();
        int dz = block.getBlockZ() - explosionSource.getBlockZ();
        int x_inc = dx < 0 ? -1 : 1;
        int l = Math.abs(dx);
        int y_inc = dy < 0 ? -1 : 1;
        int m = Math.abs(dy);
        int z_inc = dz < 0 ? -1 : 1;
        int n = Math.abs(dz);
        int dx2 = l << 1;
        int dy2 = m << 1;
        int dz2 = n << 1;
        if ((l >= m) && (l >= n)) {
            int err_1 = dy2 - l;
            int err_2 = dz2 - l;
            for (int i = 0; i < l; i++) {
                list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
                if (err_1 > 0) {
                    pixelY += y_inc;
                    err_1 -= dx2;
                }
                if (err_2 > 0) {
                    pixelZ += z_inc;
                    err_2 -= dx2;
                }
                err_1 += dy2;
                err_2 += dz2;
                pixelX += x_inc;
            }
        } else if ((m >= l) && (m >= n)) {
            int err_1 = dx2 - m;
            int err_2 = dz2 - m;
            for (int i = 0; i < m; i++) {
                list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
                if (err_1 > 0) {
                    pixelX += x_inc;
                    err_1 -= dy2;
                }
                if (err_2 > 0) {
                    pixelZ += z_inc;
                    err_2 -= dy2;
                }
                err_1 += dx2;
                err_2 += dz2;
                pixelY += y_inc;
            }
        } else {
            int err_1 = dy2 - n;
            int err_2 = dx2 - n;
            for (int i = 0; i < n; i++) {
                list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
                if (err_1 > 0) {
                    pixelY += y_inc;
                    err_1 -= dz2;
                }
                if (err_2 > 0) {
                    pixelX += x_inc;
                    err_2 -= dz2;
                }
                err_1 += dy2;
                err_2 += dx2;
                pixelZ += z_inc;
            }
        }
        list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));

        return list;
    }
}
