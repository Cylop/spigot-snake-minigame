package at.malibu.general.utils.player;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerInventoryCache {

    @Getter
    private final UUID uuid;

    private ItemStack[] inventoryItems = new ItemStack[0];
    private ItemStack[] armorItems = new ItemStack[0];

    public PlayerInventoryCache(@NonNull Player player) {
        this.uuid = player.getUniqueId();
    }

    public void saveInventory() {
        var inventoryContents = this.getPlayer().getInventory().getContents();
        this.inventoryItems = Arrays.copyOf(inventoryContents, inventoryContents.length);
    }

    public void restoreInventory() {
        this.getPlayer().getInventory().setContents(this.inventoryItems);
        this.inventoryItems = new ItemStack[0];
    }

    public void saveArmor() {
        var armorContents = this.getPlayer().getInventory().getArmorContents();
        this.armorItems = Arrays.copyOf(armorContents, armorContents.length);
    }

    public void restoreArmor() {
        this.getPlayer().getInventory().setArmorContents(this.armorItems);
        this.armorItems = new ItemStack[0];
    }

    public void clearInventory() {
        this.getPlayer().getInventory().clear();
    }

    public void clearArmor() {
        this.getPlayer().getInventory().setArmorContents(new ItemStack[0]);
    }

    private Player getPlayer() {
        @NonNull var player = Objects.requireNonNull(Bukkit.getPlayer(this.uuid));
        return player;
    }

    /**
     * @return copy of players saved inventory items
     */
    public List<ItemStack> getInventoryItems() {
        return List.of(this.inventoryItems);
    }

    /**
     * @return copy of players saved armor items
     */
    public List<ItemStack> getArmorItems() {
        return List.of(this.armorItems);
    }

}
