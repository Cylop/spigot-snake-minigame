package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public record OrRequirement(@NonNull Requirement... reqs) implements Requirement {

    @Override
    public boolean control(final @NotNull InventoryInteractEvent event) {
        for (final Requirement req : this.reqs) {
            if (req.control(event)) {
                return true;
            }
        }
        return false;
    }
}