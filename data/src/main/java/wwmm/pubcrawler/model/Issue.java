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

    private IssueLink previousIssueLink;
    private String year;
    private String volume;
    private String number;
    private String date;
    private String journalTitle;
    private long sequence;

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


    public IssueLink getPreviousIssue() {
        return previousIssueLink;
    }

    public void setPreviousIssue(final IssueLink previousIssue) {
        this.previousIssueLink = previousIssue;
    }


    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }


    public String getVolume() {
        return volume;
    }

    public void setVolume(final String volume) {
        this.volume = volume;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }


    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(final String journalTitle) {
        this.journalTitle = journalTitle;
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
        return sequence;
    }

    public void setSequence(final long sequence) {
        this.sequence = sequence;
    }
}
