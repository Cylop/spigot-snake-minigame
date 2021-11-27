package at.malibu.general.gui.element;

import at.malibu.api.gui.Element;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class LiveElement implements Element {
    private final Plugin plugin;
    private final int period;
    private final Element[] frames;

    public LiveElement(final @NonNull Plugin plugin, final int period, final @NonNull Element... frames) {
        this.plugin = plugin;
        this.period = period;
        this.frames = frames.clone();
    }

    private Element nullElement() {
        return new BasicElement(new ItemStack(Material.PAPER), "nullElement");
    }

    private Element findFrame(final @NonNull ItemStack icon) {
        for (final Element frame : this.frames) {
            if (frame.is(icon)) {
                return frame;
            }
        }

        return this.nullElement();
    }

    private boolean contains(final @NonNull Element element) {
        for (final Element frame : this.frames) {
            if (frame.is(element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void accept(final @NonNull InventoryInteractEvent event) {
        for (final Element frame : this.frames) {
            frame.accept(event);
        }
    }

    @Override
    public void displayOn(final @NonNull Inventory inventory, final int locX, final int locY) {
        this.frames[0].displayOn(inventory, locX, locY);
        new BukkitRunnable() {
            private int iterator;

            @Override
            public void run() {
                if (inventory.getViewers().isEmpty()) {
                    this.cancel();
                    return;
                }
                this.nextFrame().displayOn(inventory, locX, locY);
            }

            private Element nextFrame() {
                this.iterator = this.iterator + 1 < LiveElement.this.frames.length
                        ? this.iterator + 1
                        : 0;

                return LiveElement.this.frames[this.iterator];
            }
        }.runTaskTimer(this.plugin, 1, this.period);
    }

    @Override
    public boolean is(final @NonNull ItemStack icon) {
        return this.findFrame(icon).is(icon);
    }

    @Override
    public boolean is(final @NonNull Element element) {
        return this.contains(element);
    }
}