package wwmm.pubcrawler.model.id;

import org.apache.commons.lang.Validate;

public abstract class Id<T extends Id> implements Comparable<T> {

    private final String value;

    protected Id(String value) {
        Validate.notEmpty(value);
        this.value = value;
    }

    protected Id(Id id, String value) {
        if (value.contains("/") || value.length() == 0) {
            throw new IllegalArgumentException("Bad ID: ["+value+"]");
        }
        this.value = id.getValue() + '/' + value;
    }

    protected Id(Id id, String value1, String value2) {
        if (value1.contains("/") || value1.length() == 0) {
            throw new IllegalArgumentException("Bad ID: "+id+"["+value1+"]["+value2+"]");
        }
        if (value2.contains("/") || value2.length() == 0) {
            throw new IllegalArgumentException("Bad ID: "+id+"["+value1+"]["+value2+"]");
        }
        this.value = id.getValue() + '/' + value1 + '/' + value2;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null) {
            if (this.getClass() == o.getClass()) {
                Id other = (Id) o;
                return getValue().equals(other.getValue());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    public int compareTo(T o) {
        return getValue().compareTo(o.getValue());
    }

    @Override
    public String toString() {
        return "{"+value+"}";
    }

}