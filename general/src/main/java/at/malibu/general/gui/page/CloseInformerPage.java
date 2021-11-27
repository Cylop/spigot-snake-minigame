package at.malibu.general.gui.page;

import at.malibu.api.gui.Page;
import at.malibu.api.gui.Pane;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class CloseInformerPage implements Page {
    private final @NonNull Page basePage;
    private final @NonNull Consumer<Player> consumer;


    public CloseInformerPage(final @NonNull Page basePage, final @NonNull Consumer<Player> consumer) {
        this.basePage = basePage;
        this.consumer = consumer;
        this.defineHolder(this);
    }

    @Override
    public void add(final @NonNull Pane pane, final int position) {
        this.basePage.add(pane, position);
    }

    @Override
    public void remove(final int position) {
        this.basePage.remove(position);
    }

    @Override
    public void rearrange(final int paneIndex, final int position) {
        this.basePage.rearrange(paneIndex, position);
    }

    @Override
    public void defineHolder(final @NonNull Page holder) {
        this.basePage.defineHolder(holder);
    }

    @Override
    public void showTo(final @NonNull Player player) {
        this.basePage.showTo(player);
    }

    @Override
    public void handleClose(final @NonNull InventoryCloseEvent event) {
        this.basePage.handleClose(event);
        this.consumer.accept((Player) event.getPlayer());
    }

    @Override
    public void update(final Object argument) {
        this.basePage.update(argument);
    }

    @Override
    @Deprecated
    public @NotNull Inventory getInventory() {
        return this.basePage.getInventory();
    }

    @Override
    public void accept(final @NonNull InventoryInteractEvent event) {
        this.basePage.accept(event);
    }
}