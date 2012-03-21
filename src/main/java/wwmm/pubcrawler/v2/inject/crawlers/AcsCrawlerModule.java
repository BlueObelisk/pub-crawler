package wwmm.pubcrawler.v2.inject.crawlers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsParserFactory;

/**
 * @author Sam Adams
 */
public class AcsCrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(AcsParserFactory.class));
    }

    public static void main(final String[] args) {
        Injector injector = Guice.createInjector(new AcsCrawlerModule());
    }

}
