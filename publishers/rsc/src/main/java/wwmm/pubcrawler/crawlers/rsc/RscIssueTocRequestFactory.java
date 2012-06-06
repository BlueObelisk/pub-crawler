package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.http.RequestFactory;

import javax.inject.Singleton;

import static java.lang.String.format;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocRequestFactory implements RequestFactory<RscIssueTocCrawlTaskData, RscIssueTocRequest> {

    private static final String FILE_ID = "%s/%s";

    @Override
    public RscIssueTocRequest createFetchTask(final String taskId, final RscIssueTocCrawlTaskData data) {
        return new RscIssueTocRequest(data.getUrl(), format(FILE_ID, taskId, data.getFileId()), data.getMaxAge(), data.getReferrer(), data.getJournal(), data.getJournalName(), data.getIssue());
    }
}
