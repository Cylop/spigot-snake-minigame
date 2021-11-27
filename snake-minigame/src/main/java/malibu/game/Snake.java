package malibu.game;

import at.malibu.api.gui.Pane;
import at.malibu.general.gui.element.BasicElement;
import at.malibu.general.gui.event.ElementBasicEvent;
import at.malibu.general.gui.target.BasicGuiAction;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Snake implements Renderable {

    private final SnakeGame snakeGame;

    @Getter
    private final Node head;

    @Getter
    private final List<Node> body = new LinkedList<>();

    private Direction direction;

    @Getter
    private boolean alive = true;

    public Snake(final SnakeGame snakeGame, final int x, final int y) {
        this.snakeGame = snakeGame;

        this.head = new Node(x, y, NodeType.SNAKE_HEAD);
        this.direction = Direction.RIGHT;
    }

    public void move(final Direction direction) {
        this.direction = direction;
    }

    public void update() {
        if (!this.alive) return;

        final Move move = Move.from(this.direction);
        final Coordinate normalizedCoordinate = this.snakeGame.getBoardSize().normalizeCoordinate(this.head.dryCalcCoordinate(move));

        final Optional<Node> maybeBody = this.getBodyPartbyCoordinate(normalizedCoordinate);
        if (maybeBody.isPresent()) { //chrashed
            this.alive = false;
        }

        if (normalizedCoordinate.equals(this.snakeGame.getFood().getCoordinate())) {
            // food eaten
            // add body part
            this.grow();
            this.snakeGame.getFood().onSnakeEat();
        }

        Coordinate oldCoordinate = this.head.move(normalizedCoordinate);
        for (final Node bodyPart : this.body) {
            oldCoordinate = bodyPart.move(oldCoordinate);
        }
    }

    private Optional<Node> getBodyPartbyCoordinate(final Coordinate coordinate) {
        return this.body.stream().filter(node -> Objects.equals(node.getCoordinate(), coordinate)).findFirst();
    }

    private void grow() {
        this.body.add(new Node(NodeType.SNAKE_NODE));
    }


    @Override
    public void render(@NonNull final Pane pane) {
        pane.insert(new BasicElement(this.head.getNodeType().icon, new BasicGuiAction(ElementBasicEvent::cancel)), this.head.getX(), this.head.getY(), false);

        for (final Node bodyPart : this.body) {
            pane.insert(new BasicElement(bodyPart.getNodeType().icon, new BasicGuiAction(ElementBasicEvent::cancel)), bodyPart.getX(), bodyPart.getY(), false);
        }
    }

    public int getLength() {
        // +1 for head
        return this.body.size() + 1;
    }
}
