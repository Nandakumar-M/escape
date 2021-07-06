package escape;

import java.io.File;

public class App {

    public static void main(final String[] args) {

        final String filePath = args[0];
        final File file = new File(filePath);
        final String originCityId = args[1];
        final TravelPlanner planner = new TravelPlanner(file);

        System.out.println(planner.findBestPossiblePlan(originCityId, 60));

    }
}
