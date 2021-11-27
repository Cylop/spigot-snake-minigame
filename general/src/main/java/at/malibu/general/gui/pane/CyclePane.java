package at.malibu.general.gui.pane;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Pane;
import at.malibu.api.observer.Target;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class CyclePane implements Pane {
    private final @NonNull Plugin plugin;
    private final int period;
    private final Pane @NonNull [] frames;

    public CyclePane(@NonNull final Plugin plugin, final int period, @NonNull final Pane... frames) {
        this.plugin = plugin;
        this.period = period;
        this.frames = frames;
    }

    @Override
    public void fill(final @NotNull Element element) {
        for (final Pane frame : this.frames) {
            frame.fill(element);
        }
    }

    public void fill(final int frame, final Element element) {
        this.frames[frame].fill(element);
    }

    @Override
    public void fill(final Element... elements) {
        for (final Pane frame : this.frames) {
            frame.fill(elements);
        }
    }

    public void fill(final int frame, final Element... elements) {
        this.frames[frame].fill(elements);
    }

    @Override
    public void clear() {
        for (final Pane frame : this.frames) {
            frame.clear();
        }
    }

    @Override
    public boolean add(final @NotNull Element element) {
        for (final Pane frame : this.frames) {
            if (frame.add(element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Element[] add(final Element... elements) {
        Element[] leftOvers = elements;
        for (final Pane frame : this.frames) {
            leftOvers = frame.add(leftOvers);
            if (leftOvers.length == 0) {
                break;
            }
        }

        return leftOvers;
    }


    @Override
    @Deprecated
    public void insert(final @NotNull Element element, final int locX, final int locY,
                       final boolean shift) throws IllegalArgumentException {
    }

    public void insert(final int frame, final Element element, final int locX, final int locY,
                       final boolean shift) throws IllegalArgumentException {

        this.frames[frame].insert(element, locX, locY, shift);
    }

    @Override
    public void replaceAll(final Element... elements) {
        for (final Pane frame : this.frames) {
            frame.replaceAll(elements);
        }
    }

    public void replaceAll(final int frame, final Element... elements) {
        this.frames[frame].replaceAll(elements);
    }

    @Override
    @Deprecated
    public void remove(final int locX, final int locY) throws IllegalArgumentException {
        // this method is useless because you have to specify the frame to remove an element
    }

    public void remove(final int frame, final int locX, final int locY)
            throws IllegalArgumentException {

        this.frames[frame].remove(locX, locY);
    }

    @Override
    public void subscribe(final @NotNull Target<Object> target) {
        Arrays.stream(this.frames).forEach(frame -> frame.subscribe(target));
    }

    @Override
    public boolean contains(final @NotNull ItemStack icon) {
        for (final Pane frame : this.frames) {
            if (frame.contains(icon)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void accept(final @NotNull InventoryInteractEvent event) {
        for (final Pane frame : this.frames) {
            frame.accept(event);
        }
    }

    @Override
    public void renderOn(final @NotNull Inventory inventory) {
        this.frames[0].renderOn(inventory);

        new BukkitRunnable() {
            private int iterator;

            @Override
            public void run() {
                if (inventory.getViewers().isEmpty()) {
                    this.cancel();
                } else {
                    this.nextFrame().renderOn(inventory);
                }
            }

            private Pane nextFrame() {
                this.iterator = this.iterator + 1 < CyclePane.this.frames.length
                        ? this.iterator + 1
                        : 0;

                return CyclePane.this.frames[this.iterator];
            }
        }.runTaskTimer(this.plugin, 1, this.period);
    }
}