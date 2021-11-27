package at.malibu.general.commands.registrar;

import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.registrar.CommandRegistrar;
import at.malibu.general.commands.AbstractBaseCommand;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

@CommonsLog
public class PaperCommandRegistrar implements CommandRegistrar {

    @Getter
    private CommandMap commandMap;

    public PaperCommandRegistrar() {
        commandMap = Bukkit.getCommandMap();
    }

    /**
     * Register commands within provided package name
     *
     * @param commandPackage package path in which the commands should be registered.
     *                       All Classes need to extend {@link AbstractBaseCommand} and must be annotated
     *                       with {@link BaseCommandInfo}
     */
    @Override
    public void registerCommands(String commandPackage) {
        new Reflections(commandPackage).getSubTypesOf(AbstractBaseCommand.class).forEach(clazz -> {
            try {
                AbstractBaseCommand command = clazz.getDeclaredConstructor().newInstance();
                commandMap.register(command.getName(), command.getCommand());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Command does not provide a zero args constructor.", e);
            }
        });
    }

}
