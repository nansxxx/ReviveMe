package com.nanskieee.phone.reviveMe.model;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private int lives;
    private boolean isGhost;

    public PlayerData(UUID uuid, int lives) {
        this.uuid = uuid;
        this.lives = lives;
        this.isGhost = lives <= 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        // Jika nyawa habis, otomatis jadi hantu
        this.isGhost = lives <= 0;
    }

    public boolean isGhost() {
        return isGhost;
    }

    public void setGhost(boolean ghost) {
        isGhost = ghost;
    }
}