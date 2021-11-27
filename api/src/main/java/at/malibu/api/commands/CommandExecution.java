package at.malibu.api.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public interface CommandExecution {
    void execute(ConsoleCommandSender sender, String[] args);

    void execute(Player player, String[] args);
}
