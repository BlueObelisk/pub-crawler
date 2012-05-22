package wwmm.pubcrawler.tasks;

/**
 * @author Sam Adams
 */
public interface TaskSpecification<T> {

    Class<? extends TaskRunner<T>> getRunnerClass();

    Marshaller<T> getDataMarshaller();

}
