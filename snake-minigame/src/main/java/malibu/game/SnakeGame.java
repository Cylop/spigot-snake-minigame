package malibu.game;

import at.malibu.api.gui.Element;
import at.malibu.api.gui.Page;
import at.malibu.api.gui.Pane;
import at.malibu.general.gui.element.BasicElement;
import at.malibu.general.gui.event.ElementBasicEvent;
import at.malibu.general.gui.page.ChestPage;
import at.malibu.general.gui.pane.StaticPane;
import at.malibu.general.gui.target.BasicGuiAction;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class SnakeGame extends BukkitRunnable {

    @Getter
    private final Bounds boardSize;

    private final Snake snake;
    private final Pane gamePane;

    @Getter
    private final Food food;

    private boolean didWin;

    public SnakeGame(final Bounds boardSize, final Player player) {
        this.boardSize = boardSize;

        final var middle = this.boardSize.getMiddle();
        this.snake = new Snake(this, middle[0], middle[1]);
        this.food = new Food(this, this.boardSize.getRandomCoordinate());

        this.gamePane = new StaticPane(0, 0, this.boardSize.rows(), this.boardSize.columns(), new BasicElement(this.backgroundItemStack(), new BasicGuiAction(ElementBasicEvent::cancel)));

        final Pane controls = new StaticPane(2, this.boardSize.rows(), 1, 5, this.getControls().toArray(Element[]::new));

        final Page gamePage = new ChestPage("Snake Game", boardSize.columns() * boardSize.rows() + 9, this.gamePane, controls);
        gamePage.showTo(player);
    }

    @Override
    public void run() {
        this.gamePane.clear();

        if (this.didWin) {
            this.renderWin();
            return;
        }

        if (this.snake.isAlive()) {
            if (this.boardSize.getLength() == this.snake.getLength()) {
                this.didWin = true;
            }

            this.renderFood();

            this.renderSnake();
            this.snake.update();
            return;
        }

        this.renderGameOver();
    }

    private void renderFood() {
        this.gamePane.insert(new BasicElement(this.food.getType().icon), this.food.getCoordinate().getX(), this.food.getCoordinate().getY(), false);
    }

    private void renderSnake() {
        this.gamePane.fill(new BasicElement(this.backgroundItemStack(), new BasicGuiAction(ElementBasicEvent::cancel)));

        this.snake.render(this.gamePane);
    }

    private void renderGameOver() {
        this.gamePane.fill(new BasicElement(this.gameOverItemStack(), new BasicGuiAction(ElementBasicEvent::cancel)));
        this.cancel();
    }

    private void renderWin() {
        this.gamePane.fill(new BasicElement(this.winItemStack(), new BasicGuiAction(ElementBasicEvent::cancel)));
        this.cancel();
    }

    private List<Element> getControls() {
        return Arrays.stream(Direction.values()).map(this::getControlElement).toList();
    }

    private Element getControlElement(final Direction direction) {
        final ItemStack controlStack = new ItemStack(direction == Direction.SPLITTER ? Material.GREEN_STAINED_GLASS_PANE : Material.CLAY_BALL);
        final ItemMeta itemMeta = controlStack.getItemMeta();
        itemMeta.displayName(Component.text(direction == Direction.SPLITTER ? "" : ChatColor.GREEN + direction.name()));
        controlStack.setItemMeta(itemMeta);
        return new BasicElement(controlStack, new BasicGuiAction(event -> {
            event.cancel();
            if (direction != Direction.SPLITTER)
                this.snake.move(direction);
        }));
    }

    private ItemStack backgroundItemStack() {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta itemMeta = background.getItemMeta();
        itemMeta.displayName(Component.text(""));
        background.setItemMeta(itemMeta);
        return background;
    }

    private ItemStack gameOverItemStack() {
        final ItemStack background = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        final ItemMeta itemMeta = background.getItemMeta();
        itemMeta.displayName(Component.text("GAME OVER"));
        background.setItemMeta(itemMeta);
        return background;
    }

    private ItemStack winItemStack() {
        final ItemStack background = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        final ItemMeta itemMeta = background.getItemMeta();
        itemMeta.displayName(Component.text("YOU WON!"));
        background.setItemMeta(itemMeta);
        return background;
    }
}
