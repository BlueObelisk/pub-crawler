
PUB-CRAWLER v. 0.2.6   (C) Copyright 2010 Nick Day

1. ABOUT

The aim of pub-crawler is to provide a set web-crawlers for extracting bibliographic data 
from published journal articles.  At present pub-crawler is focused on extracting from
chemistry journals, though the base functionality is generic. 

pub-crawler currently contains crawlers for the following publishers:

* American Chemical Society 
* Acta Crystallographica
* Royal Society of Chemistry
* Nature
* Chemical Society of Japan


2. USAGE

For each publisher, there is an ArticleCrawler and IssueCrawler found in the 
wwmm.pubcrawler.core package. 

NB. there is example usage of the library in the main methods of each publisher 
article/issue crawler class.


2.1 ARTICLE CRAWLERS

The article crawling is based around DOIs.  The article crawlers accept a DOI which is 
followed to find the article abstract page.  From this page various pieces of bibliographic 
info for the article are extracted and returned:

* title
* authors
* the reference (including year, volume, issue number and pages)
* description of any full-text resources (including URL, link text and content-type (from 
   the HTTP header))
* description of any supplementary resources (including URL, link text and content-type 
   (from the HTTP header))


2.2 ISSUE CRAWLERS

When initialising an issue crawler, the specific journal to be scraped is specified, and
then public methods for the following are provided:

* getting the year and issue number of the latest journal issue.
* getting the DOIs for a specific issue
* getting the DOIs for the current issue
* getting the bibliographic info for articles in a specific issue 
* getting the bibliographic info for articles in the current issue

Again the best explanation of how to use the code is available in the crawler main methods.