package wwmm.pubcrawler.model.id;

import org.apache.commons.lang.Validate;

public abstract class Id<T extends Id> implements Comparable<T> {

    private final String value;

    protected Id(String value) {
        Validate.notEmpty(value);
        this.value = value;
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
                return getValue().equals(((Id)o).getValue());
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