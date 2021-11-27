package at.malibu.general.gui.element;

import at.malibu.api.gui.Element;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class ThreadSafeElement implements Element {
    private final Element baseElement;

    public ThreadSafeElement(final @NonNull Element baseElement) {
        this.baseElement = baseElement;
    }

    @Synchronized("baseElement")
    @Override
    public void displayOn(final @NonNull Inventory inventory, final int locX, final int locY) {
        this.baseElement.displayOn(inventory, locX, locY);
    }

    @Override
    public void accept(final @NonNull InventoryInteractEvent event) {
        this.baseElement.accept(event);
    }

    @Override
    public boolean is(final @NonNull ItemStack icon) {
        return this.baseElement.is(icon);
    }

    @Override
    public boolean is(final @NonNull Element element) {
        if (this.baseElement instanceof ThreadSafeElement) {
            return this.baseElement.is(((ThreadSafeElement) element).baseElement);
        }
        return this.baseElement.is(element);
    }
}