package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final record ClickedCursorElementRequirement(@NonNull Element element) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent) {
            return this.element.is(((InventoryClickEvent) event).getCursor());
        }
        return false;
    }
}