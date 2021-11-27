package at.malibu.api.gui;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Element {

    void displayOn(@NonNull Inventory inventory, int locX, int locY);

    void accept(@NonNull InventoryInteractEvent event);

    boolean is(@NonNull Element element);

    boolean is(@NonNull ItemStack icon);
}
