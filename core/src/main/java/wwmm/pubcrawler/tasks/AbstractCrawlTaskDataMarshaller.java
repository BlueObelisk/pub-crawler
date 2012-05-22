package wwmm.pubcrawler.tasks;

/**
 * @author Sam Adams
 */
public abstract class AbstractCrawlTaskDataMarshaller {

    protected static final String URL = "url";
    protected static final String FILE_ID = "fileId";
    protected static final String MAX_AGE = "maxAge";

    protected void marshallCommonFields(final HttpCrawlTaskData data, final DataSink target) {
        target.writeUri(URL, data.getUrl());
        target.writeString(FILE_ID, data.getFileId());
        target.writeDuration(MAX_AGE, data.getMaxAge());
    }
}
