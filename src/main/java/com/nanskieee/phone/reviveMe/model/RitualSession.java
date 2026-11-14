package com.nanskieee.phone.reviveMe.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RitualSession {
    private final Player reviver;
    private final Player target;
    private final Location altarLocation;
    private final long startTime;
    private int taskId;

    public RitualSession(Player reviver, Player target, Location altarLocation) {
        this.reviver = reviver;
        this.target = target;
        this.altarLocation = altarLocation;
        this.startTime = System.currentTimeMillis();
        this.taskId = -1;
    }

    public Player getReviver() {
        return reviver;
    }

    public Player getTarget() {
        return target;
    }

    public Location getAltarLocation() {
        return altarLocation;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}