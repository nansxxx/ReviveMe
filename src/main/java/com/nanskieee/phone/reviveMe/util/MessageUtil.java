package com.nanskieee.phone.reviveMe.util;

import com.nanskieee.phone.reviveMe.ReviveMe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static FileConfiguration langConfig;
    private static String prefix;

    public static void setup(ReviveMe plugin) {
        // Mengambil bahasa dari config.yml di bagian general.language
        String lang = plugin.getConfig().getString("general.language", "en");
        String fileName = lang + ".yml";

        // Tentukan folder tujuan di server (plugins/ReviveMe/lang/)
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs(); // Buat folder lang jika belum ada
        }

        // Tentukan file tujuan
        File file = new File(langFolder, fileName);

        // Jika file belum ada di server, ekstrak dari dalam .jar
        if (!file.exists()) {
            try {
                InputStream in = plugin.getResource("lang/" + fileName);
                if (in != null) {
                    java.nio.file.Files.copy(in, file.toPath());
                } else {
                    plugin.getLogger().severe("File bahasa " + fileName + " tidak ditemukan di dalam .jar!");
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Gagal mengekstrak file bahasa: " + e.getMessage());
            }
        }

        // Load konfigurasi bahasa
        langConfig = YamlConfiguration.loadConfiguration(file);

        // Fallback ke default jika file kosong/corrupt
        InputStream defaultStream = plugin.getResource("lang/" + fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            langConfig.setDefaults(defaultConfig);
        }

        prefix = langConfig.getString("prefix", "<gray>[<red>ReviveMe<gray>]</gray> ");
    }

    public static Component parse(String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    public static void send(CommandSender sender, String key, String... placeholders) {
        if (langConfig == null) return;

        String message = langConfig.getString(key, "<red>Missing translation: " + key + "</red>");

        // Ganti placeholder (misal: <cost> jadi 5000)
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                message = message.replace(placeholders[i], placeholders[i + 1]);
            }
        }

        sender.sendMessage(parse(prefix + message));
    }
}