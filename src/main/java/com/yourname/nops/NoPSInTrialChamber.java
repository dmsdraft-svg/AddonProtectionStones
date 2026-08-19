package com.yourname.nops;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoPSInTrialChamber extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoPSInTrialChamber включен! Защита от приватов рядом с Trial Chambers активна.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        // Проверяем только блоки ProtectionStones
        String material = event.getBlock().getType().name();
        int radius = 0;
        
        if (material.equals("COAL_BLOCK")) {
            radius = 25;
        } else if (material.equals("CRYING_OBSIDIAN")) {
            radius = 45;
        } else if (material.equals("NETHERITE_BLOCK")) {
            radius = 65;
        } else {
            return; // Не блок привата
        }
        
        // Проверяем есть ли в радиусе Vault или Trial Spawner
        Block center = event.getBlock();
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block checkBlock = center.getRelative(x, y, z);
                    Material checkMaterial = checkBlock.getType();
                    
                    if (checkMaterial == Material.VAULT || 
                        checkMaterial == Material.TRIAL_SPAWNER) {
                        
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "⛔ Нельзя ставить приват так близко к данжу! (Радиус защиты " + radius + " блоков накроет Хранилище)");
                        return;
                    }
                }
            }
        }
    }
}
