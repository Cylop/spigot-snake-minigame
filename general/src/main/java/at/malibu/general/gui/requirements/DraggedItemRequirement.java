package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final record DraggedItemRequirement(@NonNull ItemStack item) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            return false;
        }
        return ((InventoryDragEvent) event).getOldCursor().equals(this.item);
    }
}