package at.malibu.api.observer.source;

import at.malibu.api.observer.Source;
import at.malibu.api.observer.Target;

import java.util.Objects;
import java.util.Vector;

public final class BasicSource<T> implements Source<T> {
    private final Vector<Target<T>> targets;

    public BasicSource() {
        targets = new Vector<>();
    }

    @Override
    public void subscribe(Target<T> target) {
        if (!targets.contains(target)) {
            targets.add(Objects.requireNonNull(target));
        }
    }

    @Override
    public void unsubscribe(Target<T> target) {
        targets.remove(target);
    }

    @Override
    public void notifyTargets(T argument) {
        for (Target<T> target : targets) {
            target.update(argument);
        }
    }
}