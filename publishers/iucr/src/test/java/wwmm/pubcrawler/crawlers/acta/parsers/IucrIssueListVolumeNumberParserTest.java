package wwmm.pubcrawler.crawlers.acta.parsers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class IucrIssueListVolumeNumberParserTest {

    private IucrIssueListVolumeNumberParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new IucrIssueListVolumeNumberParser();
    }

    @Test
    public void testParsePrefixedVolumePart() {
        assertEquals("64", parser.getVolume("Volume D64, Part 5  (pp. 471-610, 1 May 2008) "));
        assertEquals("5", parser.getNumber("Volume D64, Part 5  (pp. 471-610, 1 May 2008) "));
    }

    @Test
    public void testParseUnPrefixedVolumePart() {
        assertEquals("45", parser.getVolume("Volume 45, Part 2  (pp. 157-380, 1 April 2012) "));
        assertEquals("2", parser.getNumber("Volume 45, Part 2  (pp. 157-380, 1 April 2012) "));
    }

    @Test
    public void testParseVolumeSupplement() {
        assertEquals("67", parser.getVolume("Volume A67 Supplement  (pp. C1-C823) "));
        assertEquals("Supplement", parser.getNumber("Volume A67 Supplement  (pp. C1-C823) "));
    }

    @Test
    public void testParseVolumeNumberedSupplement() {
        assertEquals("40", parser.getVolume("Volume 40, Part s1 (pp. s1-s705, 21 April 2007) "));
        assertEquals("s1", parser.getNumber("Volume 40, Part s1 (pp. s1-s705, 21 April 2007) "));
    }

    @Test
    public void testParseVolumeNumberedPart() {
        assertEquals("30", parser.getVolume("Volume 30, Part 5, Number 2 (pp.  569-888, 1 October 1997)"));
        assertEquals("5-2", parser.getNumber("Volume 30, Part 5, Number 2 (pp.  569-888, 1 October 1997)"));
    }

}
