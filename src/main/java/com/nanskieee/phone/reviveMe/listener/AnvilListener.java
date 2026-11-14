package com.nanskieee.phone.reviveMe.listener;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.ItemUtil;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {
    private final ReviveMe plugin;

    public AnvilListener(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent event) {
        // Hanya pedulikan jika GUI yang dibuka adalah Anvil
        if (event.getInventory().getType() != InventoryType.ANVIL) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Slot 2 adalah slot hasil di Anvil
        if (event.getRawSlot() != 2) return;

        ItemStack item1 = event.getInventory().getItem(0);
        ItemStack item2 = event.getInventory().getItem(1);

        if (item1 == null || item2 == null) return;

        // Cek apakah salah satu item adalah Soul Bound Book (CMD 1007)
        boolean hasBook = isSoulBoundBook(item1) || isSoulBoundBook(item2);
        if (!hasBook) return;

        // Ambil item target (item yang bukan buku)
        ItemStack targetItem = isSoulBoundBook(item1) ? item2 : item1;

        // Cegah bug: Jika item target sudah punya Soul Bound, jangan proses
        if (ItemUtil.hasSoulBound(plugin, targetItem)) {
            MessageUtil.send(player, "soulbound-already");
            return;
        }

        // Terapkan Soul Bound ke item target
        ItemStack soulBoundedItem = ItemUtil.applySoulBound(plugin, targetItem.clone());

        // Set hasil di slot 3 (slot result Anvil)
        event.getInventory().setItem(2, soulBoundedItem);

        MessageUtil.send(player, "soulbound-applied");
    }

    private boolean isSoulBoundBook(ItemStack item) {
        return item.getType() == Material.ENCHANTED_BOOK &&
                item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == 1007;
    }
}