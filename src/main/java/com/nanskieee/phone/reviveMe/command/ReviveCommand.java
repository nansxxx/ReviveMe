package com.nanskieee.phone.reviveMe.command;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReviveCommand implements CommandExecutor {
    private final ReviveMe plugin;

    public ReviveCommand(ReviveMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        // Fitur Soul Beacon (Sub-command /revive soulbeacon)
        if (args.length > 0 && args[0].equalsIgnoreCase("soulbeacon")) {
            if (plugin.getLifeManager().getData(player).isGhost()) {
                // Tembakkan beacon di lokasi terakhir pemain mati (bisa diambil dari GraveManager nanti)
                // Untuk simplifikasi, kita taruh di Spawn World Asli
                Location beaconLoc = plugin.getServer().getWorlds().get(0).getSpawnLocation();

                // Efek Tiang Cahaya
                for (int y = 0; y < 50; y++) {
                    beaconLoc.getWorld().spawnParticle(Particle.END_ROD, beaconLoc.clone().add(0, y, 0), 5, 0.1, 0.1, 0.1, 0.01);
                }

                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                MessageUtil.send(player, "beacon-activated");
            } else {
                MessageUtil.send(player, "not-ghost");
            }
            return true;
        }

        // Logika /revive biasa (untuk target)
        if (plugin.getLifeManager().getData(player).isGhost()) {
            MessageUtil.send(player, "ghost-cant-command");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.send(player, "usage-revive");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            MessageUtil.send(player, "player-not-found");
            return true;
        }

        if (!plugin.getLifeManager().getData(target).isGhost()) {
            MessageUtil.send(player, "target-not-ghost");
            return true;
        }

        MessageUtil.send(player, "need-altar", "<target>", target.getName());

        return true;
    }
}