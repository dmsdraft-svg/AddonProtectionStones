package com.yourname.nops;

import dev.espi.protectionstones.PSBlock;
import dev.espi.protectionstones.PSRegion;
import dev.espi.protectionstones.ProtectionStones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class NoPSInTrialChamber extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoPSInTrialChamber включен!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Block block = event.getBlock();
            List<PSRegion> regions = PSRegion.getRegions(block.getLocation());
            
            if (regions.isEmpty()) return;
            
            PSRegion region = regions.get(0);
            int radius = 0;
            
            if (event.getBlock().getType() == Material.COAL_BLOCK) {
                radius = 25;
            } else if (event.getBlock().getType() == Material.DIAMOND_ORE) {
                radius = 45;
            } else if (event.getBlock().getType() == Material.NETHERITE_BLOCK) {
                radius = 65;
            } else {
                return;
            }
            
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block checkBlock = block.getRelative(x, y, z);
                        Material checkMaterial = checkBlock.getType();
                        
                        if (checkMaterial == Material.VAULT || 
                            checkMaterial == Material.TRIAL_SPAWNER) {
                            
                            region.remove();
                            event.getPlayer().sendMessage(ChatColor.RED + "⛔ Приват удалён! Нельзя ставить рядом с данжем!");
                            return;
                        }
                    }
                }
            }
        }, 1L);
    }
}
