package at.malibu.api.commands.registrar;

import org.bukkit.command.CommandMap;

public interface CommandRegistrar {
    void registerCommands(String commandPackage);

    CommandMap getCommandMap();
}
