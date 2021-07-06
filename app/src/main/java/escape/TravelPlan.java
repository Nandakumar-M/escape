package escape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Immutable
public final class TravelPlan {

    private final List<Hop> hops;

    public TravelPlan() {
        this.hops = List.of();
    }

    private TravelPlan(final List<Hop> hops) {
        this.hops = hops;
    }

    public List<Hop> getHops() {
        return Collections.unmodifiableList(hops);
    }

    public City getOrigin() {
        return this.hops.get(0).getFromCity();
    }

    public TravelPlan addHop(final Hop hop) {
        final ArrayList<Hop> copyHops = new ArrayList<>(this.getHops());
        copyHops.add(hop);
        return new TravelPlan(copyHops);
    }

    // public Set<String> getVisitedContinentIds() {
    // return
    // this.hops.stream().map(Hop::getFromCity).map(City::getId).collect(Collectors.toSet());
    // }

    public boolean alreadyVisited(String continentId) {
        return this.hops.stream().map(Hop::getFromCity).anyMatch(city -> city.getContinentId().equals(continentId));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.hops.forEach(h -> {
            City city = h.getFromCity();
            builder.append(city.getId());
            builder.append(" (");
            builder.append(city.getName());
            builder.append(", ");
            builder.append(city.getContinentId());
            builder.append(")\n");
        });
        return builder.toString();
    }

}
