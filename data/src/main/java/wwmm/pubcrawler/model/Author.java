package wwmm.pubcrawler.model;

/**
 * @author Sam Adams
 */
public class Author {
    
    private String name;
    private String affiliation;
    private String emailAddress;

    public Author(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Author author = (Author) o;

        if (affiliation != null ? !affiliation.equals(author.affiliation) : author.affiliation != null) return false;
        if (emailAddress != null ? !emailAddress.equals(author.emailAddress) : author.emailAddress != null)
            return false;
        if (name != null ? !name.equals(author.name) : author.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (affiliation != null ? affiliation.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
            "name='" + name + '\'' +
            ", affiliation='" + affiliation + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            '}';
    }
}
