package escape;

public class EarthDistanceCalculator {

    private double calculateDistance(final double lat1, final double lon1, final double lat2, final double lon2) {

        final int R = 6371; // Radius of the earth in km
        final double dLat = deg2rad(lat2 - lat1); // deg2rad below
        final double dLon = deg2rad(lon2 - lon1);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    private double deg2rad(final double deg) {
        return deg * (Math.PI / 180);
    }

    public double getDistance(final City fromCity, final City toCity) {
        double distance = 0;
        if (fromCity.getId().compareTo(toCity.getId()) < 0) {
            distance = calculateDistance(fromCity.getLatitude(), fromCity.getLongitude(), toCity.getLatitude(),
                    toCity.getLongitude());

        } else {
            distance = calculateDistance(toCity.getLatitude(), toCity.getLongitude(), fromCity.getLatitude(),
                    fromCity.getLongitude());
        }
        return distance;
    }

}