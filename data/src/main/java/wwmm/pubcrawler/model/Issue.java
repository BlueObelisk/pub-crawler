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

/**
 * @author Sam Adams
 */
public class Issue extends PubcrawlerObject<IssueId> {

    public static final String NULL_NUMBER = "-";

    private static final String PREVIOUS_ISSUE = "previousIssue";
    private static final String YEAR = "year";
    private static final String VOLUME = "volume";
    private static final String NUMBER = "number";
    private static final String DATE = "date";
    private static final String JOURNAL_TITLE = "journalTitle";
    private static final String SEQUENCE = "sequence";

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


    public Issue getPreviousIssue() {
        return (Issue) get(PREVIOUS_ISSUE);
    }

    public void setPreviousIssue(final Issue previousIssue) {
        put(PREVIOUS_ISSUE, previousIssue);
    }


    public String getYear() {
        return getString(YEAR);
    }

    public void setYear(final String year) {
        put(YEAR, year);
    }


    public String getVolume() {
        return getString(VOLUME);
    }

    public void setVolume(final String volume) {
        put(VOLUME, volume);
    }


    public String getNumber() {
        return getString(NUMBER);
    }

    public void setNumber(final String number) {
        put(NUMBER, number);
    }


    public String getDate() {
        return getString(DATE);
    }

    public void setDate(final String date) {
        put(DATE, date);
    }

    public String getJournalTitle() {
        return getString(JOURNAL_TITLE);
    }

    public void setJournalTitle(final String journalTitle) {
        put(JOURNAL_TITLE, journalTitle);
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

    public long getSequence() {
        return getLong(SEQUENCE);
    }

    public void setSequence(final long sequence) {
        put(SEQUENCE, sequence);
    }
}
