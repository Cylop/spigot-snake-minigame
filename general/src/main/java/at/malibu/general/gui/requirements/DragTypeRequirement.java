package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final record DragTypeRequirement(@NonNull DragType dragType) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryDragEvent) {
            return ((InventoryDragEvent) event).getType() == this.dragType;
        }
        return false;
    }
}