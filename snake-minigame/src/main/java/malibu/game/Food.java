package malibu.game;

import at.malibu.api.gui.Pane;
import at.malibu.general.gui.element.BasicElement;
import at.malibu.general.gui.event.ElementBasicEvent;
import at.malibu.general.gui.target.BasicGuiAction;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Food implements Renderable {

    private final NodeType type = NodeType.FOOD;

    private final SnakeGame snakeGame;
    private @NonNull Coordinate coordinate;

    @Override
    public void render(@NonNull final Pane pane) {
        pane.insert(new BasicElement(this.type.icon, new BasicGuiAction(ElementBasicEvent::cancel)), this.coordinate.getX(), this.coordinate.getY(), false);
    }

    public void onSnakeEat() {
        this.coordinate = this.snakeGame.getBoardSize().getRandomCoordinate();
    }
}
