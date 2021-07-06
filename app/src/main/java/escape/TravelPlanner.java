package escape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.JSONTokener;

public class TravelPlanner {

    private double shortestDistance = Double.MAX_VALUE;
    private TravelPlan bestPlan = null;

    private final EarthDistanceCalculator calculator = new EarthDistanceCalculator();

    private final HashMap<String, City> allCitiesById = new HashMap<>();
    private final HashMap<String, List<City>> allCitiesByContinentId = new HashMap<>();

    // List<Hop> will be sorted by it's distance.
    private final HashMap<String, List<Hop>> hopsByCityId = new HashMap<>();

    public TravelPlanner(final File file) {

        JSONTokener tokener;
        try {
            tokener = new JSONTokener(new FileReader(file));
            final JSONObject parsed = new JSONObject(tokener);
            final Iterator<String> itr = parsed.keys();
            while (itr.hasNext()) {
                final String id = itr.next();
                final JSONObject cityJSON = parsed.getJSONObject(id);
                final String name = cityJSON.getString("name");
                final double latitude = cityJSON.getJSONObject("location").getDouble("lat");
                final double longitude = cityJSON.getJSONObject("location").getDouble("lon");
                final String continentId = cityJSON.getString("contId");

                final City city = new City(id, name, continentId, latitude, longitude);
                allCitiesById.put(id, city);
                if (allCitiesByContinentId.get(continentId) == null) {
                    allCitiesByContinentId.put(continentId, new ArrayList<>());
                }
                allCitiesByContinentId.get(continentId).add(city);
            }

        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String findBestPossiblePlan(final String originCityId, final int maxTimeInSeconds) {

        final TravelPlan plan = new TravelPlan();
        final City origin = getCityById(originCityId);
        final long endTime = System.currentTimeMillis() + maxTimeInSeconds * 1000;

        findBestPlan(plan, origin, endTime);

        return bestPlan.toString() + "\nDistance travelled : " + getPlanDistance(bestPlan);
    }

    // this method will be called recursively
    private void findBestPlan(final TravelPlan plan, final City currentCity, final long endTime) {

        // check for time limit
        if (System.currentTimeMillis() > endTime) {
            return;
        }

        final List<Hop> hops = getHopsToOtherContinentCities(currentCity).stream()
                .filter(h -> !plan.alreadyVisited(h.getToCity().getContinentId())).collect(Collectors.toList());

        if (hops.isEmpty()) {
            final TravelPlan finalPlan = plan.addHop(new Hop(currentCity, plan.getOrigin()));
            final double distance = getPlanDistance(finalPlan);
            if (distance < shortestDistance) {
                bestPlan = finalPlan;
                shortestDistance = distance;
            }
            return;
        }
        hops.stream().forEach(hop -> {
            TravelPlan next = plan.addHop(hop);
            findBestPlan(next, hop.getToCity(), endTime);
        });

    }

    // this method returns sorted hops from currentCity to all cities of other
    // continents.
    // the return value of this method is cached.
    private List<Hop> getHopsToOtherContinentCities(final City currentCity) {
        if (hopsByCityId.containsKey(currentCity.getId())) {
            return hopsByCityId.get(currentCity.getId());
        }
        final List<Hop> hops = allCitiesById.values().stream()
                .filter(nextCity -> nextCity.getContinentId() != currentCity.getContinentId())
                .map(nextCity -> new Hop(currentCity, nextCity)).collect(Collectors.toList());

        hops.sort(new Comparator<Hop>() {
            public int compare(Hop h1, Hop h2) {
                Double d1 = getHopDistance(h1);
                Double d2 = getHopDistance(h2);
                return d1.compareTo(d2);
            };
        });
        hopsByCityId.put(currentCity.getId(), Collections.unmodifiableList(hops));
        return hopsByCityId.get(currentCity.getId());
    }

    private double getPlanDistance(final TravelPlan finalPlan) {

        return finalPlan.getHops().stream().map(hop -> getHopDistance(hop)).reduce(Double::sum).orElse(0d);

    }

    private double getHopDistance(final Hop hop) {
        return calculator.getDistance(hop.getFromCity(), hop.getToCity());
    }

    private City getCityById(final String id) {
        return allCitiesById.get(id);
    }

}
