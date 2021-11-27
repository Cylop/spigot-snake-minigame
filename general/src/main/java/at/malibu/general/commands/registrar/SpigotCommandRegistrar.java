package at.malibu.general.commands.registrar;

import at.malibu.api.commands.registrar.CommandRegistrar;
import at.malibu.general.commands.AbstractBaseCommand;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

//todo: check if this is needed or simplify it to Bukkit.getServer().getCommandMap().register(..,..);

/**
 * Class that allows to dynamically register commands in a specific package
 */
@CommonsLog
public final class SpigotCommandRegistrar implements CommandRegistrar {

    @Getter
    private CommandMap commandMap;

    public SpigotCommandRegistrar() {
        try {
            final var bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("Error whilst accessing Bukkit CommandMap", e);
        }
    }

    /**
     * Register commands within provided package name
     *
     * @param commandPackage package path in which the commands should be registered.
     *                       All Classes need to extend {@link AbstractBaseCommand} and must be annotated
     *                       with {@link at.malibu.api.commands.annotations.BaseCommandInfo}
     */
    @Override
    public void registerCommands(String commandPackage) {
        new Reflections(commandPackage).getSubTypesOf(AbstractBaseCommand.class).forEach(clazz -> {
            try {
                var command = clazz.getDeclaredConstructor().newInstance();

                commandMap.register(command.getName(), command.getCommand());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Command does not provide a zero args constructor.", e);
            }
        });
    }
}
