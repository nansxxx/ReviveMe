package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.ShopItem;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import com.nanskieee.phone.reviveMe.util.ShopGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopListener implements Listener {
    private final ReviveMe plugin;

    public ShopListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().toString().contains("Soul Shop")) {
            event.setCancelled(true);

            if (!(event.getWhoClicked() instanceof Player player)) return;

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

            // Navigasi
            if (clicked.getType() == Material.ARROW) {
                String title = event.getView().title().toString();
                int currentPage = 0;
                // (Logika halaman disini sederhana, di sistem nyata kita simpan di Map atau ambil dari judul)
                // Untuk sementara kita asumsikan selalu halaman 0 kecuali kita simpan state-nya.

                // (Karena kita tidak punya state halaman di ShopGUI, kita pakai metode lama)
                if (clicked.getItemMeta().displayName() != null) {
                    String displayName = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
                    // Placeholder, karena navigasi butuh state halaman yang lebih kompleks
                }
                return;
            }

            if (clicked.getType() == Material.BARRIER) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            // Proses Pembelian
            List<ShopItem> shopItems = plugin.getShopManager().getShopItems();
            for (ShopItem shopItem : shopItems) {
                if (clicked.getType() == shopItem.getItem().getType() &&
                        clicked.hasItemMeta() && clicked.getItemMeta().hasCustomModelData() &&
                        clicked.getItemMeta().getCustomModelData() == shopItem.getItem().getItemMeta().getCustomModelData()) {

                    double price = shopItem.getPrice();

                    if (player.getInventory().firstEmpty() == -1) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        MessageUtil.send(player, "inventory-full");
                        return;
                    }

                    if (plugin.getEconomyManager().hasEnough(player, price)) {
                        plugin.getEconomyManager().withdraw(player, price);
                        player.getInventory().addItem(shopItem.getItem());
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
                        MessageUtil.send(player, "paid-ritual", "<cost>", String.valueOf(price));
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        MessageUtil.send(player, "no-money", "<cost>", String.valueOf(price));
                    }
                    return;
                }
            }
        }
    }
}