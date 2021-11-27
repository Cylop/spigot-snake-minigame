package malibu.game;

import at.malibu.api.gui.Pane;
import lombok.NonNull;

public interface Renderable {
    void render(@NonNull final Pane pane);
}
