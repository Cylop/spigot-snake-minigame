package at.malibu.api.gui;

import at.malibu.api.observer.Target;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public interface Page extends InventoryHolder, Target<Object> {

    void add(@NonNull Pane pane, int position);

    void remove(int position);

    void rearrange(int paneIndex, int position);

    void defineHolder(@NonNull Page holder);

    void showTo(@NonNull Player player);

    void handleClose(@NonNull InventoryCloseEvent event);

    void accept(@NonNull InventoryInteractEvent event);
}