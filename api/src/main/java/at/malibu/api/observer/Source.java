package at.malibu.api.observer;

public interface Source<T> {
    void subscribe(Target<T> target);

    void unsubscribe(Target<T> target);

    void notifyTargets(T argument);
}