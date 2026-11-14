package com.nanskieee.phone.reviveMe.config;

import com.nanskieee.phone.reviveMe.ReviveMe;

public class ConfigManager {
    private final ReviveMe plugin;

    public ConfigManager(ReviveMe plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public String getLanguage() {
        return plugin.getConfig().getString("general.language", "en");
    }

    public int getDefaultLives() {
        return plugin.getConfig().getInt("general.default-lives", 3);
    }

    public int getRitualDuration() {
        return plugin.getConfig().getInt("ritual.duration-seconds", 10);
    }

    public boolean isSacrificeEnabled() {
        return plugin.getConfig().getBoolean("ritual.sacrifice-enabled", true);
    }

    public boolean isFailOnMove() {
        return plugin.getConfig().getBoolean("ritual.fail-on-move", true);
    }

    public boolean isFailOnDamage() {
        return plugin.getConfig().getBoolean("ritual.fail-on-damage", true);
    }

    public int getRitualCooldown() {
        return plugin.getConfig().getInt("ritual.cooldown-seconds", 60);
    }

    public boolean isEconomyEnabled() {
        return plugin.getConfig().getBoolean("economy.enabled", true);
    }

    public double getRitualCost() {
        return plugin.getConfig().getDouble("economy.ritual-cost", 5000.0);
    }

    public boolean isDeathDimensionEnabled() {
        return plugin.getConfig().getBoolean("death-dimension.enabled", true);
    }

    public String getDeathDimension() {
        return plugin.getConfig().getString("death-dimension.world-name", "death_realm");
    }

    public long getDeathDimensionTime() {
        return plugin.getConfig().getLong("death-dimension.time", 18000);
    }

    public boolean isDeathWeatherDisabled() {
        return plugin.getConfig().getBoolean("death-dimension.disable-weather", true);
    }

    public boolean isGhostAuraEnabled() {
        return plugin.getConfig().getBoolean("visuals.ghost-aura-enabled", true);
    }

    public String getGhostParticles() {
        return plugin.getConfig().getString("visuals.ghost-particles", "CLOUD, END_ROD");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }
    public double getSoulFragmentPrice() {
        return plugin.getConfig().getDouble("shop.soul-fragment-price", 1000.0);
    }
    public double getSoulAltarPrice() {
        return plugin.getConfig().getDouble("shop.soul-altar-price", 50000.0);
    }
    public double getReviveWandPrice() {
        return plugin.getConfig().getDouble("shop.revive-wand-price", 15000.0);
    }
    public double getSoulArmorPrice() {
        return plugin.getConfig().getDouble("shop.soul-armor-price", 25000.0);
    }
    public boolean isBossEventEnabled() {
        return plugin.getConfig().getBoolean("boss-event.enabled", true);
    }
    public int getBossIntervalHours() {
        return plugin.getConfig().getInt("boss-event.interval-hours", 3);
    }
    public String getBossName() {
        return plugin.getConfig().getString("boss-event.boss-name", "<dark_red>Ancient Soul Devourer</dark_red>");
    }
    public double getBossRewardMoney() {
        return plugin.getConfig().getDouble("boss-event.reward-money", 10000.0);
    }
    public boolean isDiscordEnabled() {
        return plugin.getConfig().getBoolean("discord.enabled", false);
    }
    public String getDiscordWebhookUrl() {
        return plugin.getConfig().getString("discord.webhook-url", "");
    }
    public boolean isNotifyRevive() {
        return plugin.getConfig().getBoolean("discord.notify-revive", true);
    }
    public boolean isNotifyBoss() {
        return plugin.getConfig().getBoolean("discord.notify-boss", true);
    }
    public int getSoulCoinPerRevive() {
        return plugin.getConfig().getInt("soul-currency.reward-per-revive", 5);
    }
    public int getSoulCoinPerBoss() {
        return plugin.getConfig().getInt("soul-currency.reward-per-boss", 50);
    }
}