package at.malibu.general.gui.listener;

import at.malibu.general.gui.GuiHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public final record PluginListener(GuiHelper guiHelper) implements Listener {
    @EventHandler
    public void listener(final PluginDisableEvent event) {
        this.guiHelper.processPluginDisable(event);
    }
}