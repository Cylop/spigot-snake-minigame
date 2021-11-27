package at.malibu.api.gui;

import at.malibu.api.observer.Target;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Pane {
    
    void fill(@NonNull final Element element);

    void fill(@NonNull final Element... elements);

    boolean add(@NonNull final Element element);

    Element[] add(@NonNull final Element... elements);

    void clear();

    void insert(@NonNull final Element element, int locX, int locY, boolean shift) throws IllegalArgumentException;

    void replaceAll(@NonNull final Element... elements);

    void remove(int locX, int locY) throws IllegalArgumentException;

    void subscribe(@NonNull final Target<Object> target);

    boolean contains(@NonNull final ItemStack icon);

    void accept(@NonNull final InventoryInteractEvent event);

    void renderOn(@NonNull final Inventory inventory);

}
