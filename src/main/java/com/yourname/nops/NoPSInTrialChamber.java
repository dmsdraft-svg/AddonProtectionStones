package com.yourname.nops;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoPSInTrialChamber extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoPSInTrialChamber включен! Блокировка приватов в Trial Chamber активна.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        // 1. Проверяем биом
        Biome biome = event.getBlock().getBiome();
        String biomeName = biome.name();
        
        // Логи в консоль для отладки
        getLogger().info("Игрок " + event.getPlayer().getName() + 
                       " ставит блок " + event.getBlock().getType().name() + 
                       " в биоме " + biomeName);
        
        // 2. Проверяем, является ли биом Trial Chamber
        boolean isTrialChamber = biomeName.equals("TRIAL_CHAMBERS") || 
                                 biomeName.contains("TRIAL") || 
                                 biomeName.contains("CHAMBER");
        
        if (isTrialChamber) {
            getLogger().info("ОБНАРУЖЕН TRIAL CHAMBER! Блокируем...");
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "В Камере испытаний запрещено ставить приваты!");
        }
    }
}
