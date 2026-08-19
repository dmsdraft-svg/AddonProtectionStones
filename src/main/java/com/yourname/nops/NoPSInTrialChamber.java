package com.yourname.nops;

import dev.espi.protectionstones.ProtectionStones;
import dev.espi.protectionstones.PSBlock;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoPSInTrialChamber extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoPSInTrialChamber загружен! Приваты запрещены в биоме TRIAL_CHAMBERS.");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        PSBlock psBlock = ProtectionStones.getPSBlock(event.getBlock());
        if (psBlock == null) {
            return; 
        }

        Biome biome = event.getBlock().getBiome();
        
        if (biome.name().equals("TRIAL_CHAMBERS")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "⚠ В Камере испытаний запрещено устанавливать блоки привата!");
        }
    }
}
