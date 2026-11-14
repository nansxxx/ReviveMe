package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.api.RitualFailEvent;
import com.nanskieee.phone.reviveMe.api.RitualStartEvent;
import com.nanskieee.phone.reviveMe.model.RitualSession;
import com.nanskieee.phone.reviveMe.ritual.RitualTask;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RitualManager {
    private final ReviveMe plugin;
    private final Map<UUID, RitualSession> activeRituals = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public RitualManager(ReviveMe plugin) {
        this.plugin = plugin;
    }

    public void startRitual(Player reviver, Player target, Location altarLocation) {
        // 1. Cek Cooldown
        if (cooldowns.containsKey(reviver.getUniqueId())) {
            long timeLeft = (cooldowns.get(reviver.getUniqueId()) / 1000) - (System.currentTimeMillis() / 1000);
            if (timeLeft > 0) {
                MessageUtil.send(reviver, "cooldown", "<time>", String.valueOf(timeLeft));
                return;
            }
        }

        // 2. Cek Biaya Ekonomi
        double cost = plugin.getConfigManager().getRitualCost();
        if (cost > 0 && plugin.getEconomyManager().hasEconomy()) {
            if (!plugin.getEconomyManager().hasEnough(reviver, cost)) {
                MessageUtil.send(reviver, "no-money", "<cost>", String.valueOf(cost));
                return;
            }
            plugin.getEconomyManager().withdraw(reviver, cost);
            MessageUtil.send(reviver, "paid-ritual", "<cost>", String.valueOf(cost));
        }

        // 3. Mulai Ritual
        RitualSession session = new RitualSession(reviver, target, altarLocation);
        activeRituals.put(reviver.getUniqueId(), session);

        // Panggil Event API: RitualStartEvent
        RitualStartEvent startEvent = new RitualStartEvent(reviver, target);
        Bukkit.getPluginManager().callEvent(startEvent);

        RitualTask task = new RitualTask(plugin, session);
        int taskId = task.runTaskTimer(plugin, 0L, 20L).getTaskId();
        session.setTaskId(taskId);

        reviver.showBossBar(task.getBossBar());

        MessageUtil.send(reviver, "ritual-start");
        MessageUtil.send(target, "ritual-start");

        // 4. Set Cooldown
        cooldowns.put(reviver.getUniqueId(), System.currentTimeMillis() + (plugin.getConfigManager().getRitualCooldown() * 1000L));
    }

    public RitualSession getActiveSession(UUID uuid) {
        return activeRituals.get(uuid);
    }

    public void failRitual(UUID reviverUuid, String reason) {
        RitualSession session = activeRituals.remove(reviverUuid);
        if (session != null) {
            plugin.getServer().getScheduler().cancelTask(session.getTaskId());

            // Panggil Event API: RitualFailEvent
            RitualFailEvent failEvent = new RitualFailEvent(session.getReviver(), reason);
            Bukkit.getPluginManager().callEvent(failEvent);

            Player reviver = session.getReviver();
            if (reviver.isOnline()) {
                MessageUtil.send(reviver, "ritual-fail", "<reason>", reason);
                reviver.getScheduler().run(plugin, (t) -> {
                    for (net.kyori.adventure.bossbar.BossBar bar : reviver.activeBossBars()) {
                        reviver.hideBossBar(bar);
                    }
                }, null);
            }
        }
    }

    public void removeSession(UUID reviverUuid) {
        activeRituals.remove(reviverUuid);
    }

    public void cancelAllRituals() {
        for (UUID uuid : new HashMap<>(activeRituals).keySet()) {
            failRitual(uuid, "Server restarting");
        }
        activeRituals.clear();
    }

    public void handleQuit(Player player) {
        if (activeRituals.containsKey(player.getUniqueId())) {
            failRitual(player.getUniqueId(), "Reviver disconnected");
        }
    }
}