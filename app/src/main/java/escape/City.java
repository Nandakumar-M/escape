package escape;

// Immutable
public final class City {
    private final String id;
    private final String name;
    private final String continentId;
    private final double latitude;
    private final double longitude;

    public City(final String id, final String name, final String continentId, final double latitude,
            final double longitude) {
        this.id = id;
        this.name = name;
        this.continentId = continentId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContinentId() {
        return continentId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private int hashCode = -1;

    @Override
    public int hashCode() {
        if (hashCode != -1) {
            return hashCode;
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());

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
        final City other = (City) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
