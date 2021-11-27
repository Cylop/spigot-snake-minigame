package at.malibu.general.gui.target;

import at.malibu.api.gui.GuiAction;
import at.malibu.api.gui.Requirement;
import at.malibu.general.gui.event.ElementBasicEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public record BasicGuiAction(Consumer<ElementBasicEvent> handler,
                             Requirement... reqs) implements GuiAction {

    public BasicGuiAction(final Consumer<ElementBasicEvent> handler, final Requirement... reqs) {
        this.handler = Objects.requireNonNull(handler);
        this.reqs = Objects.requireNonNull(reqs.clone());
    }

    @Override
    public void handle(final @NotNull InventoryInteractEvent event) {
        for (final Requirement req : this.reqs) {
            if (!req.control(event)) {
                return;
            }
        }
        this.handler.accept(new ElementBasicEvent(event));
    }
}