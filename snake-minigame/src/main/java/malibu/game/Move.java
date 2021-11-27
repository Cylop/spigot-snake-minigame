package malibu.game;

import lombok.NonNull;

public record Move(int moveX, int moveY) {

    public static Move from(final @NonNull Direction direction) {
        int moveX = 0, moveY = 0;

        switch (direction) {
            case UP -> moveY--;
            case DOWN -> moveY++;
            case RIGHT -> moveX++;
            case LEFT -> moveX--;
            default -> throw new UnsupportedOperationException("Unsupported direction provided");
        }

        return new Move(moveX, moveY);
    }
}
