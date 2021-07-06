package escape;

// Immutable
public final class Hop {

    private final City fromCity;
    private final City toCity;

    public City getToCity() {
        return toCity;
    }

    public City getFromCity() {
        return fromCity;
    }

    public Hop(final City from, final City to) {
        fromCity = from;
        toCity = to;
    }

    private int hashCode = -1;

    @Override
    public int hashCode() {
        if (this.hashCode != -1) {
            return this.hashCode;
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + ((toCity == null) ? 0 : toCity.hashCode());
        result = prime * result + ((fromCity == null) ? 0 : fromCity.hashCode());
        this.hashCode = result;
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Hop other = (Hop) obj;
        if (toCity == null) {
            if (other.toCity != null)
                return false;
        } else if (!toCity.equals(other.toCity))
            return false;
        if (fromCity == null) {
            if (other.fromCity != null)
                return false;
        } else if (!fromCity.equals(other.fromCity))
            return false;
        return true;
    }
}
