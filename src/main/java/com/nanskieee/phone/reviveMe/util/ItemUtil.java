package com.nanskieee.phone.reviveMe.util;

import com.nanskieee.phone.reviveMe.ReviveMe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    public static ItemStack createCustomItem(Material material, String displayName, List<String> loreLines, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            Component nameComponent = MessageUtil.parse(displayName).decoration(TextDecoration.ITALIC, false);
            meta.displayName(nameComponent);

            List<Component> loreComponents = new ArrayList<>();
            for (String line : loreLines) {
                loreComponents.add(MessageUtil.parse(line).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(loreComponents);

            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getSoulFragment() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Fragmen jiwa yang tertinggal.</gray>");
        lore.add("<gray>Gunakan di Soul Altar untuk membangkitkan.</gray>");
        return createCustomItem(Material.AMETHYST_SHARD, "<light_purple>Soul Fragment</light_purple>", lore, 1001);
    }

    public static ItemStack getSoulHead(String playerName) {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Kepala dari <white>" + playerName + "</white>.</gray>");
        lore.add("<gray>Ini adalah kunci kebangkitan.</gray>");
        return createCustomItem(Material.NETHER_STAR, "<dark_purple>Soul Head of <white>" + playerName + "</white></dark_purple>", lore, 1002);
    }

    public static ItemStack getSoulAltarItem() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Tempat untuk melakukan ritual kebangkitan.</gray>");
        lore.add("<yellow>Letakkan ini di tanah untuk membuat Altar.</yellow>");
        return createCustomItem(Material.LODESTONE, "<gold>Soul Altar</gold>", lore, 1003);
    }

    public static ItemStack getReviveWand() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Tongkat sihir kuno.</gray>");
        lore.add("<gray>Klik kanan ke arah pemain hantu untuk membangkitkannya.</gray>");
        lore.add("<red>Hanya bisa digunakan sekali!</red>");
        return createCustomItem(Material.BLAZE_ROD, "<light_purple>Revive Wand</light_purple>", lore, 1004);
    }

    public static ItemStack getSoulArmor() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Armor yang terbuat dari jiwa yang murni.</gray>");
        lore.add("<gray>Memberikan perlindungan ekstra di Alam Baka.</gray>");
        return createCustomItem(Material.NETHERITE_CHESTPLATE, "<dark_purple>Soul Armor</dark_purple>", lore, 1005);
    }

    // Item Baru: Soul Trap
    public static ItemStack getSoulTrap() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Perangkap jiwa kuno.</gray>");
        lore.add("<gray>Letakkan di tanah. Jika ada pemain mati di dekatnya,</gray>");
        lore.add("<gray>kamu akan mencuri 1 nyawa mereka!</gray>");
        return createCustomItem(Material.SOUL_LANTERN, "<dark_red>Soul Trap</dark_red>", lore, 1006);
    }

    public static boolean isSoulFragment(ItemStack item) {
        if (item == null || item.getType() != Material.AMETHYST_SHARD) return false;
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 1001;
    }

    public static ItemStack applySoulBound(ReviveMe plugin, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return item;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        NamespacedKey key = new NamespacedKey(plugin, "soulbound");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(MessageUtil.parse("<gray><italic>Soul Bound I</italic></gray>").decoration(TextDecoration.ITALIC, false));
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean hasSoulBound(ReviveMe plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(plugin, "soulbound");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}