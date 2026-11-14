package com.nanskieee.phone.reviveMe.manager;

import com.nanskieee.phone.reviveMe.ReviveMe;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordManager {
    private final ReviveMe plugin;

    public DiscordManager(ReviveMe plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String message) {
        if (!plugin.getConfigManager().isDiscordEnabled() || plugin.getConfigManager().getDiscordWebhookUrl().isEmpty()) {
            return;
        }

        // Kirim secara async agar tidak lag server
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(plugin.getConfigManager().getDiscordWebhookUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "ReviveMe-Plugin");
                connection.setDoOutput(true);

                // Format JSON untuk Discord
                String jsonPayload = "{\"content\": \"" + message + "\"}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != 204 && responseCode != 200) {
                    plugin.getLogger().warning("Gagal mengirim pesan ke Discord. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Error saat mengirim webhook Discord: " + e.getMessage());
            }
        });
    }
}