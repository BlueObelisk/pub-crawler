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

import wwmm.pubcrawler.model.id.IssueId;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class Issue extends PubcrawlerObject<IssueId> {

    public static final String NULL_NUMBER = "-";

    private transient boolean current;

    public Issue() {
    }

    public Issue(final IssueId issueId, final String journalTitle, final String volume, final String number, final String year, final URI url) {
        setId(issueId);
        setJournalTitle(journalTitle);
        setVolume(volume);
        setNumber(number);
        setYear(year);
        if (url != null) {
            setUrl(url);
        }
    }

    @Override
    protected IssueId createId(final String id) {
        return new IssueId(id);
    }


    public List<Article> getArticles() {
        return (List<Article>) get("articles");
    }

    public void setArticles(final List<Article> articles) {
        put("articles", articles);
    }


    public Issue getPreviousIssue() {
        return (Issue) get("previousIssue");
    }

    public void setPreviousIssue(final Issue previousIssue) {
        put("previousIssue", previousIssue);
    }


    public String getYear() {
        return getString("year");
    }

    public void setYear(final String year) {
        put("year", year);
    }


    public String getVolume() {
        return getString("volume");
    }

    public void setVolume(final String volume) {
        put("volume", volume);
    }


    public String getNumber() {
        return getString("number");
    }

    public void setNumber(final String number) {
        put("number", number);
    }


    public String getDate() {
        return getString("date");
    }

    public void setDate(final String date) {
        put("date", date);
    }

    public String getJournalTitle() {
        return getString("journalTitle");
    }

    public void setJournalTitle(final String journalTitle) {
        put("journalTitle", journalTitle);
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(final boolean current) {
        this.current = current;
    }

    
    public String getJournalRef() {
        return getId().getPublisherPart() + '/' + getId().getJournalPart();
    }

    public String getNextIssueId() {
        return null;
    }

    public String getPreviousIssueId() {
        return null;
    }
}