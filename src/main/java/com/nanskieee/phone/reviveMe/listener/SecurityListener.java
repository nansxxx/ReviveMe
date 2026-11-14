package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityListener implements Listener {
    private final ReviveMe plugin;
    private final Map<UUID, Long> altarCooldown = new HashMap<>();

    public SecurityListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    // 1. ANTI-SPAM KLIK ALTAR (Mencegah Lag Server)
    @EventHandler
    public void onAltarSpam(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        if (plugin.getAltarManager().isAltar(event.getClickedBlock())) {
            Player player = event.getPlayer();
            long now = System.currentTimeMillis();

            // Cek cooldown 1 detik (1000 ms) untuk mencegah spam klik
            if (altarCooldown.containsKey(player.getUniqueId())) {
                if (now - altarCooldown.get(player.getUniqueId()) < 1000) {
                    event.setCancelled(true); // Batalkan interaksi
                    return;
                }
            }
            altarCooldown.put(player.getUniqueId(), now);
        }
    }

    // 2. ANTI-DUPE (Mencegah item masuk Ender Chest atau tempat aneh)
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasCustomModelData()) return;

        int cmd = item.getItemMeta().getCustomModelData();

        // Jika item adalah Soul Fragment (1001), Soul Head (1002), atau Revive Wand (1004)
        if (cmd == 1001 || cmd == 1002 || cmd == 1004) {
            // Cegah dimasukkan ke Ender Chest
            if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                event.setCancelled(true);
            }
            // Cegah dimasukkan ke GUI Shop (Shop kita juga pakai inventory biasa)
            if (event.getView().title().toString().contains("Soul Shop")) {
                event.setCancelled(true);
            }
        }
    }

    // 3. ALTAR PROTECTION (Mencegah Altar hancur oleh TNT/Creeper)
    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> plugin.getAltarManager().isAltar(block));
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> plugin.getAltarManager().isAltar(block));
    }
}