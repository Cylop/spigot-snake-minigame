package at.malibu.general.internals.timing;

import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@CommonsLog
public class Timing implements AutoCloseable {

    private final LocalDateTime start = LocalDateTime.now();
    private final String name;

    /**
     * Times a task, will print the result with log level info
     *
     * @param name the name of the task
     */
    public Timing(final @NonNull String name) {
        this.name = name;
    }

    @Override
    public void close() {
        final LocalDateTime end = LocalDateTime.now();
        final Duration duration = Duration.between(this.start, end);
        final String time = LocalTime.MIDNIGHT.plus(duration)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        log.info("Timings: " + this.name + " took " + time);
    }
}