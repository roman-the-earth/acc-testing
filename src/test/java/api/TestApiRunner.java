package api;

import com.intuit.karate.junit5.Karate;

public class TestApiRunner {

    @Karate.Test
    Karate testMotorsApi() {
        return Karate.run("tmCategoryUsedCars").relativeTo(getClass());
    }
}
