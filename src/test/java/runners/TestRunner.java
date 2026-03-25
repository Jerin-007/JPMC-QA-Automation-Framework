package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

// --------------------------------------------------------------
// THE IGNITION SWITCH: This tells Cucumber exactly how to behave
// --------------------------------------------------------------

@CucumberOptions(
        // 1. Where are the English feature files?
        features = "src/test/resources/features",

        // 2. Where is the Java glue code?
        glue = "steps",

        // 3. Make the console output easy to read (no weird characters)
        monochrome = true,

        // 4. Generate beautiful Enterprise-grade reports automatically!
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-report.html",
                "json:target/cucumber-reports/cucumber.json"
        }
)

public class TestRunner extends AbstractTestNGCucumberTests {
    // This class is completely empty!
    // The CucumberOptions annotation does 100% of the work.
}
