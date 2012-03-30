package wwmm.pubcrawler.model.id;

import org.apache.commons.lang.Validate;

public abstract class Id<T extends Id> implements Comparable<T> {

    private final String uid;
    private String value;

    protected Id(String uid) {
        Validate.notEmpty(uid);
        this.uid = uid;
    }

    protected Id(Id id, String uid) {
        if (uid.contains("/") || uid.length() == 0) {
            throw new IllegalArgumentException("Bad ID: ["+ uid +"]");
        }
        this.uid = id.getUid() + '/' + uid;
    }

    protected Id(Id id, String... parts) {
        Validate.notEmpty(parts);
        StringBuilder value = new StringBuilder();
        for (final String part : parts) {
            if (part.contains("/") || part.length() == 0) {
                throw new IllegalArgumentException("Bad ID: "+part);
            }
            if (value.length() != 0) {
                value.append('/');
            }
            value.append(part);
        }
        this.value = value.toString();
        this.uid = id.getUid() + '/' + value.toString();
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null) {
            if (this.getClass() == o.getClass()) {
                Id other = (Id) o;
                return getUid().equals(other.getUid());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUid().hashCode();
    }

    public int compareTo(T o) {
        return getUid().compareTo(o.getUid());
    }

    @Override
    public String toString() {
        return "{"+ uid +"}";
    }

    protected String getValue() {
        return value;
    }

}