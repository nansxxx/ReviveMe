package com.nanskieee.phone.reviveMe.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RitualSuccessEvent extends ReviveMeEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player target;

    public RitualSuccessEvent(Player reviver, Player target) {
        super(reviver);
        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}