package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerDeathListener implements Listener {
    private final ReviveMe plugin;

    public PlayerDeathListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // 1. Cek Soul Bound (Simpan item agar tidak drop)
        List<ItemStack> soulBoundItems = new ArrayList<>();
        Iterator<ItemStack> iterator = event.getDrops().iterator();

        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (ItemUtil.hasSoulBound(plugin, item)) {
                soulBoundItems.add(item);
                iterator.remove(); // Hapus dari daftar drop
            }
        }

        // 2. Kurangi nyawa
        plugin.getLifeManager().decrementLife(player);

        // 3. Cek sisa nyawa
        if (plugin.getLifeManager().getData(player).getLives() <= 0) {
            // Jika nyawa habis (0), buat makam dan jadikan hantu
            plugin.getGraveManager().createGrave(player);
            plugin.getGhostManager().setGhost(player);

            // Hapus drop bawaan agar item asli pemain tidak ilang
            event.getDrops().clear();

            // Jatuhkan Soul Head di lokasi kematian
            Location deathLoc = player.getLocation();
            ItemStack soulHead = ItemUtil.getSoulHead(player.getName());
            deathLoc.getWorld().dropItemNaturally(deathLoc, soulHead);
        }

        // 4. Kembalikan item Soul Bound ke inventory pemain saat respawn
        // (Karena drop di-clear atau player jadi hantu, kita simpan di scheduler)
        if (!soulBoundItems.isEmpty()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (ItemStack item : soulBoundItems) {
                    player.getInventory().addItem(item);
                }
            }, 20L); // Delay 1 detik setelah respawn
        }
    }
}