package at.malibu.api.observer;

/**
 * This is the Observer from the Observer pattern
 */
public interface Target<T> {
    void update(T argument);
}