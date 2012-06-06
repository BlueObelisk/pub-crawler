package wwmm.pubcrawler.http;

/**
 * @author Sam Adams
 */
public interface RequestFactory<Data, Request> {

    Request createFetchTask(String taskId, Data data);

}
