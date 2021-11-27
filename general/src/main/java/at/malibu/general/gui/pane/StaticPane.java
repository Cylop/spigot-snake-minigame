package at.malibu.general.gui.pane;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Pane;
import at.malibu.api.observer.Source;
import at.malibu.api.observer.Target;
import at.malibu.api.observer.source.BasicSource;
import at.malibu.general.gui.element.BasicElement;
import at.malibu.general.gui.requirements.SlotRequirement;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@CommonsLog
public final class StaticPane implements Pane {

    private static final String OUT_OF_BOUNDS =
            "The Location you specified [%s][%s] is out of bounds";

    private final Source<Object> source;

    private final Element[][] paneElements;
    private final int locX;
    private final int locY;

    public StaticPane(final int locX, final int locY, final int height, final int length) {
        this.source = new BasicSource<>();
        this.locX = locX;
        this.locY = locY;
        this.paneElements = new Element[height][length];
        this.clear();
    }

    public StaticPane(final int locX, final int locY, final int height, final int length,
                      final @NonNull Element element) {

        this(locX, locY, height, length);
        this.replaceAll(element);
    }

    public StaticPane(final int locX, final int locY, final int height, final int length,
                      final @NonNull Element... elements) {

        this(locX, locY, height, length);
        this.add(elements);
    }

    private Element getEmptyElement() {
        return new BasicElement(
                new ItemStack(Material.GUNPOWDER), "emptyElement"
        );
    }

    private void validate(final int inventorySize) throws IllegalArgumentException {
        final boolean locXFaulty = this.locX < 0;
        final boolean locYFaulty = this.locY < 0;
        final boolean heightFaulty = this.locY + this.getHeight() > inventorySize / 9 || this.getHeight() <= 0;
        final boolean lengthFaulty = this.locX + this.getLength() > 9 || this.getLength() <= 0;
        if (locXFaulty || locYFaulty || heightFaulty || lengthFaulty) {
            throw new IllegalArgumentException(
                    String.format(
                            "Validation check failed.%n" +
                                    "locX (%s) is faulty: %s, locY (%s) is faulty: %s, " +
                                    "height (%s) is faulty: %s, length (%s) is faulty: %s",
                            this.locX, locXFaulty, this.locY, locYFaulty, this.getHeight(), heightFaulty, this.getLength(),
                            lengthFaulty
                    )
            );
        }
    }

    private boolean isWithinBounds(final int xToCheck, final int yToCheck) {
        return xToCheck < this.getLength() && yToCheck < this.getHeight() && xToCheck >= 0 && yToCheck >= 0;
    }

    private void shiftElementAt(final int xToShift, final int yToShift) {
        for (int y = this.getHeight() - 1; y >= 0; y--) {
            for (int x = this.getLength() - 1; x >= 0; x--) {
                if (y < yToShift || y == yToShift && x < xToShift) {
                    continue;
                }

                if (x + 1 < this.getLength()) {
                    this.paneElements[y][x + 1] = this.paneElements[y][x];
                } else if (y + 1 < this.getHeight()) {
                    this.paneElements[y + 1][0] = this.paneElements[y][x];
                }
            }
        }

        this.paneElements[yToShift][xToShift] = this.getEmptyElement();
    }

    private boolean forEachSlot(final @NonNull BiFunction<? super Integer, ? super Integer, Boolean> action) {
        for (int y = 0; this.isWithinBounds(0, y); y++) {
            for (int x = 0; this.isWithinBounds(x, y); x++) {
                if (action.apply(y, x)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void forEachSlot(final @NonNull BiConsumer<? super Integer, ? super Integer> action) {
        this.forEachSlot((y, x) -> {
            action.accept(y, x);
            return false;
        });
    }

    @Override
    public void fill(final @NonNull Element element) {
        this.fill(new Element[]{element});
        this.source.notifyTargets(new Object());
    }

    @Override
    public void fill(final @NonNull Element... elements) {
        final Queue<Element> queue = new LinkedList<>(
                Arrays.asList(elements)
        );
        this.forEachSlot((y, x) -> {
            if (queue.isEmpty()) {
                queue.addAll(Arrays.asList(elements));
            }
            if (this.paneElements[y][x].is(this.getEmptyElement())) {
                this.paneElements[y][x] = queue.poll();
            }
        });
        this.source.notifyTargets(new Object());
    }

    @Override
    public void clear() {
        this.replaceAll(this.getEmptyElement());
    }

    @Override
    public boolean add(final @NonNull Element element) {
        return this.forEachSlot((y, x) -> {
            if (this.paneElements[y][x].is(this.getEmptyElement())) {
                this.paneElements[y][x] = Objects.requireNonNull(element);
                this.source.notifyTargets(new Object());
                return true;
            }
            return false;
        });
    }

    @Override
    public Element[] add(final @NonNull Element... elements) {
        final ArrayList<Element> remaining = new ArrayList<>();
        for (final Element element : elements) {
            if (!this.add(element)) {
                remaining.add(element);
            }
        }
        return remaining.toArray(new Element[0]);
    }

    @Override
    public void insert(final @NonNull Element element, final int locX, final int locY,
                       final boolean shift) throws IllegalArgumentException {

        if (this.isWithinBounds(locX, locY)) {
            if (shift && !this.paneElements[locY][locX].is(this.getEmptyElement())) {
                this.shiftElementAt(locX, locY);
            }
            this.paneElements[locY][locX] = element;
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            OUT_OF_BOUNDS,
                            locX, locY
                    )
            );
        }
        this.source.notifyTargets(new Object());
    }

    @Override
    public void replaceAll(final @NonNull Element... elements) {
        final Queue<Element> queue = new LinkedList<>(
                Arrays.asList(elements)
        );
        this.forEachSlot((y, x) -> {
            if (queue.isEmpty()) {
                queue.addAll(Arrays.asList(elements));
            }
            this.paneElements[y][x] = queue.poll();
        });
        this.source.notifyTargets(new Object());
    }

    @Override
    public void remove(final int locX, final int locY) throws IllegalArgumentException {
        if (this.isWithinBounds(locX, locY)) {
            this.paneElements[locY][locX] = this.getEmptyElement();
            this.source.notifyTargets(new Object());
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            OUT_OF_BOUNDS,
                            locX, locY
                    )
            );
        }
    }

    @Override
    public void subscribe(final @NonNull Target<Object> target) {
        this.source.subscribe(target);
    }

    @Override
    public boolean contains(final @NonNull ItemStack icon) {
        return this.forEachSlot((y, x) -> {
            return this.paneElements[y][x].is(icon);
        });
    }

    @Override
    public void accept(final @NonNull InventoryInteractEvent event) {
        this.forEachSlot((y, x) -> {
            if (new SlotRequirement(this.locX + x + (this.locY + y) * 9).control(event)) {
                this.paneElements[y][x].accept(event);
            }
        });
    }

    @Override
    public void renderOn(final Inventory inventory) {
        try {
            this.validate(inventory.getSize());
        } catch (final IllegalArgumentException ex) {
            log.fatal(ex.toString());
            return;
        }
        this.forEachSlot((y, x) -> {
            final Element element = this.paneElements[y][x];
            if (!element.is(this.getEmptyElement())) {
                element.displayOn(inventory, this.locX + x, this.locY + y);
            }
        });
    }

    private int getLength() {
        return this.paneElements[0].length;
    }

    private int getHeight() {
        return this.paneElements.length;
    }
}