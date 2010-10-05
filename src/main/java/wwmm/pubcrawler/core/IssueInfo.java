package wwmm.pubcrawler.core;

public class IssueInfo {

	public String infoPath;
	/** extract year and issue from URL? */
	/** regex to use */
	public String yearIssueRegex;
	/** matcher groups expected */
	public Integer matcherGroupCount;
	/** group containing year */
	public Integer yearMatcherGroup;
	/** group containing issue */
	public Integer issueMatcherGroup;
	/** construct current issue */
	public String currentIssueHtmlStart;
	public String currentIssueHtmlEnd;
	/** construct issueUrl from address and volume/issue numbers */
	public String issueUrlStart;
	public String issueUrlPreVolumeYear;
	public String issueUrlPreIssue;
	public String issueUrlEnd;
	/** if true use volume numbers/ids else years */
	public boolean useVolume;
	/** */
	public String doiXpath;
	public Integer beheadDoi;
	/** */
	public String issueIdReplaceFrom;
	public String issueIdReplaceTo;
	/** */
	public String yearVolumeReplaceFrom;
	public String yearVolumeReplaceTo;
	public Class articleCrawlerClass;
	
	

}
