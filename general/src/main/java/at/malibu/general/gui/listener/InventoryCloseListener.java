package at.malibu.general.gui.listener;

import at.malibu.api.gui.Page;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class InventoryCloseListener implements Listener {
    /**
     * the listener that listens for inventory closes and informs the pages associated with them.
     *
     * @param event the event that happened
     */
    @EventHandler
    public void closeListener(final InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Page) {
            ((Page) event.getInventory().getHolder()).handleClose(event);
        }
    }
}