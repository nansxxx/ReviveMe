package com.nanskieee.phone.reviveMe;

import com.nanskieee.phone.reviveMe.command.ReviveAdminCommand;
import com.nanskieee.phone.reviveMe.command.ReviveCommand;
import com.nanskieee.phone.reviveMe.config.ConfigManager;
import com.nanskieee.phone.reviveMe.listener.*;
import com.nanskieee.phone.reviveMe.manager.*;
import com.nanskieee.phone.reviveMe.util.MessageUtil;
import com.nanskieee.phone.reviveMe.util.ReviveMeExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReviveMe extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private LifeManager lifeManager;
    private GhostManager ghostManager;
    private GraveManager graveManager;
    private AltarManager altarManager;
    private RitualManager ritualManager;
    private EconomyManager economyManager;
    private ShopManager shopManager;
    private BossManager bossManager;
    private DiscordManager discordManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this);
        MessageUtil.setup(this);

        this.lifeManager = new LifeManager(this);
        this.ghostManager = new GhostManager(this);
        this.graveManager = new GraveManager(this);
        this.altarManager = new AltarManager(this);
        this.ritualManager = new RitualManager(this);
        this.economyManager = new EconomyManager(this);
        this.shopManager = new ShopManager(this);
        this.bossManager = new BossManager(this);
        this.discordManager = new DiscordManager(this);

        setupDeathWorld();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new AltarInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new RitualInterruptListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new ShopListener(this), this);
        getServer().getPluginManager().registerEvents(new SecurityListener(this), this);
        getServer().getPluginManager().registerEvents(new SoulTrapListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);

        getCommand("revive").setExecutor(new ReviveCommand(this));

        ReviveAdminCommand adminCmd = new ReviveAdminCommand(this);
        getCommand("reviveme").setExecutor(adminCmd);
        getCommand("reviveme").setTabCompleter(adminCmd);

        if (configManager.isGhostAuraEnabled()) {
            getServer().getScheduler().runTaskTimer(this, () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (lifeManager.getData(p).isGhost()) {
                        String[] particles = configManager.getGhostParticles().split(",");
                        for (String part : particles) {
                            try {
                                Particle particle = Particle.valueOf(part.trim().toUpperCase());
                                p.getWorld().spawnParticle(particle, p.getLocation().add(0, 1, 0), 5, 0.3, 0.5, 0.3, 0.02);
                            } catch (IllegalArgumentException ignored) {}
                        }
                    }
                }
            }, 0L, 20L);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ReviveMeExpansion(this).register();
        }

        getLogger().info("ReviveMe berhasil dinyalakan!");
    }

    private void setupDeathWorld() {
        if (!configManager.isDeathDimensionEnabled()) return;
        String dimName = configManager.getDeathDimension();
        World deathWorld = Bukkit.getWorld(dimName);

        if (deathWorld == null) {
            WorldCreator creator = new WorldCreator(dimName)
                    .generator(new org.bukkit.generator.ChunkGenerator() {
                        @Override
                        public org.bukkit.generator.ChunkGenerator.ChunkData generateChunkData(World world, java.util.Random random, int chunkX, int chunkZ, org.bukkit.generator.ChunkGenerator.BiomeGrid biome) {
                            return createChunkData(world);
                        }
                    });
            deathWorld = creator.createWorld();
            if (deathWorld != null) {
                deathWorld.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
                deathWorld.setTime(configManager.getDeathDimensionTime());
                if (configManager.isDeathWeatherDisabled()) deathWorld.setStorm(false);
                deathWorld.setSpawnLocation(0, 65, 0);
                for (int x = -5; x <= 5; x++) {
                    for (int z = -5; z <= 5; z++) {
                        deathWorld.getBlockAt(x, 64, z).setType(org.bukkit.Material.BARRIER);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (lifeManager != null) lifeManager.saveAll();
        if (ritualManager != null) ritualManager.cancelAllRituals();
        if (databaseManager != null) databaseManager.close();
    }

    public ConfigManager getConfigManager() { return configManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public LifeManager getLifeManager() { return lifeManager; }
    public GhostManager getGhostManager() { return ghostManager; }
    public GraveManager getGraveManager() { return graveManager; }
    public AltarManager getAltarManager() { return altarManager; }
    public RitualManager getRitualManager() { return ritualManager; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public ShopManager getShopManager() { return shopManager; }
    public BossManager getBossManager() { return bossManager; }
    public DiscordManager getDiscordManager() { return discordManager; }
}