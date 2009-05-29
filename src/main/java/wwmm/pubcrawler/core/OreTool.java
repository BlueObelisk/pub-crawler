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
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.Predicate;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;

import wwmm.pubcrawler.Utils;
import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

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
	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	private static final Logger LOG = Logger.getLogger(OreTool.class);

	public OreTool(String remUrl, ArticleDetails ad) {
		this.ad = ad;
		if (ad == null) {
			throw new IllegalStateException("ArticleDetails passed to the constructor is null.");
		}
		this.remUrl = remUrl;
	}

	/**
	 * <p>
	 * Gets the ORE RDF XML of the <code>ArticleDetails</code>
	 * provided in the constructor.
	 * </p>
	 * 
	 * @return an XML String containing an ORE description of
	 * the provided <code>ArticleDetails</code>.
	 * 
	 * @throws URISyntaxException 
	 * @throws OREException 
	 * @throws ORESerialiserException 
	 */
	public String getORE() throws Exception {
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
		similarToList.add(new URI(createDoiString(ad.getDoi())));
		agg.setSimilarTo(similarToList);
		addTriple(agg, aggregationUri, createPredicate(DC_NS+"title"), ad.getTitle());
		addTriple(agg, aggregationUri, createPredicate(DC_NS+"type"), "j.0:AcademicArticle");
		addTriple(agg, aggregationUri, createPredicate(BIBO_NS+"authorList"), ad.getAuthors());
		String journalUuid = Utils.getRandomUuidString();
		URI journalUri = new URI(journalUuid);
		addTriple(agg, aggregationUri, createPredicate(DC_NS+"isPartOf"), journalUri);
		ArticleReference reference = ad.getReference();
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
		// add triples about aggregated resources
		// --1st the triples about the full-text
		List<FullTextResourceDetails> ftrds = ad.getFullTextResources();
		for (FullTextResourceDetails ftrd : ftrds) {
			URI fullTextUri = new URI(ftrd.getURI().getURI());
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
			addTriple(ar, sfdUri, createPredicate(DC_NS+"format"), sfd.getContentType());
			// FIXME - need to find a better way of describing this
			addTriple(agg, sfdUri, createPredicate(DC_NS+"type"), "SUPPLEMENTARY_FILE");
			addTriple(agg, sfdUri, createPredicate(DC_NS+"language"), "en");
		}
		
		// FIXME - figure out how to do this
		// now add a few triples to describe the journal the article is part of
		//addTriple(agg, journalUri, createPredicate(DC_NS+"type"), BIBO_PREFIX+":Journal");
		//addTriple(agg, journalUri, createPredicate("j.0:shortTitle"), reference.getJournalTitle());
		
		URI doiUri = new URI(ad.getDoi().toString());
		AggregatedResource doiAr = OREFactory.createAggregatedResource(doiUri);
		agg.addAggregatedResource(doiAr);
		addTriple(agg, doiUri, createPredicate(RDF_NS+"type"), "info:eu-repo/semantics/humanStartPage");
		addTriple(agg, doiUri, createPredicate(DC_NS+"language"), "en");
		addTriple(agg, doiUri, createPredicate(DC_NS+"format"), "text/html");

		ORESerialiser serial = ORESerialiserFactory.getInstance("RDF/XML");
		ResourceMapDocument doc = serial.serialise(rem);
		return doc.toString();
	}

	private void addTriple(OREResource resource, URI subject, Predicate pred, Object object) throws OREException {
		resource.addTriple(OREFactory.createTriple(subject, pred, object));
	}

	private Predicate createPredicate(String url) throws URISyntaxException {
		Predicate predicate = new Predicate();
		predicate.setURI(new URI(url));
		return predicate;
	}

	private String createDoiString(DOI doi) {
		return "info:doi/"+doi.getPostfix();
	}

}
