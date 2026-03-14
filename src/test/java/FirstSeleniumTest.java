// We import the Interface (The Remote Control)
import org.openqa.selenium.WebDriver;
// We import the Implementation(The TV)
import org.openqa.selenium.chrome.ChromeDriver;

public class FirstSeleniumTest {
    public static void main(String[] args) {

        System.out.println("--- Booting up Automation Engine ___");

        // 1. The Contract: We declare the WebDriver interface and instantiate ChromeDriver
        WebDriver driver = new ChromeDriver();

        // 2. The Action: Navigate to the target application
        System.out.println("Navigating to J.P. Morgan...");
        driver.get("https://www.jpmorgan.com");

        // 3. The Validation: Extract data from the UI
        String pageTitle = driver.getTitle();
        System.out.println("Successfully landed on: " + pageTitle);

        // 4. The Teardown: ALWAYS close the browser to prevent memory leaks!
        driver.quit();

        System.out.println("---- Test Execution Complete ---");
    }
}
