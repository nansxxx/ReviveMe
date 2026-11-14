package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AltarInteractListener implements Listener {
    private final ReviveMe plugin;

    public AltarInteractListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAltarInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        if (plugin.getAltarManager().isAltar(event.getClickedBlock())) {
            event.setCancelled(true);

            Player reviver = event.getPlayer();
            ItemStack itemInHand = reviver.getInventory().getItemInMainHand();

            // Cek apakah membawa Soul Head (Nether Star)
            if (itemInHand.getType() == Material.NETHER_STAR && itemInHand.hasItemMeta()) {

                String displayName = itemInHand.getItemMeta().getDisplayName();
                Player target = null;

                // Cari pemain hantu di dekat altar
                for (Player p : reviver.getWorld().getNearbyPlayers(reviver.getLocation(), 10.0)) {
                    if (plugin.getLifeManager().getData(p).isGhost() && displayName.contains(p.getName())) {
                        target = p;
                        break;
                    }
                }

                if (target == null) {
                    MessageUtil.send(reviver, "no-ghost-near");
                    return;
                }

                // Mulai ritual!
                plugin.getRitualManager().startRitual(reviver, target, event.getClickedBlock().getLocation());

                // Hapus Soul Head dari tangan
                if (itemInHand.getAmount() > 1) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                } else {
                    reviver.getInventory().setItemInMainHand(null);
                }

            } else {
                MessageUtil.send(reviver, "need-soul-head");
            }
        }
    }

    // Event Handler untuk Revive Wand
    @EventHandler
    public void onWandInteract(PlayerInteractEvent event) {
        // Cek jika klik kanan (di udara atau di blok)
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Cek apakah yang dipegang Revive Wand (CMD 1004)
        if (item.getType() == Material.BLAZE_ROD && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()
                && item.getItemMeta().getCustomModelData() == 1004) {

            event.setCancelled(true); // Cegah interaksi lain

            // Cari pemain hantu terdekat (radius 50 blok)
            Player target = null;
            for (Player p : player.getWorld().getNearbyPlayers(player.getLocation(), 50.0)) {
                if (plugin.getLifeManager().getData(p).isGhost()) {
                    target = p;
                    break;
                }
            }

            if (target == null) {
                MessageUtil.send(player, "no-ghost-near");
                return;
            }

            // Bangkitkan target
            plugin.getGhostManager().unsetGhost(target);

            // Hancurkan wand (sekali pakai)
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }

            // Efek suara
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
            target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
        }
    }
}