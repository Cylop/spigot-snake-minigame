package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final record SlotRequirement(int slot) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            return ((InventoryClickEvent) event).getSlot() == this.slot;
        }
        return ((InventoryDragEvent) event).getInventorySlots().contains(this.slot);
    }
}