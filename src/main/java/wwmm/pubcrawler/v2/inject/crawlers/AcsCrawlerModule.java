package wwmm.pubcrawler.v2.inject.crawlers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Sam Adams
 */
public class AcsCrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    public static void main(final String[] args) {
        Injector injector = Guice.createInjector(new AcsCrawlerModule());
    }

}
