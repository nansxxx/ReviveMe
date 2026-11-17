<div align="center">
  
# 💀 ReviveMe Modern

**Don't just respawn. Fight for your soul.**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-Available-blue.svg)](#)

A next-generation hardcore death system featuring cinematic resurrection rituals, custom death dimensions, and deep RPG mechanics. Built for SMP, Anarchy, and Hardcore servers.

</div>

---

## 📖 Overview

**ReviveMe Modern** completely overhauls the vanilla death system. Players no longer simply respawn. Instead, they have a limited number of lives. When their lives run out, their soul is cast into the **Death Realm**—a custom, eerie void dimension. To bring them back, friends must gather at a **Soul Altar** and perform a high-stakes, cinematic resurrection ritual.

---

## ✨ Features

*   **💀 Hardcore Life System:** Players have limited lives. When they run out, they become ghosts.
*   **🌌 The Death Realm:** A fully automated custom void dimension with eerie visual effects (No Multiverse required).
*   **⚡ Cinematic Rituals:** Revive players using a Soul Altar and Soul Head. Features Boss Bars, soul particles, and fail conditions (moving/taking damage).
*   **🛒 Soul Shop GUI:** A fully functional, paginated GUI shop for buying Soul Fragments, Wands, Armor, and Tools using Vault economy.
*   **⚔️ Soul Bound Enchantment:** A custom Anvil mechanic to prevent items from dropping on death.
*   **🕸️ Soul Trap (PvP):** Place a trap to steal the lives of players who die nearby.
*   **👑 Global Boss Events:** Periodic bosses that reward all nearby players with money and Soul Coins.
*   **🌍 Multi-Language:** Fully translatable with default English (`en`) and Indonesian (`id`) files.
*   **🔗 Integrations:** Vault, PlaceholderAPI, WorldGuard, and Discord Webhooks (Rich Embeds).
*   **💾 Database Support:** Choose between SQLite (local) or MySQL (cross-server).

---

## 🛠️ Tech Stack & Requirements

*   **Server Software:** Paper 1.21.4+ (or above)
*   **Java:** Java 21+
*   **Dependencies:** 
    *   [Vault](https://www.spigotmc.org/resources/vault.34315/) (Required for Economy)
    *   [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (Optional, for placeholders)
    *   [WorldGuard](https://www.spigotmc.org/resources/worldguard.1177/) (Optional, for region protection)

---

## 🎮 Commands & Permissions

### Player Commands
| Command | Description |
| :--- | :--- |
| `/revive <player>` | Try to revive a ghost player. |
| `/revive soulbeacon` | Send a beacon of light to the living world (Ghosts only). |

### Admin Commands `(Permission: revivemodern.admin)`
| Command | Description |
| :--- | :--- |
| `/rm givealtar` | Get a Soul Altar item. |
| `/rm shop` | Open the player Soul Shop. |
| `/rm adminshop` | Open the Admin Shop GUI to add items. |
| `/rm givesoulbound` | Apply Soul Bound to the held item. |
| `/rm revive <player>` | Forcefully revive a ghost player. |
| `/rm setlives <player> <amount>` | Set a player's lives. |
| `/rm pay <player> <amount>` | Transfer Soul Coins to another player. |
| `/rm top` | View the Top 10 Soul Coin leaderboard. |
| `/rm reload` | Reload config and languages. |

### Placeholders (PlaceholderAPI)
*   `%reviveme_lives%` - Returns the player's remaining lives.
*   `%reviveme_soulcoins%` - Returns the player's Soul Coin balance.
*   `%reviveme_is_ghost%` - Returns "Ghost" or "Alive".
*   `%reviveme_top_1_name%` - Name of the richest player.
*   `%reviveme_top_1_coins%` - Coins of the richest player.

---

## 📥 Installation

1. Download the latest `.jar` from the [Releases](https://github.com/nansxxx/ReviveMe/releases) page.
2. Drop the `.jar` file into your server's `plugins` folder.
3. Ensure you have **Vault** and an Economy plugin installed.
4. Start the server. (The Death Realm dimension will be generated automatically!)
5. Configure your settings in `plugins/ReviveMe/config.yml` and language files in `plugins/ReviveMe/lang/`.

---

## 📸 Screenshots

*(Add your screenshots here! Upload images to GitHub and paste the links below)*
*   [Soul Altar Ritual]()
*   [Death Realm & Particles]()
*   [Soul Shop GUI]()

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---
<div align="center">
  <i>Developed with ❤️ by <a href="https://github.com/nansxxx">Nansxxx</a></i>
</div>
