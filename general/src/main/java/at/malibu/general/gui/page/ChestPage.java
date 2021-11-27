package at.malibu.general.gui.page;

import at.malibu.api.gui.Page;
import at.malibu.api.gui.Pane;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ChestPage implements Page {
    private final String title;
    private final int size;
    private final List<Pane> panes;
    private final List<Player> viewers;
    private Page holder;

    public ChestPage(final @NonNull String title, final int size, final @NonNull Pane... panes) {
        this.title = title;
        this.size = Math.max(size, 9);
        this.panes = new ArrayList<>(Arrays.asList(panes));
        this.viewers = new ArrayList<>();
        this.holder = this;

        Arrays.stream(panes).forEach(pane -> pane.subscribe(this));
    }

    @Override
    public void add(final @NonNull Pane pane, final int position) {
        this.panes.add(
                Math.min(position, this.panes.size()),
                pane
        );
        this.update(new Object());
    }

    @Override
    public void remove(final int position) {
        this.panes.remove(position);
        this.update(new Object());
    }

    @Override
    public void rearrange(final int paneIndex, final int position) {
        final Pane pane = this.panes.get(paneIndex);
        this.panes.remove(paneIndex);
        this.panes.add(
                Math.min(position, this.panes.size()),
                pane
        );
        this.update(new Object());
    }

    @Override
    public void defineHolder(final @NonNull Page holder) {
        this.holder = holder;
    }

    @Override
    public void showTo(final @NotNull Player player) {
        final Inventory inventory = Bukkit.createInventory(this.holder, this.size, Component.text(this.title));
        for (final Pane pane : this.panes) {
            pane.renderOn(inventory);
        }
        player.openInventory(inventory);
        if (!this.viewers.contains(player)) {
            this.viewers.add(player);
        }
    }

    @Override
    public void handleClose(final InventoryCloseEvent event) {
        this.viewers.remove((Player) event.getPlayer());
    }

    @Override
    public void update(final Object argument) {
        Bukkit.getScheduler().runTask(
                Bukkit.getPluginManager().getPlugins()[0], () -> this.viewers.forEach(viewer -> {
                    final Inventory inventory = viewer.getOpenInventory().getTopInventory();
                    inventory.clear();
                    this.panes.forEach(pane -> pane.renderOn(inventory));
                })
        );
    }

    @Override
    @Deprecated
    public @NotNull Inventory getInventory() {
        return Bukkit.createInventory(null, 9);
    }

    @Override
    public void accept(final @NotNull InventoryInteractEvent event) {
        new ArrayList<>(this.panes).forEach(pane -> pane.accept(event));
    }
}