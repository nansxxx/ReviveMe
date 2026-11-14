package com.nanskieee.phone.reviveMe.util;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.ShopItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ShopGUI {

    public static void openShop(ReviveMe plugin, Player player, int page) {
        List<ShopItem> shopItems = plugin.getShopManager().getShopItems();

        // Ukuran 36 (4 baris) agar muat border + item + navigasi
        Inventory inv = Bukkit.createInventory(null, 36, MessageUtil.parse("<dark_gray>Soul Shop</dark_gray>"));

        // Slot untuk item (Baris 2 dan 3)
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

        int startIndex = page * slots.length;
        int endIndex = Math.min(startIndex + slots.length, shopItems.size());

        int itemIndex = 0;
        for (int i = startIndex; i < endIndex; i++) {
            ShopItem shopItem = shopItems.get(i);
            ItemStack displayItem = shopItem.getItem().clone();
            ItemMeta meta = displayItem.getItemMeta();

            meta.lore(Arrays.asList(
                    MessageUtil.parse("<dark_gray>---------------------"),
                    MessageUtil.parse("<gray>Price: <gold>$" + String.format("%.2f", shopItem.getPrice()) + "</gold>"),
                    MessageUtil.parse("<dark_gray>---------------------"),
                    MessageUtil.parse("<yellow>▶ Click to purchase")
            ));
            displayItem.setItemMeta(meta);

            inv.setItem(slots[itemIndex], displayItem);
            itemIndex++;
        }

        // Navigasi Previous Page (Panah Kiri)
        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();
            prevMeta.displayName(MessageUtil.parse("<yellow>« Previous Page</yellow>"));
            prev.setItemMeta(prevMeta);
            inv.setItem(30, prev);
        }

        // Navigasi Next Page (Panah Kanan)
        if (endIndex < shopItems.size()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.displayName(MessageUtil.parse("<yellow>Next Page »</yellow>"));
            next.setItemMeta(nextMeta);
            inv.setItem(32, next);
        }

        // Tombol Close (Barrier) di tengah bawah
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(MessageUtil.parse("<red>Close</red>"));
        close.setItemMeta(closeMeta);
        inv.setItem(31, close);

        // Border Kaca Hitam (Mengisi semua sisa slot)
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.displayName(Component.empty());
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }

        player.openInventory(inv);
    }
}