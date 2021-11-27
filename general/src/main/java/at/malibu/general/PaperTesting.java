package at.malibu.general;

import at.malibu.general.server.BasicServerInformation;
import org.mineacademy.fo.plugin.SimplePlugin;

public class PaperTesting extends SimplePlugin {

    private static volatile SimplePlugin instance;

    public static SimplePlugin getInstance() {
        return instance;
    }


    @Override
    protected void onPluginStart() {
        instance = this;

        GeneralApi generalApi = new General(this, new BasicServerInformation("My Server", "prefix"));
        generalApi.getCommandRegistrar().registerCommands("at.malibu.general.testing");
    }


}
