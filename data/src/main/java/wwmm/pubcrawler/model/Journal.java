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

package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class Journal extends PubcrawlerObject<JournalId> {

    private String abbreviation;
    private String title;
    private List<String> previousTitles;
    private List<IssueLink> issues = Collections.emptyList();

    private URI url;

    public Journal() { }

    public Journal(final PublisherId publisherId, final String abbreviation, final String title, final Integer offset) {
        setId(new JournalId(publisherId, abbreviation));
		this.abbreviation = abbreviation;
		this.title = title;
	}

    public Journal(final PublisherId publisherId, final String abbreviation, final String title) {
        setId(new JournalId(publisherId, abbreviation));
		this.abbreviation = abbreviation;
		this.title = title;
	}

    public Journal(final String abbreviation, final String title) {
		this.abbreviation = abbreviation;
		this.title = title;
	}

    public String getTitle() {
		return this.title;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

    @Override
    public URI getUrl() {
        return url;
    }

    @Override
    public void setUrl(final URI url) {
        this.url = url;
    }

    public String getPublisherRef() {
        return getId().getPublisherPart();
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
