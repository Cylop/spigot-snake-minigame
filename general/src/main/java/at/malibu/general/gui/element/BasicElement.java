package at.malibu.general.gui.element;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.GuiAction;
import at.malibu.api.gui.Requirement;
import at.malibu.general.gui.requirements.AddedElementRequirement;
import at.malibu.general.gui.requirements.ClickedElementRequirement;
import at.malibu.general.gui.requirements.DragRequirement;
import at.malibu.general.gui.requirements.OrRequirement;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class BasicElement implements Element {
    private final String id;
    private final ItemStack icon;
    private final GuiAction[] targets;
    private final Requirement elementReq;

    public BasicElement(final @NonNull ItemStack icon, final @NonNull String id, final @NonNull GuiAction... targets) {
        this.icon = this.encrypted(icon, id);
        this.id = id;
        this.targets = targets.clone();
        this.elementReq = new OrRequirement(
                new ClickedElementRequirement(this),
                icon.getType() == Material.AIR
                        ? new DragRequirement()
                        : new AddedElementRequirement(this)
        );
    }

    public BasicElement(final ItemStack icon, final GuiAction... targets) {
        this(icon, UUID.randomUUID().toString() + System.currentTimeMillis(), targets);
    }

    public BasicElement(final ItemStack icon, final String id) {
        this(icon, id, new GuiAction[0]);
    }

    public BasicElement(final ItemStack icon) {
        this(icon, new GuiAction[0]);
    }

    private ItemStack encrypted(final @NonNull ItemStack itemStack, final @NonNull String textToEncrypt) {
        if (itemStack.getType() == Material.AIR) {
            return itemStack;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> lore = itemMeta.getLore() == null
                ? new ArrayList<>()
                : itemMeta.getLore();
        lore.add(this.encrypted(textToEncrypt));
        itemMeta.setLore(lore);

        final ItemStack encryptedItem = itemStack.clone();
        encryptedItem.setItemMeta(itemMeta);
        return encryptedItem;
    }

    private String encrypted(final String textToEncrypt) {
        final StringBuilder encryptedText = new StringBuilder();
        for (final char ch : textToEncrypt.toCharArray()) {
            encryptedText.append(ChatColor.COLOR_CHAR).append(ch);
        }
        return encryptedText.toString();
    }

    private String decrypted(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        if (itemStack.getType() == Material.AIR) {
            return "";
        }
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            final List<String> lore = itemStack.getItemMeta().getLore();
            return Objects.requireNonNull(lore).get(lore.size() - 1).replace(String.valueOf(ChatColor.COLOR_CHAR), "");
        }
        throw new IllegalArgumentException(
                "The itemStack couldn't be decrypted because it has no lore\n" +
                        itemStack
        );
    }

    @Override
    public void displayOn(final @NonNull Inventory inventory, final int locX, final int locY) {
        inventory.setItem(locX + locY * 9, this.icon.clone());
    }

    @Override
    public void accept(final @NonNull InventoryInteractEvent event) {
        if (this.elementReq.control(event)) {
            for (final GuiAction target : this.targets) {
                target.handle(event);
            }
        }
    }

    @Override
    public boolean is(final @NonNull ItemStack itemStack) {
        return ItemUtil.isSimilar(itemStack, this.icon);
    }

    @Override
    public boolean is(final @NonNull Element element) {
        if (element instanceof BasicElement) {
            return this.is(((BasicElement) element).icon);
        }
        return false;
    }
}