# 💀 ReviveMe Modern

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)

**ReviveMe Modern** adalah plugin *hardcore death system* yang mengubah cara pemain berinteraksi dengan kematian. Saat nyawa habis, pemain tidak mati biasa—jiwa mereka ditarik ke **Alam Baka (Death Dimension)**, dan teman-teman mereka harus melakukan **Ritual Kebangkitan** yang sinematik untuk membawa mereka kembali.

Plugin ini sangat cocok untuk server **SMP, Anarchy, Hardcore, dan RPG**.

---

## ✨ Fitur Utama

*   💀 **Sistem Nyawa & Ghost Mode**: Pemain memiliki batas nyawa. Jika habis, mereka menjadi hantu (Spectator) dan terlempar ke dimensi khusus.
*   ⚡ **Ritual Kebangkitan Sinematik**: Pemain lain harus melakukan ritual di *Soul Altar* menggunakan *Soul Head* dari mayat. Dilengkapi animasi Boss Bar, detak jantung, dan petir.
*   🌌 **Death Dimension (Alam Baka)**: Dimensi kosong melayang yang dibuat **secara otomatis** oleh plugin (tanpa butuh Multiverse). Suasananya gelap, penuh partikel jiwa, dan mencekam.
*   🛒 **Soul Shop (GUI Berhalaman)**: Toko untuk membeli *Soul Fragment*, *Revive Wand*, *Soul Armor*, dan *Soul Bound Book*. Desain GUI modern dan rapi.
*   ⚔️ **Custom Enchantments**: Enchantment **Soul Bound** (didapat dari Anvil) membuat item tidak hilang saat pemain mati.
*   🕸️ **Soul Trap (PvP)**: Perangkap jiwa untuk mencuri nyawa pemain lain yang mati di dekatnya.
*   👑 **Ritual Boss Event**: Setiap beberapa jam, Boss raksasa (*Ancient Soul Devourer*) muncul. Kalahkan untuk mendapatkan uang & Soul Coin.
*   💰 **Integrasi Vault & Soul Coin**: Ekonomi ganda. Uang biasa untuk toko, dan *Soul Coin* untuk hadiah event.
*   🤖 **Discord Webhook**: Otomatis mengirim notifikasi ke channel Discord saat Boss mati atau Ritual Sukses.
*   🌍 **Multi-Language**: Mendukung Bahasa Indonesia (`id.yml`) dan Inggris (`en.yml`).

---

## 🛠️ Persyaratan Server

*   **Minecraft**: Paper 1.21.4 (atau versi di atasnya).
*   **Java**: Minimal Java 21.
*   **Plugin Wajib**: [Vault](https://www.spigotmc.org/resources/vault.34315/) & [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).
*   **Database**: SQLite (otomatis terbuat, tanpa instalasi tambahan).

---

## 📥 Instalasi

1.  Download file `.jar` dari rilis terbaru.
2.  Letakkan di folder `plugins` server.
3.  Nyalakan server, lalu edit `plugins/ReviveMe/config.yml` sesuai selera.
4.  Restart server.

---

## 🎮 Command & Permission

| Command | Permission | Deskripsi |
| :--- | :--- | :--- |
| `/revive <player>` | - | Mencoba membangkitkan pemain hantu. |
| `/revive soulbeacon` | - | Mengirim suar jiwa (khusus pemain hantu). |
| `/reviveme givealtar` | `revivemodern.admin` | Mendapatkan item Soul Altar. |
| `/reviveme shop` | `revivemodern.admin` | Membuka GUI Soul Shop. |
| `/reviveme givesoulbound` | `revivemodern.admin` | Memberi efek Soul Bound ke item di tangan. |
| `/reviveme reload` | `revivemodern.admin` | Reload config & bahasa. |
| `/reviveme setlives <p> <n>` | `revivemodern.admin` | Mengatur nyawa pemain. |

**PlaceholderAPI**:
*   `%reviveme_lives%` (Menampilkan sisa nyawa)
*   `%reviveme_is_ghost%` (Cek apakah pemain hantu)

---

## 📸 Screenshots

*(Tambahkan screenshot-mu di sini nanti! Kamu bisa upload gambar ke GitHub, lalu paste link-nya).*
*   Screenshot Ritual Boss Bar
*   Screenshot Alam Baka (Death Dimension)
*   Screenshot Soul Shop GUI

---

## 📜 Lisensi

Plugin ini dilisensikan di bawah [MIT License](LICENSE).

---
*Dibuat dengan ❤️ oleh nanskieee*
