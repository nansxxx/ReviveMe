package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final ReviveMe plugin;
    private Connection connection;

    public DatabaseManager(ReviveMe plugin) {
        this.plugin = plugin;
        connect();
        createTable();
    }

    private void connect() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();

            File dbFile = new File(dataFolder, "data.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            connection = DriverManager.getConnection(url);
            plugin.getLogger().info("Berhasil terhubung ke database SQLite.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Gagal terhubung ke database!");
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "lives INTEGER NOT NULL" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("Gagal membuat tabel!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}