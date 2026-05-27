import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;

/* ===================== PROFILED ANNOTATION ===================== */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Profiled {
}

/* ===================== PROFILER INTERFACE ===================== */

interface Profiler {

    <T> T wrap(Class<T> klass, T delegate);

    void writeData(Writer writer) throws Exception;
}

/* ===================== FAKE CLOCK ===================== */

class FakeClock extends Clock {

    private Instant now = Instant.now();

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return now;
    }

    public void tick(Duration duration) {
        now = now.plus(duration);
    }
}

/* ===================== PROFILER IMPLEMENTATION ===================== */

class ProfilerImpl implements Profiler {

    private final Clock clock;

    private final Map<String, Duration> data =
            new LinkedHashMap<>();

    ProfilerImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T wrap(Class<T> klass, T delegate) {

        boolean found = false;

        for (Method method : klass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Profiled.class)) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                    "No @Profiled methods found");
        }

        return (T) Proxy.newProxyInstance(
                klass.getClassLoader(),
                new Class[]{klass},
                (proxy, method, args) -> {

                    Method actualMethod =
                            delegate.getClass()
                                    .getMethod(
                                            method.getName(),
                                            method.getParameterTypes());

                    boolean profiled =
                            method.isAnnotationPresent(
                                    Profiled.class);

                    if (!profiled) {
                        return actualMethod.invoke(delegate, args);
                    }

                    Instant start =
                            clock.instant();

                    try {

                        return actualMethod.invoke(
                                delegate,
                                args);

                    } catch (InvocationTargetException e) {

                        throw e.getCause();

                    } finally {

                        Instant end =
                                clock.instant();

                        Duration duration =
                                Duration.between(start, end);

                        String key =
                                delegate.getClass().getName()
                                        + "#"
                                        + method.getName();

                        data.put(
                                key,
                                data.getOrDefault(
                                                key,
                                                Duration.ZERO)
                                        .plus(duration));
                    }
                });
    }

    @Override
    public void writeData(Writer writer)
            throws Exception {

        for (Map.Entry<String, Duration> entry
                : data.entrySet()) {

            Duration d = entry.getValue();

            long minutes = d.toMinutes();

            long seconds = d.minusMinutes(minutes)
                    .getSeconds();

            long millis =
                    d.toMillisPart();

            writer.write(
                    entry.getKey()
                            + " "
                            + minutes
                            + "m "
                            + seconds
                            + "s "
                            + millis
                            + "ms\n");
        }

        writer.flush();
    }
}

/* ===================== CLOSEABLE STRING WRITER ===================== */

class CloseableStringWriter extends StringWriter {

    private boolean closed = false;

    @Override
    public void close() {

        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}

/* ===================== TEST INTERFACES ===================== */

interface ProfiledInterface {

    @Profiled
    String profiled();

    @Profiled
    void throwSomething(Throwable throwable)
            throws Throwable;

    boolean equals(String foo, String bar);
}

interface NonProfiledInterface {
}

/* ===================== IMPLEMENTATIONS ===================== */

class NonProfiledInterfaceImpl
        implements NonProfiledInterface {
}

class ProfiledInterfaceImpl
        implements ProfiledInterface {

    private final FakeClock fakeClock;

    private boolean wasFakeEqualsCalled =
            false;

    ProfiledInterfaceImpl(
            FakeClock fakeClock) {

        this.fakeClock = fakeClock;
    }

    @Override
    public String profiled() {

        fakeClock.tick(
                Duration.ofSeconds(1));

        return "profiled";
    }

    @Override
    public void throwSomething(
            Throwable throwable)
            throws Throwable {

        fakeClock.tick(
                Duration.ofSeconds(1));

        throw throwable;
    }

    @Override
    public boolean equals(
            Object other) {

        return other instanceof ProfiledInterface;
    }

    @Override
    public boolean equals(
            String foo,
            String bar) {

        wasFakeEqualsCalled = true;

        return false;
    }

    public boolean wasFakeEqualsCalled() {
        return wasFakeEqualsCalled;
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args)
            throws Exception {

        FakeClock clock =
                new FakeClock();

        Profiler profiler =
                new ProfilerImpl(clock);

        ProfiledInterfaceImpl delegate =
                new ProfiledInterfaceImpl(clock);

        ProfiledInterface proxy =
                profiler.wrap(
                        ProfiledInterface.class,
                        delegate);

        System.out.println(
                proxy.profiled());

        proxy.profiled();

        proxy.profiled();

        try {

            proxy.throwSomething(
                    new Throwable(
                            "expected exception"));

        } catch (Throwable t) {

            System.out.println(
                    t.getMessage());
        }

        System.out.println(
                proxy.equals("foo", "bar"));

        System.out.println(
                delegate.wasFakeEqualsCalled());

        CloseableStringWriter writer =
                new CloseableStringWriter();

        profiler.writeData(writer);

        System.out.println(
                "\nPROFILE DATA:\n");

        System.out.println(
                writer.toString());
    }
}