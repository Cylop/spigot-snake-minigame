package at.malibu.general.gui.requirements;

import at.malibu.api.gui.Requirement;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

public final record PlayerRequirement(@NonNull Player player) implements Requirement {

    @Override
    public boolean control(final @NonNull InventoryInteractEvent event) {
        return event.getWhoClicked().getUniqueId().equals(this.player.getUniqueId());
    }
}