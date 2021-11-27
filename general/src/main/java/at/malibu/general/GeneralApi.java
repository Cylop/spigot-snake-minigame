package at.malibu.general;

import at.malibu.api.Api;
import at.malibu.api.commands.registrar.CommandRegistrar;
import at.malibu.general.listeners.ListenerRegistrar;
import org.bukkit.plugin.java.JavaPlugin;

public interface GeneralApi extends Api {

    CommandRegistrar getCommandRegistrar();

    ListenerRegistrar getListenerRegistrar();

    JavaPlugin getPlugin();

}
