package at.malibu.api.gui;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;

public interface Requirement {
    boolean control(@NonNull InventoryInteractEvent event);
}