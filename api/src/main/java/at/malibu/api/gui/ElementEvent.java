package at.malibu.api.gui;

import org.bukkit.entity.Player;

public interface ElementEvent {
    
    Player player();

    void cancel();

    void closeView();
}