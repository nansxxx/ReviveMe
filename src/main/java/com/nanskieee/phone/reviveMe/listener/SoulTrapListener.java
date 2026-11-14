package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.MessageUtil; // Pastikan ini di-import
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulTrapListener implements Listener {
    private final ReviveMe plugin;
    private final Map<Location, UUID> activeTraps = new HashMap<>();

    public SoulTrapListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() == Material.SOUL_LANTERN && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()
                && item.getItemMeta().getCustomModelData() == 1006) {

            activeTraps.put(event.getBlock().getLocation(), event.getPlayer().getUniqueId());
            MessageUtil.send(event.getPlayer(), "trap-placed");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        // ANTI-EXPLOIT: Jika pemain bunuh diri, Trap tidak aktif
        if (victim.getLastDamageCause() != null && victim.getLastDamageCause().getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.SUICIDE) {
            return;
        }

        Location deathLoc = victim.getLocation();

        for (Map.Entry<Location, UUID> entry : activeTraps.entrySet()) {
            Location trapLoc = entry.getKey();

            if (trapLoc.getWorld().equals(deathLoc.getWorld()) && trapLoc.distance(deathLoc) <= 10.0) {
                Player owner = plugin.getServer().getPlayer(entry.getValue());

                // PERBAIKAN: Tanda seru (!) agar tidak mencuri nyawa diri sendiri
                if (owner != null && owner.isOnline() && !owner.equals(victim)) {
                    plugin.getLifeManager().decrementLife(victim);
                    plugin.getLifeManager().addLife(owner, 1);

                    MessageUtil.send(owner, "soul-trap-success", "<victim>", victim.getName());
                    MessageUtil.send(victim, "soul-trap-victim", "<owner>", owner.getName());

                    owner.playSound(owner.getLocation(), org.bukkit.Sound.ENTITY_WITHER_HURT, 1f, 1f);

                    trapLoc.getBlock().setType(Material.AIR);
                    activeTraps.remove(trapLoc);
                }
            }
        }
    }
}