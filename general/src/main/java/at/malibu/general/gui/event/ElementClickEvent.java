package at.malibu.general.gui.event;

import at.malibu.api.gui.ElementEvent;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class ElementClickEvent implements ElementEvent {
    private final InventoryClickEvent baseEvent;
    private final ElementBasicEvent baseElementEvent;

    public ElementClickEvent(final @NonNull InventoryClickEvent baseEvent) {
        this.baseEvent = baseEvent;
        this.baseElementEvent = new ElementBasicEvent(baseEvent);
    }

    public ItemStack currentItem() {
        return Objects.requireNonNull(this.baseEvent.getCurrentItem()).clone();
    }


    public int clickedX() {
        return this.baseEvent.getSlot() % 9;
    }

    public int clickedY() {
        return this.baseEvent.getSlot() / 9;
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