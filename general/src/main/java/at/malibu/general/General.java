package at.malibu.general;

import at.malibu.api.commands.registrar.CommandRegistrar;
import at.malibu.general.commands.registrar.PaperCommandRegistrar;
import at.malibu.general.commands.registrar.SpigotCommandRegistrar;
import at.malibu.general.gui.GuiHelper;
import at.malibu.general.listeners.ListenerRegistrar;
import at.malibu.general.server.ServerInformation;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class General implements GeneralApi {

    private JavaPlugin plugin;

    private ServerInformation serverInformation;

    private CommandRegistrar commandRegistrar;
    private ListenerRegistrar listenerRegistrar;

    private GuiHelper guiHelper;

    private General() {
    }

    public General(JavaPlugin plugin, ServerInformation serverInformation) {
        this.plugin = plugin;
        this.serverInformation = serverInformation;

        this.commandRegistrar = this.isRunningPaper() ? new PaperCommandRegistrar() : new SpigotCommandRegistrar();
        this.listenerRegistrar = new ListenerRegistrar(plugin);

        this.guiHelper = new GuiHelper();
        this.guiHelper.prepareFor(this.plugin);
    }

    @Override
    public boolean isRunningPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
