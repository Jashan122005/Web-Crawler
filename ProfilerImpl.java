import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* ===================== QUALIFIER ===================== */

@Retention(RetentionPolicy.RUNTIME)
@interface Qualifier {
}

/* ===================== PARSE DEADLINE ===================== */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface ParseDeadline {
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "ParseDeadline annotation loaded successfully.");
    }
}