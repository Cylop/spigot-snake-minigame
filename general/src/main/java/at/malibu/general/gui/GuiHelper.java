package at.malibu.general.gui;

import at.malibu.general.gui.listener.InventoryClickListener;
import at.malibu.general.gui.listener.InventoryCloseListener;
import at.malibu.general.gui.listener.InventoryDragListener;
import at.malibu.general.gui.listener.PluginListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GuiHelper {

    private static final Listener[] LISTENERS = {
            new PluginListener(new GuiHelper()),
            new InventoryClickListener(),
            new InventoryDragListener(),
            new InventoryCloseListener(),
    };

    private static final Queue<Plugin> PLUGIN_QUEUE = new ConcurrentLinkedQueue<>();

    public void prepareFor(final Plugin plugin) {
        Objects.requireNonNull(plugin);
        if (PLUGIN_QUEUE.isEmpty()) {
            this.registerListeners(plugin);
        }

        synchronized (this) {
            PLUGIN_QUEUE.add(plugin);
        }
    }

    public void processPluginDisable(final PluginDisableEvent event) {
        assert PLUGIN_QUEUE.peek() != null;
        if (!PLUGIN_QUEUE.peek().equals(event.getPlugin())) {
            synchronized (this) {
                PLUGIN_QUEUE.remove(event.getPlugin());
                return;
            }
        }

        synchronized (this) {
            PLUGIN_QUEUE.poll();
        }

        final Plugin nextPlugin = PLUGIN_QUEUE.peek();
        if (nextPlugin != null && nextPlugin.isEnabled()) {
            this.registerListeners(nextPlugin);
        }
    }

    private void registerListeners(final Plugin plugin) {
        synchronized (this) {
            Arrays.stream(LISTENERS).forEach(listener ->
                    Bukkit.getPluginManager().registerEvents(listener, plugin));
        }
    }
}
