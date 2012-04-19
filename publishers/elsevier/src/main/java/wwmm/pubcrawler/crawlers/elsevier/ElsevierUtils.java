/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.model.Journal;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class ElsevierUtils {

    private static final String URI_TEMPLATE = "http://www.sciencedirect.com/science/publication?issn=%s&volume=%s&issue=%s";

    public static URI getIssueUrl(final Journal journal, final String volume, final String number) {
        final String u = String.format(URI_TEMPLATE, journal.getAbbreviation(), volume, number);
        return URI.create(u);
    }

}
