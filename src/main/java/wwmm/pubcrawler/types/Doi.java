/*
 * Copyright 2010-2011 Nick Day, Sam Adams
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
 */
package wwmm.pubcrawler.types;

import java.net.URI;

/**
 * <p>
 * The <code>DOI</code> class provides a representation of a Digital Object
 * Identifier (DOI).  It is a simple wrapper class based around a URI.
 * </p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2.0
 *
 */
public class Doi {

    public static final URI DOI_SITE_URL = URI.create("http://dx.doi.org/");

    private final String value;

    public Doi(String value) {
        String doi = extractDoi(value);
        validate(doi);
        this.value = doi;
    }

    public Doi(URI uri) {
        String url = uri.toString();
        if (!url .toLowerCase().startsWith(DOI_SITE_URL.toString())) {
            throw new InvalidDoiException("URI must start "+DOI_SITE_URL + "["+uri+"]");
        }
        String doi = url.substring(DOI_SITE_URL.toString().length());
        validate(doi);
        this.value = doi;
    }

    private String extractDoi(String value) {
        if (value.toLowerCase().startsWith("doi:")) {
            return value.substring(4).trim();
        }
        if (value.toLowerCase().startsWith("http://dx.doi.org/")) {
            return value.substring(18);
        }
        return value;
    }

    /**
     * <p>Make sure that the provided <code>URI</code> is a valid DOI.</p>
     *
     * @throws RuntimeException if the provided URI is not
     * a valid DOI.
     *
     * @param doi
     */
    private void validate(String doi) {
        if (!doi.matches("10\\.\\d+/\\S+")) {
            throw new InvalidDoiException("Invalid DOI: "+doi);
        }
    }



    public String getValue() {
        return value;
    }

    /**
     * <p>Get the DOI's URI.</p>
     * @return the URI for the DOI.
     */
    public URI getUrl() {
        return DOI_SITE_URL.resolve(getValue());
    }

    /**
     * <p>Simple method to get the DOIs URI string.</p>
     */
    @Override
    public String toString() {
        return "DOI:"+value;
    }

    /**
     * <p>Method to override equals in <code>Object</code> class.</p>
     *
     * @return true if the supplied Object is a DOI with the same
     * URI as this, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Doi) {
            Doi other = (Doi) o;
            return getValue().equals(other.getValue());
        }
        return false;
    }

    /**
     * <p>Overrides <code>hashCode</code> method in <code>Object</code> class.
     * Implemented because I have overriden <code>equals(Object)</code>.</p>
     */
    @Override
    public int hashCode() {
        return 31 * getValue().hashCode();
    }

    public String getPrefix() {
        int i = getValue().indexOf('/');
        return getValue().substring(0, i);
    }

    public String getSuffix() {
        int i = getValue().indexOf('/');
        return getValue().substring(i+1);
    }

}
