package at.malibu.general.gui.event;

import at.malibu.api.gui.ElementEvent;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class ElementDragEvent implements ElementEvent {
    private final InventoryDragEvent baseEvent;
    private final ElementBasicEvent baseElementEvent;

    public ElementDragEvent(final @NonNull InventoryDragEvent baseEvent) {
        this.baseEvent = baseEvent;
        this.baseElementEvent = new ElementBasicEvent(baseEvent);
    }

    public ItemStack oldCursor() {
        return this.baseEvent.getOldCursor();
    }

    public Map<Integer, ItemStack> items() {
        return this.baseEvent.getNewItems();
    }

    @Override
    public Player player() {
        return this.baseElementEvent.player();
    }

    @Override
    public void cancel() {
        this.baseElementEvent.cancel();
    }

    @Override
    public void closeView() {
        this.baseElementEvent.closeView();
    }
}