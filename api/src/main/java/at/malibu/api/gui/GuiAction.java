package at.malibu.api.gui;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;

@FunctionalInterface
public interface GuiAction {
    void handle(@NonNull InventoryInteractEvent event);
}