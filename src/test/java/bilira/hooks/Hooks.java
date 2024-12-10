package bilira.hooks;

import bilira.utilities.Driver;
import io.cucumber.java.BeforeAll;

public class Hooks {

    @BeforeAll
    public void setup() {
        Driver.getDriver();
    }


}

