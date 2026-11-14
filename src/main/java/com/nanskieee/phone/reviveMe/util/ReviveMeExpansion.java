package com.nanskieee.phone.reviveMe.util;

import com.nanskieee.phone.reviveMe.ReviveMe;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReviveMeExpansion extends PlaceholderExpansion {
    private final ReviveMe plugin;

    public ReviveMeExpansion(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "reviveme"; // Ini akan jadi awalan %reviveme_...
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nanskieee";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Agar tidak hilang saat /papi reload
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // Placeholder: %reviveme_lives%
        if (params.equalsIgnoreCase("lives")) {
            if (player.isOnline()) {
                return String.valueOf(plugin.getLifeManager().getData(player.getPlayer()).getLives());
            }
            return "0"; // Atau ambil dari database jika offline
        }

        // Placeholder: %reviveme_is_ghost%
        if (params.equalsIgnoreCase("is_ghost")) {
            if (player.isOnline()) {
                return String.valueOf(plugin.getLifeManager().getData(player.getPlayer()).isGhost());
            }
            return "false";
        }

        // Placeholder: %reviveme_target_lives%
        if (params.equalsIgnoreCase("target_lives")) {
            if (player.isOnline()) {
                Player p = player.getPlayer();
                if (p != null) {
                    // Contoh: Menampilkan nyawa pemain lain yang sedang di-target (opsional)
                }
            }
        }

        return null; // Placeholder tidak dikenal
    }
}