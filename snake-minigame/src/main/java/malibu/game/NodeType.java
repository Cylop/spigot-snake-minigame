package malibu.game;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public enum NodeType {

    EMPTY(buildIcon("", Material.GRAY_STAINED_GLASS_PANE)),
    SNAKE_HEAD(buildIcon("Kopf", Material.ORANGE_WOOL)),
    SNAKE_NODE(buildIcon("", Material.GRAY_WOOL)),
    FOOD(buildIcon("Essen", Material.GOLDEN_APPLE));

    ItemStack icon;

    private static ItemStack buildIcon(final @NonNull String name, final @NonNull Material material) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
