package at.malibu.general.gui.target;

import at.malibu.api.gui.GuiAction;
import at.malibu.api.gui.Requirement;
import at.malibu.general.gui.event.ElementDragEvent;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

public record DragGuiAction(@NonNull Consumer<ElementDragEvent> handler,
                            @NonNull Requirement... reqs) implements GuiAction {

    public DragGuiAction(final @NonNull Consumer<ElementDragEvent> handler, final @NonNull Requirement... reqs) {
        this.handler = handler;
        this.reqs = reqs.clone();
    }

    @Override
    public void handle(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryDragEvent &&
                Arrays.stream(this.reqs).allMatch(req -> req.control(event))) {

            this.handler.accept(new ElementDragEvent((InventoryDragEvent) event));
        }
    }
}