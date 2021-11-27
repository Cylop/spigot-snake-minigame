package at.malibu.general.server;

public interface ServerInformation {
    String getName();

    default String getPrefix() {
        return getName();
    }
}
