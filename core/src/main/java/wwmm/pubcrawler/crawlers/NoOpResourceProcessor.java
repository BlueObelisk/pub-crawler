package wwmm.pubcrawler.crawlers;

/**
 * @author Sam Adams
 */
public class NoOpResourceProcessor<R, D> implements ResourceProcessor<R, D> {

    @Override
    public void process(final String taskId, final D task, final R resource) {
    }

    public static <R, D> ResourceProcessor<R, D> getInstance() {
        return new NoOpResourceProcessor<R, D>();
    }
}
