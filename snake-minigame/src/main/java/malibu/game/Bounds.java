package malibu.game;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public record Bounds(int columns, int rows) {

    private static final Random RANDOM = new Random();

    public Bounds(final int columns, final int rows) {
        if (columns < 0 || columns > 9 || rows < 1 || rows > 8) {
            throw new IllegalArgumentException(String.format("Snake Gamebounds are unsupported. [columns=%s, rows=%s]", columns, rows));
        }

        this.columns = columns;
        this.rows = rows;
    }

    public Integer[] getMiddle() {
        return new Integer[]{Math.floorDiv(this.columns, 2), Math.floorDiv(this.rows, 2)};
    }

    public int getMiddleCoordinate() {
        return Arrays.stream(this.getMiddle()).reduce(0, (subtotal, value) -> subtotal * value);
    }

    public Coordinate getRandomCoordinate() {
        return new Coordinate(RANDOM.nextInt(this.columns - 1), RANDOM.nextInt(this.rows - 1));
    }

    public @NotNull Coordinate normalizeCoordinate(final @NotNull Coordinate coordinate) {
        int newX = coordinate.getX();
        int newY = coordinate.getY();

        final int x = coordinate.getX();
        final int y = coordinate.getY();

        if (x > this.columns - 1) newX = 0;
        if (x < 0) newX = this.columns - 1;

        if (y > this.rows - 1) newY = 0;
        if (y < 0) newY = this.rows - 1;

        return new Coordinate(newX, newY);
    }

    public int getLength() {
        return this.columns * this.rows;
    }
}