package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final record AddedElementRequirement(@NonNull Element element) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryDragEvent) {
            return ((InventoryDragEvent) event).getNewItems().values()
                    .stream().anyMatch(this.element::is);
        }
        return false;
    }
}