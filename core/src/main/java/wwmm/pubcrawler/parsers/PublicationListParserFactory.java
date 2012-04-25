package wwmm.pubcrawler.parsers;

/**
 * @author Sam Adams
 */
public interface PublicationListParserFactory<T> {
    
    PublicationListParser createPublicationListParser(T resource);

}
