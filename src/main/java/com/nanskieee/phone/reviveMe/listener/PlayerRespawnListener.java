package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    private final ReviveMe plugin;

    public PlayerRespawnListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // Jika pemain adalah hantu (nyawa 0)
        if (plugin.getLifeManager().getData(player).isGhost()) {

            // Cek apakah dunia dimensi kematian ada
            String dimName = plugin.getConfigManager().getDeathDimension();
            World deathWorld = Bukkit.getWorld(dimName);

            if (deathWorld != null) {
                // Paksa titik respawn ke dimensi kematian
                event.setRespawnLocation(deathWorld.getSpawnLocation());
            }

            // Set mode jadi hantu (Adventure/Spectator)
            plugin.getGhostManager().setGhost(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Beri jeda 1 detik agar server selesai memuat pemain
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getLifeManager().getData(player).isGhost()) {

                // Jika dia login dan ternyata hantu, teleport ke dimensi kematian
                String dimName = plugin.getConfigManager().getDeathDimension();
                World deathWorld = Bukkit.getWorld(dimName);

                if (deathWorld != null) {
                    player.teleport(deathWorld.getSpawnLocation());
                }

                // Pastikan mode-nya tetap mode hantu
                plugin.getGhostManager().setGhost(player);
            }
        }, 20L);
    }
}