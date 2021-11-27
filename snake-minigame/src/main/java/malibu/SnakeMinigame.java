package malibu;

import at.malibu.general.General;
import at.malibu.general.server.BasicServerInformation;
import org.bukkit.plugin.java.JavaPlugin;

public class SnakeMinigame extends JavaPlugin {

    @Override
    public void onEnable() {
        final General general = new General(this, new BasicServerInformation("Test Server", ""));
        general.getCommandRegistrar().registerCommands("malibu.command");
    }

    @Override
    public void onDisable() {

    }
}
