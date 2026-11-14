package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import com.nanskieee.phone.reviveMe.model.PlayerData;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LifeManager {
    private final ReviveMe plugin;
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public LifeManager(ReviveMe plugin) {
        this.plugin = plugin;
        loadAllData();
    }

    public PlayerData getData(Player player) {
        UUID uuid = player.getUniqueId();
        if (!dataMap.containsKey(uuid)) {
            int lives = getLivesFromDb(uuid);
            dataMap.put(uuid, new PlayerData(uuid, lives));
        }
        return dataMap.get(uuid);
    }

    // --- FITUR NYAWA ---

    public void decrementLife(Player player) {
        PlayerData data = getData(player);
        if (data.getLives() > 0) {
            data.setLives(data.getLives() - 1);
            saveToDb(data);
        }
    }

    public void addLife(Player player, int amount) {
        PlayerData data = getData(player);
        data.setLives(data.getLives() + amount);
        saveToDb(data);
    }

    public void setLife(Player player, int amount) {
        PlayerData data = getData(player);
        data.setLives(amount);
        saveToDb(data);
    }

    // --- FITUR SOUL COIN ---

    public void addSoulCoins(Player player, int amount) {
        String sql = "UPDATE player_data SET soul_coins = soul_coins + ? WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.getDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setString(2, player.getUniqueId().toString());
            int rows = stmt.executeUpdate();

            // Jika pemain belum ada di database, buat baris baru
            if (rows == 0) {
                String insert = "INSERT INTO player_data(uuid, lives, soul_coins) VALUES(?, ?, ?)";
                try (PreparedStatement insertStmt = plugin.getDatabaseManager().getConnection().prepareStatement(insert)) {
                    insertStmt.setString(1, player.getUniqueId().toString());
                    insertStmt.setInt(2, plugin.getConfigManager().getDefaultLives());
                    insertStmt.setInt(3, amount);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSoulCoins(Player player) {
        String sql = "SELECT soul_coins FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.getDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("soul_coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- DATABASE HELPER ---

    private int getLivesFromDb(UUID uuid) {
        String sql = "SELECT lives FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = plugin.getDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("lives");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plugin.getConfigManager().getDefaultLives();
    }

    public void saveToDb(PlayerData data) {
        String sql = "INSERT INTO player_data(uuid, lives) VALUES(?, ?) ON CONFLICT(uuid) DO UPDATE SET lives = ?";
        try (PreparedStatement stmt = plugin.getDatabaseManager().getConnection().prepareStatement(sql)) {
            stmt.setString(1, data.getUuid().toString());
            stmt.setInt(2, data.getLives());
            stmt.setInt(3, data.getLives());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAllData() {
        // (Opsional) Memuat semua data ke RAM saat startup
    }

    public void saveAll() {
        // SQLite otomatis menyimpan, jadi ini bisa dibiarkan kosong
    }
}