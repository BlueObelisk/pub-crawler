package wwmm.pubcrawler.tasks;

/**
 * @author Sam Adams
 */
public interface Marshaller<T> {

    void marshall(T data, DataSink target);

    T unmarshall(DataSource source);

}
