package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class AltarManager {
    private final ReviveMe plugin;
    // Menyimpan daftar lokasi altar yang valid
    private final Set<Location> altars = new HashSet<>();

    public AltarManager(ReviveMe plugin) {
        this.plugin = plugin;
    }

    // Memberikan item Soul Altar ke admin
    public void giveAltarItem(Player player) {
        ItemStack altar = ItemUtil.getSoulAltarItem();
        player.getInventory().addItem(altar);
    }

    // Mendaftarkan lokasi baru saat Soul Altar diletakkan
    public void registerAltar(Location location) {
        // Pastikan lokasinya integer (tidak ada desimal)
        Location roundedLoc = new Location(location.getWorld(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
        altars.add(roundedLoc);
    }

    // Menghapus altar jika blok dihancurkan
    public void unregisterAltar(Location location) {
        Location roundedLoc = new Location(location.getWorld(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
        altars.remove(roundedLoc);
    }

    // Cek apakah blok yang diklik adalah Soul Altar
    public boolean isAltar(Block block) {
        Location loc = block.getLocation();
        return block.getType() == Material.LODESTONE && altars.contains(loc);
    }
}