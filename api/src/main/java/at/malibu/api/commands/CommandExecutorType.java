package at.malibu.api.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum CommandExecutorType {
    PLAYER(Player.class),
    CONSOLE(ConsoleCommandSender.class);

    // COMMAND_BLOCK <- will be added

    @Getter
    private Class<? extends CommandSender> clazz;

    public static String getHumanName(@NonNull CommandExecutorType commandExecutorType) {
        var name = commandExecutorType.name();
        return name.substring(0, 2).toUpperCase() + name.substring(2).toUpperCase();
    }

    public final boolean isFor(@NonNull CommandSender sender) {
        return this.getClazz().isAssignableFrom(sender.getClass());
    }
}
