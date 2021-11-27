package at.malibu.general.gui.event;

import at.malibu.api.gui.ElementEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.util.Objects;

public final class ElementBasicEvent implements ElementEvent {
    private final InventoryInteractEvent baseEvent;


    public ElementBasicEvent(final InventoryInteractEvent baseEvent) {
        this.baseEvent = Objects.requireNonNull(baseEvent);
    }

    @Override
    public Player player() {
        return (Player) this.baseEvent.getWhoClicked();
    }

    @Override
    public void cancel() {
        this.baseEvent.setCancelled(true);
    }

    @Override
    public void closeView() {
        Bukkit.getScheduler().runTask(
                this.baseEvent.getHandlers().getRegisteredListeners()[0].getPlugin(),
                () -> {
                    this.baseEvent.getWhoClicked().closeInventory();
                }
        );
    }
}