package com.yourname.nops;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class NoPSInTrialChamber extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoPSInTrialChamber включен!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        // Проверяем материал
        Material material = event.getBlock().getType();
        int radius = 0;
        
        if (material == Material.COAL_BLOCK) {
            radius = 25;
        } else if (material == Material.DIAMOND_ORE) {
            radius = 45;
        } else if (material == Material.NETHERITE_BLOCK) {
            radius = 65;
        } else {
            return; // Не блок привата
        }
        
        // Ждём 1 тик пока ProtectionStones создаст регион
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Block block = event.getBlock();
            Location loc = block.getLocation();
            
            // Получаем менеджер регионов
            RegionManager regionManager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(block.getWorld()));
            
            if (regionManager == null) return;
            
            // Проверяем есть ли в радиусе Vault или Trial Spawner
            boolean foundDungeon = false;
            
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block checkBlock = block.getRelative(x, y, z);
                        Material checkMaterial = checkBlock.getType();
                        
                        if (checkMaterial == Material.VAULT || 
                            checkMaterial == Material.TRIAL_SPAWNER) {
                            foundDungeon = true;
                            break;
                        }
                    }
                    if (foundDungeon) break;
                }
                if (foundDungeon) break;
            }
            
            if (foundDungeon) {
                // Ищем регион ProtectionStones в этой точке и удаляем
                Map<String, ProtectedRegion> regions = regionManager.getRegions();
                for (Map.Entry<String, ProtectedRegion> entry : regions.entrySet()) {
                    ProtectedRegion region = entry.getValue();
                    if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                        // Проверяем что это PS регион (начинается с ps-)
                        if (region.getId().startsWith("ps-")) {
                            regionManager.removeRegion(region.getId());
                            event.getPlayer().sendMessage(ChatColor.RED + "⛔ Приват удалён! Нельзя ставить рядом с данжем! (Радиус " + radius + " блоков)");
                            break;
                        }
                    }
                }
            }
        }, 1L);
    }
}
