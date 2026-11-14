package com.nanskieee.phone.reviveMe.ritual;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.api.RitualSuccessEvent;
import com.nanskieee.phone.reviveMe.model.RitualSession;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RitualTask extends BukkitRunnable {
    private final ReviveMe plugin;
    private final RitualSession session;
    private final BossBar bossBar;

    private int timeLeft;
    private final Location startLocation;

    public RitualTask(ReviveMe plugin, RitualSession session) {
        this.plugin = plugin;
        this.session = session;
        this.timeLeft = plugin.getConfigManager().getRitualDuration();
        this.startLocation = session.getReviver().getLocation();

        // --- DESAIN BOSS BAR EPIK ---
        // Warna Merah Darah, Teks Bergradasi Emas, Overlay Bergerigi (Notched 10)
        this.bossBar = BossBar.bossBar(
                MessageUtil.parse("<gradient:#FF0000:#FFD700>⚡ RITUAL OF RESURRECTION: <white>" + session.getTarget().getName() + "</white> ⚡</gradient>"),
                1.0f,
                BossBar.Color.RED,
                BossBar.Overlay.NOTCHED_10
        );
    }

    @Override
    public void run() {
        Player reviver = session.getReviver();
        Player target = session.getTarget();

        if (!reviver.isOnline() || !target.isOnline()) {
            fail(RitualResult.FAIL_MOVE, "Player offline");
            return;
        }

        if (plugin.getConfigManager().isFailOnMove()) {
            if (reviver.getLocation().distanceSquared(startLocation) > 0.5) {
                fail(RitualResult.FAIL_MOVE, "Reviver moved!");
                return;
            }
        }

        if (timeLeft <= 0) {
            success();
            return;
        }

        // --- UPDATE BOSS BAR SAAT HITUNG MUNDUR ---
        float progress = (float) timeLeft / plugin.getConfigManager().getRitualDuration();
        bossBar.progress(progress);
        bossBar.name(MessageUtil.parse("<gradient:#FF0000:#FFD700>⚡ RITUAL: <white>" + timeLeft + "s</white> ⚡</gradient>"));

        // Suara detak jantung yang lebih menegangkan
        reviver.playSound(reviver.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 0.5f, 1.0f);
        reviver.playSound(reviver.getLocation(), Sound.BLOCK_SOUL_SOIL_BREAK, 0.3f, 0.5f);

        // Partikel di sekitar altar
        Location altarLoc = session.getAltarLocation().add(0.5, 1.0, 0.5);
        altarLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, altarLoc, 10, 0.5, 0.5, 0.5, 0.05);
        altarLoc.getWorld().spawnParticle(Particle.SOUL, altarLoc, 5, 0.3, 0.3, 0.3, 0.02);

        timeLeft--;
    }

    private void success() {
        Player reviver = session.getReviver();
        Player target = session.getTarget();
        Location altarLoc = session.getAltarLocation();

        target.teleport(altarLoc.clone().add(0, 1, 0));
        plugin.getGhostManager().unsetGhost(target);

        RitualSuccessEvent successEvent = new RitualSuccessEvent(reviver, target);
        Bukkit.getPluginManager().callEvent(successEvent);

        // Efek sukses yang lebih MEGAH
        altarLoc.getWorld().strikeLightningEffect(altarLoc);
        altarLoc.getWorld().playSound(altarLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        altarLoc.getWorld().playSound(altarLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        altarLoc.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, altarLoc, 100, 1.0, 1.0, 1.0, 0.5);

        // --- SOUL COIN & DISCORD ---
        int soulCoins = plugin.getConfigManager().getSoulCoinPerRevive();
        plugin.getLifeManager().addSoulCoins(reviver, soulCoins);
        MessageUtil.send(reviver, "soul-coin-earned", "<amount>", String.valueOf(soulCoins));

        if (plugin.getConfigManager().isNotifyRevive()) {
            plugin.getDiscordManager().sendMessage("✨ **" + reviver.getName() + "** baru saja membangkitkan **" + target.getName() + "**!");
        }
        // ----------------------------

        MessageUtil.send(reviver, "ritual-success", "<target>", target.getName());
        MessageUtil.send(target, "ritual-success", "<target>", target.getName());

        bossBar.removeViewer(reviver);
        plugin.getRitualManager().removeSession(reviver.getUniqueId());
        this.cancel();
    }

    public void fail(RitualResult result, String reason) {
        Player reviver = session.getReviver();

        Location altarLoc = session.getAltarLocation();
        altarLoc.getWorld().playSound(altarLoc, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        altarLoc.getWorld().playSound(altarLoc, Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.5f);
        altarLoc.getWorld().spawnParticle(Particle.SMOKE, altarLoc, 20, 0.5, 1.0, 0.5, 0.05);

        plugin.getRitualManager().failRitual(reviver.getUniqueId(), reason);

        this.cancel();
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}