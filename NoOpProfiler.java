import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

class FakeClock extends Clock {

    private Instant now;

    private ZoneId zoneId;

    public FakeClock() {

        this(
                Instant.now(),
                ZoneId.systemDefault());
    }

    public FakeClock(
            Instant now,
            ZoneId zoneId) {

        this.now =
                Objects.requireNonNull(now);

        this.zoneId =
                Objects.requireNonNull(zoneId);
    }

    @Override
    public ZoneId getZone() {

        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {

        return new FakeClock(now, zone);
    }

    @Override
    public Instant instant() {

        return now;
    }

    /*
     * Increment fake time
     */
    public void tick(Duration duration) {

        now =
                now.plus(
                        Objects.requireNonNull(duration));
    }

    /*
     * Set fake time
     */
    public void setTime(Instant instant) {

        this.now =
                Objects.requireNonNull(instant);
    }

    /*
     * Set fake timezone
     */
    public void setZone(ZoneId zoneId) {

        this.zoneId =
                Objects.requireNonNull(zoneId);
    }
}

public class Main {

    public static void main(String[] args) {

        FakeClock clock =
                new FakeClock();

        System.out.println(
                "Current Time: "
                        + clock.instant());

        clock.tick(Duration.ofHours(2));

        System.out.println(
                "After 2 Hours: "
                        + clock.instant());

        clock.setTime(
                Instant.parse(
                        "2026-01-01T10:00:00Z"));

        System.out.println(
                "Updated Time: "
                        + clock.instant());

        clock.setZone(
                ZoneId.of("Asia/Kolkata"));

        System.out.println(
                "Zone: "
                        + clock.getZone());
    }
}