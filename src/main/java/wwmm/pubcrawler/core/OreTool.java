package wwmm.pubcrawler.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dspace.foresite.AggregatedResource;
import org.dspace.foresite.Aggregation;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.OREResource;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.Predicate;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;

/**
 * <p>
 * Provides a method of converting the information in an 
 * <code>ArticleDetails</code> object into an ORE description.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 */
public class OreTool {

	private ArticleDetails ad;
	private String remUrl;

	private static final String BIBO_NS = "http://purl.org/ontology/bibo/";
	private static final String DC_NS = "http://purl.org/dc/elements/1.1/";
	private static final String DCTERMS_NS = "http://purl.org/dc/terms/";
	private static final String PRISM_NS = "http://prismstandard.org/namespaces/1.2/basic/";
	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	private static final Logger LOG = Logger.getLogger(OreTool.class);

	public OreTool(String remUrl, ArticleDetails ad) {
		this.ad = ad;
		if (ad == null) {
			throw new IllegalStateException("ArticleDetails passed to the constructor is null.");
		}
		this.remUrl = remUrl;
		alterRemUrl();
	}

	/**
	 * <p>
	 * If the provided REM URL is of the file protocol, then for it to be
	 * legal in FORESITE it must be file:/// rather than file:/ (which is 
	 * output by File.toURI()). This method simply changes any URLs of the 
	 * latter to the former.
	 * </p>
	 * 
	 */
	private void alterRemUrl() {
		if (remUrl.startsWith("file:/") && !remUrl.startsWith("file:///")) {
			remUrl = "file:///"+remUrl.substring(6);
		} else if (remUrl.startsWith("file://") && !remUrl.startsWith("file:///")) {
			remUrl = "file:///"+remUrl.substring(7);
		}
	}

	/**
	 * <p>
	 * Gets the ORE RDF XML of the <code>ArticleDetails</code>
	 * provided in the constructor.
	 * </p>
	 * 
	 * @return an XML String containing an ORE description of
	 * the provided <code>ArticleDetails</code>.  Will return null
	 * if an exception was thrown during ORE creation.
	 *  
	 */
	public String getORE() {
		try {
			URI remUri = new URI(remUrl);
			URI aggregationUri = new URI(remUrl+"#aggregation");
			Aggregation agg = OREFactory.createAggregation(aggregationUri);
			ResourceMap rem = agg.createResourceMap(remUri);
			Date dateNow = new Date();

			// add triples about the Resource Map
			rem.setModified(dateNow);
			rem.setCreated(dateNow);
			rem.setRights("http://www.opendatacommons.org/licenses/pddl/1.0/");

			// add triples about the aggregation
			agg.setModified(dateNow);
			agg.setCreated(dateNow);
			List<URI> similarToList = new ArrayList<URI>(1);
			similarToList.add(new URI(createDoiInfoString(ad.getDoi())));
			agg.setSimilarTo(similarToList);
			String title = ad.getTitle();
			if (title != null) {
				addTriple(agg, aggregationUri, createPredicate(DC_NS+"title"), title);
			}
			addTriple(agg, aggregationUri, createPredicate(DC_NS+"type"), "j.0:AcademicArticle");
			String authors = ad.getAuthors();
			if (authors != null) {
				addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"authorList"), authors);
			}
			//String journalUuid = Utils.getRandomUuidString();
			//URI journalUri = new URI(journalUuid);
			//addTriple(agg, aggregationUri, createPredicate(DC_NS+"isPartOf"), journalUri);
			ArticleReference reference = ad.getReference();
			if (reference != null) {
				String volume = reference.getVolume();
				if (volume != null) {
					addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"volume"), volume);
				}
				String number = reference.getNumber();
				if (number != null) {
					addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"issue"), number);
				}
				String pages = reference.getPages();
				if (pages != null) {
					addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"pages"), pages);
				}
				String year = reference.getYear();
				if (year != null) {
					addTriple(agg, aggregationUri, createPredicate(DCTERMS_NS+"issued"), year);
				}
				String journalTitle = reference.getJournalTitle();
				if (year != null) {
					addTriple(agg, aggregationUri, createPredicate(PRISM_NS+"publicationName"), journalTitle);
				}
				String refString = reference.getRefString();
				if (year != null) {
					addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"shortDescription"), refString);
				}
			}
			// add triples about aggregated resources
			// --1st the triples about the full-text
			List<FullTextResourceDetails> ftrds = ad.getFullTextResources();
			for (FullTextResourceDetails ftrd : ftrds) {
				URI fullTextUri = ftrd.getURI();
				AggregatedResource fullTextAr = OREFactory.createAggregatedResource(fullTextUri);
				agg.addAggregatedResource(fullTextAr);
				addTriple(agg, fullTextUri, createPredicate(DC_NS+"format"), ftrd.getContentType());
				addTriple(agg, fullTextUri, createPredicate(DC_NS+"language"), "en");
				// FIXME - need to find a better way of describing this
				addTriple(agg, fullTextUri, createPredicate(DC_NS+"type"), "FULL_TEXT");
			}
			// --2nd the triples about supplementary files
			for (SupplementaryResourceDetails sfd : ad.getSupplementaryResources()) {
				URI sfdUri = new URI(sfd.getUriString());
				AggregatedResource ar = OREFactory.createAggregatedResource(sfdUri);
				agg.addAggregatedResource(ar);
				String contentType = sfd.getContentType();
				if (contentType != null) {
					addTriple(ar, sfdUri, createPredicate(DC_NS+"format"), contentType);
				}
				// FIXME - need to find a better way of describing this
				addTriple(agg, sfdUri, createPredicate(DC_NS+"type"), "SUPPLEMENTARY_FILE");
				addTriple(agg, sfdUri, createPredicate(DC_NS+"language"), "en");
			}

			// add triples about the human splash page
			URI doiUri = new URI(ad.getDoi().toString());
			AggregatedResource doiAr = OREFactory.createAggregatedResource(doiUri);
			agg.addAggregatedResource(doiAr);
			addTriple(agg, doiUri, createPredicate(RDF_NS+"type"), "info:eu-repo/semantics/humanStartPage");
			addTriple(agg, doiUri, createPredicate(DC_NS+"language"), "en");
			addTriple(agg, doiUri, createPredicate(DC_NS+"format"), "text/html");

			ORESerialiser serial = ORESerialiserFactory.getInstance("RDF/XML");
			ResourceMapDocument doc = serial.serialise(rem);
			return doc.toString();
		} catch (Exception e) {
			LOG.warn("Problem generating ORE document: "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>
	 * Adds a triple to the provided <code>resource</code> using 
	 * the subject, predicate and object provided.
	 * </p>
	 * 
	 * @param resource you want to add the triple to.
	 * @param subject of the triple to be added.
	 * @param predicate of the triple to be added. 
	 * @param object of the triple to be added.
	 * 
	 * @throws OREException if something bad happens.
	 */
	private void addTriple(OREResource resource, URI subject, Predicate predicate, Object object) throws OREException {
		resource.addTriple(OREFactory.createTriple(subject, predicate, object));
	}

	/**
	 * <p>
	 * Create a FORESITE predicate using the provided URL.
	 * </p>
	 * 
	 * @param url you want to create the Predicate with.
	 * 
	 * @return a FORESITE predicate based on the provided URL.
	 * 
	 * @throws URISyntaxException - if the provided String does
	 * not represent a valid URI.
	 */
	private Predicate createPredicate(String url) throws URISyntaxException {
		Predicate predicate = new Predicate();
		predicate.setURI(new URI(url));
		return predicate;
	}

	/**
	 * <p>
	 * Create the protocol-less String representation of the
	 * DOI.
	 * </p>
	 * 
	 * @param doi that you want to create the String from.
	 * 
	 * @return a protocol-less String representation of the
	 * provided DOI.
	 */
	private String createDoiInfoString(DOI doi) {
		return "info:doi/"+doi.getPostfix();
	}

	/**
	 * <p>
	 * Main method is meant for demonstration purposes only.
	 * Does not require any arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		DOI doi = new DOI("http://dx.doi.org/10.1107/S1600536809016109");
		ActaArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDetails details = crawler.getDetails();
		OreTool ot = new OreTool("file:/wwmm.ch.cam.ac.uk/sdfa", details);
		System.out.println(ot.getORE());
	}

}
