package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.SoulGrave;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GraveManager {
    private final ReviveMe plugin;
    private final Map<UUID, SoulGrave> graves = new HashMap<>();

    public GraveManager(ReviveMe plugin) {
        this.plugin = plugin;
    }

    // Membuat makam di lokasi pemain mati
    public void createGrave(Player player) {
        Location loc = player.getLocation();

        // Simpan data makam
        SoulGrave grave = new SoulGrave(player.getUniqueId(), loc);
        graves.put(player.getUniqueId(), grave);

        // Buat blok visual (misal pakai tulang atau batu nisan)
        Block block = loc.getBlock();
        loc.getBlock().setType(Material.CRYING_OBSIDIAN);
    }

    // Menghapus makam (dipanggil saat ritual sukses)
    public void removeGrave(Player player) {
        SoulGrave grave = graves.remove(player.getUniqueId());
        if (grave != null) {
            Block block = grave.getLocation().getBlock();
            // Kembalikan jadi udara (hilangkan blok)
            block.setType(Material.AIR);
        }
    }

    public SoulGrave getGrave(UUID uuid) {
        return graves.get(uuid);
    }
}