package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GhostManager {
    private final ReviveMe plugin;
    private final Set<UUID> activeGhosts = new HashSet<>();

    public GhostManager(ReviveMe plugin) {
        this.plugin = plugin;
    }

    public boolean isGhost(UUID uuid) {
        return activeGhosts.contains(uuid);
    }

    public void setGhost(Player player) {
        activeGhosts.add(player.getUniqueId());

        player.setGameMode(GameMode.ADVENTURE);
        MessageUtil.send(player, "no-lives");
        MessageUtil.send(player, "ghost-welcome");

        // --- TITLE SAAT MATI (Modern & Elegan) ---
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500));
        Title title = Title.title(
                MessageUtil.parse("<dark_red>You Died</dark_red>"),
                MessageUtil.parse("<gray>Your soul is fading away...</gray>"),
                times
        );
        player.showTitle(title);
        // ------------------------------------------

        Location loc = player.getLocation();
        player.playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 0.5f);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 1, false, false));

        // Animasi Ditarik ke Atas
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!player.isOnline() || ticks >= 40) {
                    teleportToDeathWorld(player);
                    this.cancel();
                    return;
                }
                Location currentLoc = player.getLocation();
                currentLoc.add(0, 0.25, 0);
                player.teleport(currentLoc);
                currentLoc.getWorld().spawnParticle(Particle.SOUL, currentLoc, 10, 0.3, 0.3, 0.3, 0.05);
                ticks += 2;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private void teleportToDeathWorld(Player player) {
        String dimName = plugin.getConfigManager().getDeathDimension();
        World deathWorld = Bukkit.getWorld(dimName);
        if (deathWorld != null) {
            player.teleport(deathWorld.getSpawnLocation());
        }
        player.removePotionEffect(PotionEffectType.LEVITATION);
    }

    public void unsetGhost(Player player) {
        activeGhosts.remove(player.getUniqueId());

        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.LEVITATION);

        player.setGameMode(GameMode.SURVIVAL);
        MessageUtil.send(player, "ritual-success");

        // --- TITLE SAAT DIBANGKITKAN (Modern & Elegan) ---
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500));
        Title title = Title.title(
                MessageUtil.parse("<green>Resurrected</green>"),
                MessageUtil.parse("<gray>Welcome back to the living.</gray>"),
                times
        );
        player.showTitle(title);
        // -------------------------------------------------

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, player.getLocation(), 100, 1.0, 1.0, 1.0, 0.5);

        World mainWorld = Bukkit.getWorld("world");
        if (mainWorld != null) {
            player.teleport(mainWorld.getSpawnLocation());
        }
    }

    public Set<UUID> getActiveGhosts() {
        return activeGhosts;
    }
}