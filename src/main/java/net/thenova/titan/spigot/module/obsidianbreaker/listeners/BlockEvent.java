package net.thenova.titan.spigot.module.obsidianbreaker.listeners;

import net.thenova.titan.spigot.module.obsidianbreaker.handler.OBHandler;
import net.thenova.titan.spigot.module.obsidianbreaker.handler.data.BlockState;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
public final class BlockEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();
        final BlockState state = OBHandler.INSTANCE.getBlockStatus(block, false);
        if (state != null) {
            OBHandler.INSTANCE.removeBlockState(state);
            OBHandler.INSTANCE.sendCrackEffect(block.getLocation(), -1);
        }
    }
}
