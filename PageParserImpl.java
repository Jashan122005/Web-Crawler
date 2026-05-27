import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * CUSTOM QUALIFIER ANNOTATION
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Qualifier {
}

/*
 * IGNORED WORDS ANNOTATION
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface IgnoredWords {
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "IgnoredWords annotation loaded successfully.");
    }
}