package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class BossManager implements Listener {
    private final ReviveMe plugin;

    public BossManager(ReviveMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        if (plugin.getConfigManager().isBossEventEnabled()) {
            startBossScheduler();
        }
    }

    private void startBossScheduler() {
        long ticks = plugin.getConfigManager().getBossIntervalHours() * 60L * 60L * 20L; // Jam -> Tick

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            // Cari lokasi altar pertama (sebagai titik spawn boss)
            Location spawnLoc = null;
            // (Logika simplifikasi: kita spawn di lokasi pemain pertama jika ada altar)
            if (!plugin.getServer().getOnlinePlayers().isEmpty()) {
                Player randomPlayer = plugin.getServer().getOnlinePlayers().iterator().next();
                spawnLoc = randomPlayer.getLocation();
                // Idealnya: Ambil lokasi dari AltarManager.getAltars()
            }

            if (spawnLoc == null) return;

            // Spawn Boss
            WitherSkeleton boss = (WitherSkeleton) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
            boss.customName(MessageUtil.parse(plugin.getConfigManager().getBossName()));
            boss.setCustomNameVisible(true);

            // Buff Boss
            boss.getAttribute(Attribute.MAX_HEALTH).setBaseValue(200.0);
            boss.setHealth(200.0);

            // Tandai sebagai Boss (agar tidak bisa di-claim biasa)
            boss.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "soul_boss"), PersistentDataType.BYTE, (byte) 1);

            // Efek Spawn
            spawnLoc.getWorld().strikeLightningEffect(spawnLoc);
            spawnLoc.getWorld().playSound(spawnLoc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);

            // Broadcast ke seluruh server
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                MessageUtil.send(p, "boss-spawn", "<boss>", boss.getName());
            }

        }, ticks, ticks);
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        // Cek apakah yang mati adalah Soul Boss
        if (event.getEntity() instanceof WitherSkeleton boss) {
            if (boss.getPersistentDataContainer().has(new org.bukkit.NamespacedKey(plugin, "soul_boss"), PersistentDataType.BYTE)) {
                // Hapus drop bawaan agar tidak menumpuk
                event.getDrops().clear();

                double reward = plugin.getConfigManager().getBossRewardMoney();

                // Berikan hadiah ke semua pemain yang terlibat
                if (boss.getKiller() != null) {
                    // Berikan hadiah ke killer utama
                    plugin.getEconomyManager().deposit(boss.getKiller(), reward);
                    MessageUtil.send(boss.getKiller(), "boss-reward",
                            "<money>", String.valueOf(reward),
                            "<boss>", boss.getName()
                    );
                }
            }
        }
    }
}