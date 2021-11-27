package malibu.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Node implements Cloneable {
    private int x, y;
    private NodeType nodeType;

    public Node(final NodeType nodeType) {
        this.nodeType = nodeType;
    }
    
    public Coordinate move(final int x, final int y) {
        final Coordinate oldCoordinate = new Coordinate(this.x, this.y);

        this.x = x;
        this.y = y;

        return oldCoordinate;
    }

    public Coordinate move(final @NonNull Coordinate coordinate) {
        return this.move(coordinate.getX(), coordinate.getY());
    }

    public Coordinate move(final @NonNull Move move) {
        final Coordinate oldCoordinate = new Coordinate(this.x, this.y);

        this.x += move.moveX();
        this.y += move.moveY();

        return oldCoordinate;
    }

    public Coordinate dryCalcCoordinate(final @NonNull Move move) {
        return new Coordinate(this.x + move.moveX(), this.y + move.moveY());
    }

    public Coordinate getCoordinate() {
        return new Coordinate(this.x, this.y);
    }
}
