package at.malibu.general.testing;

import at.malibu.api.commands.CommandExecutorType;
import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.annotations.HasPermission;
import at.malibu.api.commands.annotations.OnlyFor;
import at.malibu.api.gui.Element;
import at.malibu.api.gui.Page;
import at.malibu.api.gui.Pane;
import at.malibu.general.commands.AbstractBaseCommand;
import at.malibu.general.gui.element.BasicElement;
import at.malibu.general.gui.event.ElementBasicEvent;
import at.malibu.general.gui.page.ChestPage;
import at.malibu.general.gui.page.InventorySize;
import at.malibu.general.gui.pane.StaticPane;
import at.malibu.general.gui.target.BasicGuiAction;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

@HasPermission(permission = "my.permission")
@OnlyFor(executor = CommandExecutorType.PLAYER)
@BaseCommandInfo(name = "guitest", description = "Das ist meine Beschreibung", usageMessage = "Nutze /guitest")
public class GuiTest extends AbstractBaseCommand {

    private final List<ShopItem> shopItemList = new ArrayList<>();

    public GuiTest() {
        for (int i = 0; i < Material.values().length; i++) {
            Material material = RandomUtil.nextItem(Material.values());
            if (material == null || material.isAir()) continue;
            int price = RandomUtil.nextInt(100001);
            shopItemList.add(new ShopItem("Item: " + material.name(), material, price));
        }
    }

    @Override
    public void execute(Player player, String[] args) {

        var shopState = new ShopState(this.shopItemList);

        Element backGroundElement = new BasicElement(
                new ItemStack(Material.DIAMOND),
                new BasicGuiAction(event -> {
                    event.cancel();
                    event.player().sendMessage(Component.text("clicked item"));
                })
        );

        Element secBgItem = new BasicElement(
                new ItemStack(Material.GLASS_BOTTLE),
                new BasicGuiAction(ElementBasicEvent::cancel)
        );

        Pane itemPane = new StaticPane(2, 2, 4, 5, secBgItem);

        Element prev = new BasicElement(
                ItemCreator.builder().material(CompMaterial.ARROW).name("Eine Seite zurück").build().make(),
                new BasicGuiAction(event -> {
                    event.cancel();
                    shopState.prev();
                    player.playSound(player.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                    itemPane.replaceAll(getCurrentShopItems(shopState));
                })
        );

        Element next = new BasicElement(
                ItemCreator.builder().material(CompMaterial.ARROW).name("Eine Seite nach vorne").build().make(),
                new BasicGuiAction(event -> {
                    event.cancel();
                    shopState.next();
                    player.playSound(player.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1f, 2f);
                    itemPane.replaceAll(getCurrentShopItems(shopState));
                })
        );

        Pane pane1 = new StaticPane(0, 0, 6, 9, backGroundElement);


        Pane controls = new StaticPane(8, 0, 2, 1, prev, next);

        itemPane.replaceAll(getCurrentShopItems(shopState));

        Page page = new ChestPage("Mein Titel", InventorySize.SIX_ROWS, pane1, itemPane, controls);


        page.showTo(player);
    }

    public Element[] getCurrentShopItems(ShopState shopState) {
        return shopState
                .getCurrentSubset()
                .stream().map(shopItem -> new BasicElement(fromShopItem(shopItem))).toArray(Element[]::new);
    }

    public ItemStack fromShopItem(@NotNull ShopItem shopItem) {
        return ItemCreator.builder().material(CompMaterial.fromMaterial(shopItem.icon())).name(ChatColor.GRAY + shopItem.name()).lores(List.of(ChatColor.GRAY + "Dieses Item kostet", "", ChatColor.GREEN + "" + shopItem.price() + ChatColor.GOLD + " Gold")).build().make();
    }
}


class ShopState {
    private final List<ShopItem> shopItemList;
    private final int pages;
    private int currentPage;

    public ShopState(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;

        this.currentPage = 0;
        this.pages = (int) Math.ceil(Math.max(this.shopItemList.size(), 1d) / 20);
    }

    public void prev() {
        if (currentPage > 0) this.currentPage--;
    }

    public void next() {
        if (currentPage < this.pages) currentPage++;
    }

    public List<ShopItem> getCurrentSubset() {
        return new ArrayList<>(this.shopItemList).subList(this.currentPage * 20, Math.min(this.shopItemList.size(), this.currentPage * 20 + 20));
    }


}

record ShopItem(String name, Material icon, int price) {
}
