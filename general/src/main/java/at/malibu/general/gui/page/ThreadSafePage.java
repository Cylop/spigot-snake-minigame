package at.malibu.general.gui.page;

import at.malibu.api.gui.Page;
import at.malibu.api.gui.Pane;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ThreadSafePage implements Page {
    private final @NonNull Page basePage;

    public ThreadSafePage(@NonNull final Page basePage) {
        this.basePage = basePage;
    }

    @Override
    @Synchronized("basePage")
    public void add(final @NonNull Pane pane, final int position) {
        this.basePage.add(pane, position);
    }

    @Override
    @Synchronized("basePage")
    public void remove(final int position) {
        this.basePage.remove(position);
    }

    @Override
    @Synchronized("basePage")
    public void rearrange(final int paneIndex, final int position) {
        this.basePage.rearrange(paneIndex, position);
    }

    @Override
    public void defineHolder(final @NonNull Page holder) {
        this.basePage.defineHolder(holder);
    }

    @Override
    @Synchronized("basePage")
    public void showTo(final @NonNull Player player) {
        this.basePage.showTo(player);
    }

    @Override
    @Synchronized("basePage")
    public void handleClose(final @NonNull InventoryCloseEvent event) {
        this.basePage.handleClose(event);
    }

    @Override
    @Synchronized("basePage")
    public void update(final Object argument) {
        this.basePage.update(argument);
    }

    @Override
    @Deprecated
    public @NotNull Inventory getInventory() {
        return this.basePage.getInventory();
    }

    @Override
    @Synchronized("basePage")
    public void accept(final @NonNull InventoryInteractEvent event) {
        this.basePage.accept(event);
    }
}