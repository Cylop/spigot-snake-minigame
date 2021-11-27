package at.malibu.general.gui.listener;

import at.malibu.api.gui.Page;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public final class InventoryClickListener implements Listener {

    @EventHandler
    public void listener(final InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Page &&
                !(event.getClickedInventory() instanceof PlayerInventory)) {
            ((Page) event.getInventory().getHolder()).accept(event);
        }
    }
}