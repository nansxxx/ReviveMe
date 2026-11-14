package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    private final ReviveMe plugin;
    private Economy economy;

    public EconomyManager(ReviveMe plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault tidak ditemukan! Fitur ekonomi dimatikan.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
    }

    public boolean hasEconomy() {
        return economy != null;
    }

    public boolean hasEnough(Player player, double amount) {
        if (!hasEconomy()) return true; // Jika tidak ada Vault, anggap gratis
        return economy.has(player, amount);
    }

    public void withdraw(Player player, double amount) {
        if (hasEconomy()) {
            economy.withdrawPlayer(player, amount);
        }
    }

    // Method Baru untuk Hadiah Boss / Event
    public void deposit(Player player, double amount) {
        if (hasEconomy()) {
            economy.depositPlayer(player, amount);
        }
    }
}