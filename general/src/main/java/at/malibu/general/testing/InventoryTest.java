package at.malibu.general.testing;

import at.malibu.api.commands.CommandExecutorType;
import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.annotations.HasPermission;
import at.malibu.api.commands.annotations.OnlyFor;
import at.malibu.general.PaperTesting;
import at.malibu.general.commands.AbstractBaseCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

@HasPermission(permission = "my.permission")
@OnlyFor(executor = CommandExecutorType.PLAYER)
@BaseCommandInfo(name = "invtest", description = "Das ist meine Beschreibung", usageMessage = "Nutze /invtest")
public class InventoryTest extends AbstractBaseCommand {

    public InventoryTest() {

    }

    @Override
    public void execute(Player player, String[] args) {

        Inventory inventory = Bukkit.createInventory(null, 9 * 5);

        ItemStack stack = ItemCreator.builder().material(CompMaterial.IRON_SWORD).name("Initial Test").build().makeMenuTool();
        //ItemStack stack = testItem.make();

        player.openInventory(inventory);

        int firstSlot = RandomUtil.nextBetween(0, (9 * 5) - 1);

        inventory.setItem(firstSlot, stack);

        new BukkitRunnable() {

            int rounds = 0;

            @Override
            public void run() {

                //inventory.clear();

                ItemMeta meta = stack.getItemMeta();
                stack.editMeta((itemMeta -> itemMeta.displayName(Component.text(ChatColor.GOLD + "" + RandomUtil.nextBetween(1, 2000)))));
                player.sendMessage(stack.getItemMeta().displayName());
                inventory.setItem(firstSlot, stack);

                if (RandomUtil.chance(20)) {
                    //inventory.setItem(RandomUtil.nextBetween(0, (9 * 5) - 1), stack);
                }
                player.updateInventory();

                if (rounds >= 100)
                    cancel();

                rounds++;
            }
        }.runTaskTimer(PaperTesting.getInstance(), 20L, 5L);
    }
}
