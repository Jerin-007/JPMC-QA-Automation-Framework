package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JPMorganSearchResultsPage {

    private WebDriver driver;

    public JPMorganSearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForPageLoad() {
        System.out.println("Results Page: Waiting for browser navigation to complete...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("search"));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // --- THE NEW SENIOR DOM SCANNER ---
    public boolean isSearchTermRenderedOnPage(String expectedTerm) {
        System.out.println("Results Page: Scanning the physical DOM for the word '" + expectedTerm + "'...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // We use a Lambda to tell Selenium:
            // "Look at the entire <body> tag. Keep checking until our exact word appears inside it."
            // This proves the backend server actually returned the data to the UI!
            return wait.until(d -> d.findElement(By.tagName("body")).getText().toLowerCase().contains(expectedTerm.toLowerCase()));
        } catch (Exception e) {
            // If 15 seconds pass and the word never renders, the page is broken.
            return false;
        }
    }
}





/*
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JPMorganSearchResultsPage {

    private WebDriver driver;

    public JPMorganSearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    // 1. We still wait for the page to load
    public void waitForPageLoad() {
        System.out.println("Results Page: Waiting for browser navigation to complete...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("search"));
    }

    // 2. THE NEW METHOD: We extract the raw URL to prove the data was sent
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

*/





/*
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JPMorganSearchResultsPage {

    private WebDriver driver;

    // Notice we deleted the flaky XPath entirely!

    public JPMorganSearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    // The UPGRADED ACTION: Verify via Browser Metadata instead of DOM text
    public String getResultsPageTitle() {
        System.out.println("Results Page: Waiting for browser navigation to complete...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. The Senior Trick: Wait for the URL to physically change to the search route
        // This proves the 'Enter' key actually submitted the form!
        wait.until(ExpectedConditions.urlContains("search"));

        // 2. Wait for the new Page Title to generate
        wait.until(d -> !d.getTitle().isEmpty());

        String newTitle = driver.getTitle();
        System.out.println("Results Page: Successfully loaded -> '" + newTitle + "'");

        return newTitle;
    }
}

*/






/*
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JPMorganSearchResultsPage {

    private WebDriver driver;

    // THE LOCATOR: We look for the main header that usually says "Search Results"or shows your query.
    // Using a broad 'h1' or a class containing 'result' is a safe senior strategy.

    private By searchResultsHeader = By.xpath("//h1 | //*[contains(@class, 'search-results-title')] | //*[contains(@class, 'results-header')]");

    public JPMorganSearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    // THE ACTION: Wait for the new page to physically load its data
    public String getResultsHeaderText() {
        System.out.println("Results Page: Waiting for J.P. Morgan servers to return data...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // THE SENIOR FIX: A custom Lambda Wait
        // We tell the engine: "Keep checking this element until its text is NO LONGER empty."
        wait.until(d -> !d.findElement(searchResultsHeader).getText().trim().isEmpty());


        */
/*//*
/ We wait for the header to be physically visible to the new screen
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultsHeader));
*//*


        // Once visible, we scrape the English text off the screen and it back to the Test Class
        String headerText = driver.findElement(searchResultsHeader).getText();
        System.out.println("Results Page: Extracted header text --> '" + headerText + "' ");

        return headerText;
    }
}
*/
