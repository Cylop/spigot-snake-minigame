package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final record DragRequirement() implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        return event instanceof InventoryDragEvent;
    }
}