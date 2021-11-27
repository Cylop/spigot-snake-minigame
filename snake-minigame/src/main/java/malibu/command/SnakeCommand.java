package malibu.command;

import at.malibu.api.commands.CommandExecutorType;
import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.annotations.HasPermission;
import at.malibu.api.commands.annotations.OnlyFor;
import at.malibu.general.commands.AbstractBaseCommand;
import malibu.game.Bounds;
import malibu.game.SnakeGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@OnlyFor(executor = CommandExecutorType.PLAYER)
@HasPermission(permission = "snake.play")
@BaseCommandInfo(name = "snake", description = "My cool snake command!")
public class SnakeCommand extends AbstractBaseCommand {

    @Override
    public void execute(final Player player, final String[] args) {
        final SnakeGame game = new SnakeGame(new Bounds(9, 5), player); // 45 fields
        game.runTaskTimer(Bukkit.getPluginManager().getPlugins()[0], 5, 10);

    }
}
