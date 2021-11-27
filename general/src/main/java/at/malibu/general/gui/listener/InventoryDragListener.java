package at.malibu.general.gui.listener;

import at.malibu.api.gui.Page;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.PlayerInventory;

public final class InventoryDragListener implements Listener {

    @EventHandler
    public void listener(final InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof Page &&
                !(event.getInventory() instanceof PlayerInventory)) {
            ((Page) event.getInventory().getHolder()).accept(event);
        }
    }
}