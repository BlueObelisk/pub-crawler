package wwmm.pubcrawler.controller;

/**
 * @author Sam Adams
 */
public interface Processor<Resource> {

    void process(Resource resource);

}
