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
package wwmm.pubcrawler.journal.acs;

import static wwmm.pubcrawler.core.CrawlerConstants.ACS_HOMEPAGE_URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.Crawler;
import wwmm.pubcrawler.core.CrawlerRuntimeException;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.Journal;

/**
 * <p>
 * The <code>AcsRssCrawler</code> class provides a method for 
 * crawling RSS feeds published by the American Chemical Society. 
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class AcsRssCrawler extends Crawler {

	private Journal journal;
	private Date lastCrawledDate;

	private static final Logger LOG = Logger.getLogger(AcsRssCrawler.class);
	
	/**
	 * <p>
	 * Creates an instance of the AcsRssCrawler class ready to 
	 * crawl the feed for the provided <code>AcsJournal</code>.
	 * By using this constructor, all articles in the feed will
	 * be returned on executing <code>getNewArticleDetails</code>.
	 * </p>
	 * 
	 * @param journal - the journals RSS feed to be crawled.
	 * 
	 */
	public AcsRssCrawler(Journal journal) {
		this.journal = journal;
	}

	/**
	 * <p>
	 * Creates an instance of the AcsRssCrawler class ready to 
	 * crawl the feed for the provided <code>AcsJournal</code>.
	 * On executing <code>getNewArticleDetails</code>, only those
	 * article included in the feed after the provided 
	 * <code>lastCrawledDate</code> will be returned.
	 * </p>
	 * 
	 * @param journal - the journals RSS feed to be crawled.
	 * 
	 */
	public AcsRssCrawler(Journal journal, Date lastCrawledDate) {
		this.journal = journal;
		this.lastCrawledDate = lastCrawledDate;
	}

	/**
	 * <p>
	 * Returns information about the new articles to be published
	 * in the RSS feed.  If a <code>lastCrawledDate</code> was
	 * provided in the constructor, then only information about
	 * articles published after that date will be returned. If no
	 * <code>lastCrawledDate</code> was provided, then information
	 * about all article in the feed will be returned.
	 * </p>
	 * 
	 * @return a list where each item provides information about a
	 * separate article.
	 * 
	 */
	public List<ArticleDescription> getNewArticleDetails() {
		String feedUrl = createFeedURL();
		Document feedDoc = httpClient.getResourceXML(feedUrl);
		List<Element> entries = getFeedEntries(feedDoc);
		List<ArticleDescription> adList = new ArrayList<ArticleDescription>();
		for (Element entry : entries) {
			Date entryDate = getEntryDate(entry);
			if (needToCrawlArticle(entryDate)) {
				DOI doi = getDOI(entry);
				ArticleDescription ad = new AcsArticleCrawler(doi).getDetails();
				adList.add(ad);
			}
		}
		return adList;
	}
	
	/**
	 * <p>
	 * Gets the article DOI from the RSS entry.
	 * </p>
	 * 
	 * @param entry - the RSS entry that is being crawled.
	 * 
	 * @return DOI for the article described by the RSS entry.
	 * 
	 */
	private DOI getDOI(Element entry) {
		Nodes nds = entry.query("./link");
		if (nds.size() != 1) {
			throw new IllegalStateException("Expected to find 1 link element in this entry, found "+nds.size()+":\n"+entry.toXML());
		}
		String entryLink = ((Element)nds.get(0)).getValue();
		
		Pattern p = Pattern.compile(ACS_HOMEPAGE_URL+"/doi/abs/(10.1021/.{9})\\?.*");
		Matcher matcher = p.matcher(entryLink);
		String doiPostfix = null;
		if (matcher.find() && matcher.groupCount() == 1) {
			doiPostfix = matcher.group(1);
		} else {
			throw new CrawlerRuntimeException("Could not extract DOI from <link> URI, "+
					entryLink.toString()+"element, crawler may need rewriting.");
		}
		String doiStr = DOI.DOI_SITE_URL+"/"+doiPostfix;
		return new DOI(doiStr);
	}
	
	/**
	 * <p>
	 * If a <code>lastCrawledDate</code> was provided in the 
	 * constructor, then then entry for each article is checked
	 * here to make sure it was published afterwards.
	 * </p>
	 * 
	 * @param entryDate - the date of the current RSS entry.
	 * 
	 * @return boolean whether or not the current article needs
	 * to be scraped.
	 * 
	 */
	private boolean needToCrawlArticle(Date entryDate) {
		if (lastCrawledDate == null) {
			return true;
		} else {
			if (entryDate.before(lastCrawledDate)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * <p>
	 * Gets the published date from the provided RSS entry.
	 * </p> 
	 * 
	 * @param entry - the RSS entry to be scraped.
	 * 
	 * @return Date that the article described by the provided 
	 * RSS entry was published.
	 * 
	 */
	private Date getEntryDate(Element entry) {
		Nodes nds = entry.query("./pubDate");
		if (nds.size() != 1) {
			throw new IllegalStateException("Expected to find 1 date element in this entry, found "+nds.size()+":\n"+entry.toXML());
		}
		String dateStr = ((Element)nds.get(0)).getValue();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new CrawlerRuntimeException("Could not parse date string "+dateStr+
					", looks like the crawler may need rewriting...");
		}
		return date;		
	}
	
	/**
	 * <p>
	 * Gets all entries found in the RSS feed being crawled.
	 * </p>
	 * 
	 * @param feedDoc - XML of the RSS feed being crawled.
	 * 
	 * @return list of entries from the crawled RSS feed.
	 * 
	 */
	private List<Element> getFeedEntries(Document feedDoc) {
		Nodes nds = feedDoc.query("./rss/channel/item");
		if (nds.size() == 0) {
			throw new CrawlerRuntimeException("Could not find any entries in RSS feed for "+journal.getFullTitle());
		}
		List<Element> entries = new ArrayList<Element>(nds.size());
		for (int i = 0; i < nds.size(); i++) {
			Element entry = (Element)nds.get(i);
			entries.add(entry);
		}
		return entries;
	}

	/**
	 * <p>
	 * Gets the URI for the RSS feed for the latest published articles
	 * of the provided <code>AcsJournal</code>.
	 * </p>
	 * 
	 * @return the RSS feed <code>URI</code>.
	 * 
	 */
	private String createFeedURL() {
		return ACS_HOMEPAGE_URL+"/action/showFeed?ui=0&mi=r41k3s&ai=54r&jc="
		+journal.getAbbreviation()+"&type=etoc&feed=rss";
	}

	/**
	 * <p>
	 * Main method only for demonstration of class use. Does not require
	 * any arguments.
	 * </p>
	 * 
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		Journal journal = AcsJournalIndex.J_ACS_cgdefu;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
		Date date = sdf.parse("Thu, 12 Feb 2009 12:00:00 GMT");
		AcsRssCrawler acf = new AcsRssCrawler((Journal)journal, date);
		List<ArticleDescription> details = acf.getNewArticleDetails();
		for (ArticleDescription ad : details) {
			System.out.println(ad.toString());
		}
	}
	
}
