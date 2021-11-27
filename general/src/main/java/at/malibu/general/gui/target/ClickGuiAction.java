package at.malibu.general.gui.target;

import at.malibu.api.gui.GuiAction;
import at.malibu.api.gui.Requirement;
import at.malibu.general.gui.event.ElementClickEvent;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

public record ClickGuiAction(@NonNull Consumer<ElementClickEvent> handler,
                             Requirement... reqs) implements GuiAction {

    public ClickGuiAction(final @NonNull Consumer<ElementClickEvent> handler, final @NonNull Requirement... reqs) {
        this.handler = handler;
        this.reqs = reqs.clone();
    }

    @Override
    public void handle(final @NotNull InventoryInteractEvent event) {
        if (event instanceof InventoryClickEvent &&
                Arrays.stream(this.reqs).allMatch(req -> req.control(event))) {

            this.handler.accept(new ElementClickEvent((InventoryClickEvent) event));
        }
    }
}