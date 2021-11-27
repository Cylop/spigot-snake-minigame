package at.malibu.general.gui.pane;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Pane;
import at.malibu.api.observer.Target;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ThreadSafePane implements Pane {
    private final Pane basePane;

    public ThreadSafePane(@NonNull final Pane basePane) {
        this.basePane = basePane;
    }

    @Synchronized("basePane")
    @Override
    public void fill(final @NotNull Element element) {
        this.basePane.fill(element);
    }

    @Synchronized("basePane")
    @Override
    public void fill(final @NonNull Element... elements) {
        this.basePane.fill(elements);
    }

    @Synchronized("basePane")
    @Override
    public void clear() {
        this.basePane.clear();
    }

    @Synchronized("basePane")
    @Override
    public boolean add(final @NotNull Element element) {
        return this.basePane.add(element);
    }

    @Synchronized("basePane")
    @Override
    public Element[] add(final @NonNull Element... elements) {
        return this.basePane.add(elements);
    }

    @Synchronized("basePane")
    @Override
    public void insert(final @NotNull Element element, final int locX, final int locY, final boolean shift)
            throws IllegalArgumentException {

        this.basePane.insert(element, locX, locY, shift);
    }

    @Synchronized("basePane")
    @Override
    public void replaceAll(final @NonNull Element... elements) {
        this.basePane.replaceAll(elements);
    }

    @Synchronized("basePane")
    @Override
    public void remove(final int locX, final int locY) throws IllegalArgumentException {
        this.basePane.remove(locX, locY);
    }

    @Synchronized("basePane")
    @Override
    public void subscribe(final @NotNull Target<Object> target) {
        this.basePane.subscribe(target);
    }

    @Override
    public boolean contains(final @NotNull ItemStack icon) {
        return this.basePane.contains(icon);
    }

    @Override
    public void accept(final @NotNull InventoryInteractEvent event) {
        this.basePane.accept(event);
    }

    @Override
    public void renderOn(final @NotNull Inventory inventory) {
        this.basePane.renderOn(inventory);
    }
}