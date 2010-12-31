/*******************************************************************************
 * Copyright 2010 Nick Day
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package wwmm.crawler.core.types;

import org.junit.Test;

import wwmm.pubcrawler.core.types.Doi;
import wwmm.pubcrawler.core.InvalidDoiException;

import java.net.URI;

import static org.junit.Assert.*;

public class DoiTest {

    private static final String INVALID_DOI = "this_is_not_a_doi";

    private static final String VALID_DOI1 = "10.1039/b815603d";
    private static final String VALUD_DOI1_URL = "http://dx.doi.org/"+VALID_DOI1;
    private static final URI VALID_DOI1_URI = URI.create(VALUD_DOI1_URL);

    private static final String VALID_DOI2 = "10.1021/cg100078b";

    private static final String INVALID_DOI_URL = "http://www.google.com";
    private static final URI INVALID_DOI_HOST_URI = URI.create(INVALID_DOI_URL);

    private static final URI INVALID_DOI_URI_NO_DOI = URI.create("http://dx.doi.org/");
    private static final URI INVALID_DOI_URI_BAD_DOI = URI.create("http://dx.doi.org/foo");

    @Test
    public void testStringConstructor() {
        Doi doi = new Doi(VALID_DOI1);
        assertEquals(VALID_DOI1,  doi.getValue());
    }

    @Test
    public void testStringConstructorPrefixed() {
        Doi doi = new Doi("DOI:"+VALID_DOI1);
        assertEquals(VALID_DOI1,  doi.getValue());
    }

    @Test
    public void testStringConstructorPrefixedSpace() {
        Doi doi = new Doi("DOI: "+VALID_DOI1);
        assertEquals(VALID_DOI1,  doi.getValue());
    }

    @Test
    public void testStringConstructorPrefixedLowercase() {
        Doi doi = new Doi("doi:"+VALID_DOI1);
        assertEquals(VALID_DOI1,  doi.getValue());
    }

    @Test
    public void testStringConstructorPrefixedLowercaseSpace() {
        Doi doi = new Doi("doi: "+VALID_DOI1);
        assertEquals(VALID_DOI1,  doi.getValue());
    }

    @Test(expected = InvalidDoiException.class)
    public void testStringConstructorInvalidDoi() {
        Doi doi = new Doi(INVALID_DOI);
        assertNotNull(doi);
    }

	@Test
	public void testStringConstructorUrl() {
        Doi doi = new Doi(VALUD_DOI1_URL);
        assertEquals(VALID_DOI1, doi.getValue());
    }

    @Test(expected = InvalidDoiException.class)
    public void testStringConstructorInvalidUrl() {
        Doi doi = new Doi(INVALID_DOI_URL);
        assertNotNull(doi);
    }

    @Test(expected = InvalidDoiException.class)
    public void testStringConstructorInvalidUrlNoSuffix1() {
        Doi doi = new Doi("http://dx.doi.org");
        assertNotNull(doi);
    }

    @Test(expected = InvalidDoiException.class)
    public void testStringConstructorInvalidUrlNoSuffix2() {
        Doi doi = new Doi("http://dx.doi.org/");
        assertNotNull(doi);
    }

    @Test(expected = InvalidDoiException.class)
    public void testStringConstructorInvalidUrlBadSuffix() {
        Doi doi = new Doi("http://dx.doi.org/foo");
        assertNotNull(doi);
    }
	

	@Test
	public void testUriConstructor() {
        Doi doi = new Doi(VALID_DOI1_URI);
        assertEquals(VALID_DOI1, doi.getValue());
	}

    @Test(expected = InvalidDoiException.class)
	public void testUriConstructorInvalidHost() {
        Doi doi = new Doi(INVALID_DOI_HOST_URI);
        assertNotNull(doi);
	}

    @Test(expected = InvalidDoiException.class)
	public void testUriConstructorNoDoi() {
        Doi doi = new Doi(INVALID_DOI_URI_NO_DOI);
        assertNotNull(doi);
	}

    @Test(expected = InvalidDoiException.class)
	public void testUriConstructorBadDoi() {
        Doi doi = new Doi(INVALID_DOI_URI_BAD_DOI);
        assertNotNull(doi);
	}
    
	@Test
	public void testGetURI() {
		Doi doi1 = new Doi(VALID_DOI1);
        assertEquals(VALID_DOI1_URI, doi1.getUrl());
	}

	@Test
	public void testToString() {
		Doi doi1 = new Doi(VALID_DOI1);
		assertEquals("DOI:"+VALID_DOI1, doi1.toString());
	}

    @Test
    public void testEqual() {
        Doi doi1 = new Doi(VALID_DOI1);
        Doi doi2 = new Doi(VALID_DOI1);
        assertTrue(doi1.equals(doi2));
        assertTrue(doi2.equals(doi1));
    }

    @Test
    public void testSameHashcode() {
        Doi doi1 = new Doi(VALID_DOI1);
        Doi doi2 = new Doi(VALID_DOI1);
        assertTrue(doi1.hashCode() == doi2.hashCode());
    }

    @Test
    public void testNotEqual() {
        Doi doi1 = new Doi(VALID_DOI1);
        Doi doi2 = new Doi(VALID_DOI2);
        assertFalse(doi1.equals(doi2));
        assertFalse(doi2.equals(doi1));
    }

}
