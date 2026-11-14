package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.RitualSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RitualInterruptListener implements Listener {
    private final ReviveMe plugin;

    public RitualInterruptListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            RitualSession session = plugin.getRitualManager().getActiveSession(player.getUniqueId());

            if (session != null && plugin.getConfigManager().isFailOnDamage()) {
                plugin.getRitualManager().failRitual(player.getUniqueId(), "Reviver menerima damage!");
            }
        }
    }
}