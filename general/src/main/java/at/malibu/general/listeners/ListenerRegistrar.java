package at.malibu.general.listeners;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

@CommonsLog
public class ListenerRegistrar {

    @Getter
    private JavaPlugin plugin;

    public ListenerRegistrar(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners(String pckage) {
        new Reflections(pckage).getSubTypesOf(Listener.class).forEach((clazz) -> {
            try {
                Listener listener = clazz.getDeclaredConstructor().newInstance();
                Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Listener class must have an empty constructor", e);
            }
        });
    }

}
