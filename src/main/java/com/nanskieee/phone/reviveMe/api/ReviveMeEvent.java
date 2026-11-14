package com.nanskieee.phone.reviveMe.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class ReviveMeEvent extends Event {
    private final Player player;

    public ReviveMeEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}