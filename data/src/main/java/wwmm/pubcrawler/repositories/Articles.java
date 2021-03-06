package wwmm.pubcrawler.repositories;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sam Adams
 */
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Qualifier
public @interface Articles {
}
