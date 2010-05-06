package wwmm.pubcrawler.core;

/**
 * <p>
 * The <code>RscJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the Royal Society of 
 * Chemistry.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public enum RscJournal {
	CHEMCOMM("cc", "Chemical Communications", 1964),
	CRYSTENGCOMM("ce", "CrystEngComm", 1998),
	DALTON_TRANSACTIONS("dt", "Dalton Transactions", 1971),
	GREEN_CHEMISTRY("gc", "Green Chemistry", 1998),
	JOURNAL_OF_MATERIALS_CHEMISTRY("jm", "Journal of Materials Chemistry", 1990),
	JOURNAL_OF_ENVIRONMENTAL_MONITORING("em", "Journal of Environmental Monitoring", 1998),
	NATURAL_PRODUCT_REPORTS("np", "Natural Product Reports", 1983),
	NEW_JOURNAL_OF_CHEMISTRY("nj", "New Journal of Chemistry", 1977),
	ORGANIC_AND_BIOMOLECULAR_CHEMISTRY("ob", "Organic and Biomolecular Chemistry", 2002),
	PCCP("cp", "Physical Chemistry Chemical Physics", 1998);

	private final String abbreviation;
	private final String fullTitle;
	private final int volumeOffset;

	RscJournal(String abbreviation, String fullTitle, int volumeOffset) {
		this.abbreviation = abbreviation;
		this.fullTitle = fullTitle;
		this.volumeOffset = volumeOffset;
	}

	/**
	 * <p>
	 * Gets the complete journal title.
	 * </p>
	 * 
	 * @return String of the complete journal title.
	 * 
	 */
	public String getFullTitle() {
		return this.fullTitle;
	}

	/**
	 * <p>
	 * Gets the journal abbreviation (as used by the publisher
	 * on their website).
	 * </p>
	 * 
	 * @return String of the journal abbreviation.
	 * 
	 */
	public String getAbbreviation() {
		return this.abbreviation;
	}
	
	/**
	 * <p>
	 * Gets an <code></code> which describes the relationship 
	 * between a journals year and volume i.e. if you know 
	 * the journal year is 2009 and the <code>volumeOffset</code> 
	 * is 2000, then the volume of the journal in 2009 is 9. 
	 * Magic.
	 * </p>
	 *
	 * @return int
	 * 
	 */
	public int getVolumeOffset() {
		return this.volumeOffset;
	}
	
	public int getVolumeFromYear(int year) {
		return year - this.volumeOffset;
	}
	
	public int getYearFromVolume(int volume) {
		return this.volumeOffset + volume;
	}
}