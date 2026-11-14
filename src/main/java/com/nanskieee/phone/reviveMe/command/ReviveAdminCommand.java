package com.nanskieee.phone.reviveMe.command;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.ItemUtil;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import com.nanskieee.phone.reviveMe.util.ShopGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviveAdminCommand implements CommandExecutor, TabCompleter {
    private final ReviveMe plugin;

    public ReviveAdminCommand(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            MessageUtil.send(sender, "admin-usage");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "givealtar" -> {
                if (sender instanceof Player player) {
                    plugin.getAltarManager().giveAltarItem(player);
                    MessageUtil.send(player, "admin-altar-given");
                } else {
                    MessageUtil.send(sender, "admin-only-player");
                }
            }
            case "shop" -> {
                if (sender instanceof Player player) {
                    ShopGUI.openShop(plugin, player, 0);
                } else {
                    MessageUtil.send(sender, "admin-only-player");
                }
            }
            case "givesoulbound" -> {
                if (sender instanceof Player player) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.AIR) {
                        MessageUtil.send(player, "admin-usage");
                        return true;
                    }
                    player.getInventory().setItemInMainHand(ItemUtil.applySoulBound(plugin, item));
                    MessageUtil.send(player, "admin-altar-given");
                } else {
                    MessageUtil.send(sender, "admin-only-player");
                }
            }
            case "reload" -> {
                plugin.getConfigManager().reloadConfig();
                MessageUtil.setup(plugin);
                MessageUtil.send(sender, "admin-reloaded");
                plugin.getShopManager().loadItemsFromConfig();
            }
            case "setlives" -> {
                if (args.length < 3) {
                    MessageUtil.send(sender, "admin-setlives-usage");
                    return true;
                }

                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    MessageUtil.send(sender, "admin-player-not-found");
                    return true;
                }

                try {
                    int amount = Integer.parseInt(args[2]);
                    plugin.getLifeManager().setLife(target, amount);

                    MessageUtil.send(sender, "admin-setlives-success", "<player>", target.getName(), "<amount>", String.valueOf(amount));
                    if (!sender.equals(target)) {
                        MessageUtil.send(target, "admin-setlives-success", "<player>", target.getName(), "<amount>", String.valueOf(amount));
                    }
                } catch (NumberFormatException e) {
                    MessageUtil.send(sender, "admin-invalid-number");
                }
            }
            default -> MessageUtil.send(sender, "admin-unknown-subcommand");
        }
        return true;
    }

    // --- FITUR TAB COMPLETE ---
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("givealtar", "shop", "givesoulbound", "reload", "setlives");
            for (String sub : subCommands) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setlives")) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(p.getName());
                }
            }
            return completions;
        }

        return completions;
    }
}