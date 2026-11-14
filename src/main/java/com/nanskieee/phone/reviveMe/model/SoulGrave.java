package com.nanskieee.phone.reviveMe.model;

import org.bukkit.Location;
import java.util.UUID;

public class SoulGrave {
    private final UUID ownerUuid;
    private final Location location;
    private final long deathTime;

    public SoulGrave(UUID ownerUuid, Location location) {
        this.ownerUuid = ownerUuid;
        this.location = location;
        this.deathTime = System.currentTimeMillis();
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public Location getLocation() {
        return location;
    }

    public long getDeathTime() {
        return deathTime;
    }
}