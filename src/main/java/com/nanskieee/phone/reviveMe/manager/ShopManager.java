package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.ShopItem;
import com.nanskieee.phone.reviveMe.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private final ReviveMe plugin;
    private final List<ShopItem> shopItems = new ArrayList<>();

    public ShopManager(ReviveMe plugin) {
        this.plugin = plugin;
        loadItemsFromConfig(); // Baca dari config.yml
    }

    public void loadItemsFromConfig() {
        shopItems.clear();
        ConfigurationSection shopConfig = plugin.getConfig().getConfigurationSection("shop.items");

        if (shopConfig == null) {
            plugin.getLogger().warning("Bagian 'shop.items' tidak ditemukan di config.yml!");
            return;
        }

        for (String key : shopConfig.getKeys(false)) {
            String path = "shop.items." + key;
            try {
                Material material = Material.valueOf(plugin.getConfig().getString(path + ".material", "STONE").toUpperCase());
                double price = plugin.getConfig().getDouble(path + ".price", 1000.0);
                int cmd = plugin.getConfig().getInt(path + ".custom-model-data", 0);

                // Buat item berdasarkan config
                List<String> lore = new ArrayList<>();
                lore.add("<gray>Item Toko</gray>");

                // Jika ada custom model data, buat item custom
                ItemStack item = ItemUtil.createCustomItem(material, "<yellow>" + key + "</yellow>", lore, cmd);

                shopItems.add(new ShopItem(item, price));

            } catch (Exception e) {
                plugin.getLogger().warning("Gagal memuat item toko: " + key);
            }
        }
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }
}