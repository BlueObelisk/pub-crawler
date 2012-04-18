package wwmm.pubcrawler.crawlers.acta.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class IucrIssueListVolumeNumberParser {

    private static final Logger LOG = LoggerFactory.getLogger(IucrIssueListVolumeNumberParser.class);
    
    private static final IucrIssueListVolumeNumberParser SINGLETON_INSTANCE = new IucrIssueListVolumeNumberParser();

    private static final Pattern P_VOLUME = Pattern.compile("Volume\\s+[A-Z]?(\\d+)");
    private static final Pattern P_PART_NUMBER = Pattern.compile("Part (\\d+), Number (\\d+)");
    private static final Pattern P_PART_SUPPLEMENT = Pattern.compile("((?<=Part )[a-z]?\\d+|Supplement)");

    public static IucrIssueListVolumeNumberParser getInstance() {
        return SINGLETON_INSTANCE;
    }

    public String getVolume(final String node) {
        Matcher matcher = P_VOLUME.matcher(node);
        if (matcher.find()) {
            return matcher.group(1);
        }
        LOG.warn("Unable to locate volume: '" + node + "'");
        return null;
    }

    public String getNumber(final String node) {
        Matcher partNumberMatcher = P_PART_NUMBER.matcher(node);
        if (partNumberMatcher.find()) {
            return partNumberMatcher.group(1) + '-' + partNumberMatcher.group(2);
        }

        Matcher matcher = P_PART_SUPPLEMENT.matcher(node);
        if (matcher.find()) {
            return matcher.group(1);
        }

        LOG.warn("Unable to locate issue: '" + node + "'");
        return null;
    }

}
