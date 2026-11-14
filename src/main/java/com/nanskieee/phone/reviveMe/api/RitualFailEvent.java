package com.nanskieee.phone.reviveMe.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RitualFailEvent extends ReviveMeEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String reason;

    public RitualFailEvent(Player reviver, String reason) {
        super(reviver);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}