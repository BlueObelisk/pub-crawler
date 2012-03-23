package wwmm.pubcrawler.v2.inject.crawlers;

import com.google.inject.AbstractModule;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;

/**
 * @author Sam Adams
 */
public class ElsevierCrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IssueTocParserFactory.class).to(ElsevierIssueTocParserFactory.class);
    }

}
