import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.JPMorganHomePage;
import pages.JPMorganSearchResultsPage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import utils.ExcelUtil;

public class JPMorganTitleTest {

    WebDriver driver;
    JPMorganHomePage homePage;

    @BeforeMethod
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up fresh browser ----");
        driver = new ChromeDriver();

        // THE SENIOR FIX: Force a massive logical resolution to bypass Windows 11 scaling!
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        homePage = new JPMorganHomePage(driver);
    }


    /*
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up fresh browser ---");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new JPMorganHomePage(driver);
    }

*/



    /*
    // THE DATA PROVIDER (Our 3 test cases)
    @DataProvider(name = "searchQueries")
    public Object[][] getSearchData() {
        return new Object[][] {
                {"Software Engineering"},
                {"Credit Cards"},
                {"Wealth Management"}
        };
    */

    // THE DATA PROVIDER ( Dynamic Excel Injection )
    @DataProvider(name = "searchQueries")
    public Object[][] getSearchData() {
        // We tell TestNG: "Go ask the ExcelUtil for the data!"
        // It passes the exact filename we saved, and the default "Sheet1" tab name.
        return ExcelUtil.getExcelData("SearchData.xlsx", "Sheet1");
    }

// We temporarily turn off the Search

    @Test
    public void verifyHoverMenuNavigation() {
        System.out.println("--- @Test: Executing Complex Hover & Tab Switch Flow ---");

        homePage.navigateToPage();

        // 1. THE ANCHOR: Save the ID of the original tab before we click anything!
        String originalWindow = driver.getWindowHandle();
        System.out.println("Test: Original Tab ID saved -> " + originalWindow);

        // Execute the Hover and Click!
        homePage.hoverOverMenuAndClickLeadership();

        // 2. THE WAIT: Explicitly wait for Google Chrome to register that 2 tabs are now open
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        System.out.println("Test: Waiting for the new browser tab to open...");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe(2));

        // 3. THE JUMP: Loop through all open tabs and switch to the new one
        for(String windowHandle : driver.getWindowHandles()){
            if (!originalWindow.contentEquals(windowHandle)) {
                System.out.println("Test: Jumping Selenium's brain to the new tab -> " + windowHandle);
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // 4. THE ASSERTION: Now that we are in the new tab, check the URL!
        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);
        String newUrl = resultsPage.getCurrentUrl();
        System.out.println("Test: Success! New Tab URL is: " + newUrl);

        Assert.assertTrue(newUrl.toLowerCase().contains("careers") || newUrl.toLowerCase().contains("jpmorgan"), "CRITICAL BUG: The new tab did not route to the expected portal!");

        // 5. THE CLEANUP: Close the new tab and teleport back to the main window!
        System.out.println("Test: Closing the new tab and returning to the main window...");
        driver.close(); // 'close()' destroys ONLY the current tab. 'quit()' destroys the whole browser.
        driver.switchTo().window(originalWindow);

        System.out.println("--- @test: Hover and Tab Switch completely verified! ---");
    }

    //Below is the code before adding the tab jumping code.
    /*
    @Test(dataProvider = "searchQueries", enabled = false)
    public void verifyHomepageTitle(String searchWord) {
        System.out.println("--- @Test: Executing Search Flow for: '" + searchWord + "' ---");

        homePage.navigateToPage();
        homePage.clickSearchIcon();
        homePage.enterSearchQuery(searchWord);

        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        // --- DEFENSE LAYER 1: The Routing Check ---
        String actualUrl = resultsPage.getCurrentUrl();
        System.out.println("Results Page: Server payload URL ->" + actualUrl);

        try {

            //We teach Java to format our string exactly how Chrome formats it!
            // "Software Engineering" becomes "Software+Engineering"
            String urlEncodedSearchWord = URLEncoder.encode(searchWord, StandardCharsets.UTF_8.toString());

            // J.P. Morgan specifically uses %20 for spaces in some of their new routing,
            // So if URLEncoder outputs a '+', we can gracefully swap it to '%20' just in case!
            urlEncodedSearchWord = urlEncodedSearchWord.replace("+", "%20");
            Assert.assertTrue(actualUrl.toLowerCase().contains(urlEncodedSearchWord.toLowerCase()), "CRITICAL BUG: The routing failed. URL did not contain the encoded query!");

        } catch (Exception e) {
            System.out.println("Encoding failed: " + e.getMessage());
        }

        *//*
        Assert.assertTrue(actualUrl.toLowerCase().contains(searchWord.toLowerCase()), "CRITICAL BUG: The routing failed.URL did not contain the query!");
        *//*

        // --- DEFENSE LAYER 2: The Rendering Check (YOUR CATCH!) ---
        // Now we actually prove the page isn't blank or broken.
        boolean isDataRendered = resultsPage.isSearchTermRenderedOnPage(searchWord);

        Assert.assertTrue(isDataRendered, "CRITICAL BUG: URL was correct, but the JPMC server returned a broken/blank page missing our rsults!");

        System.out.println("--- @Test: Search for'" + searchWord + "'completely verified (URL + DOM)! ---");


*//*
        // --- YOUR BRILLIANT CATCH: THE EXACT ASSERTION ---
        String actualUrl = resultsPage.getCurrentUrl();
        System.out.println("Results Page: Server payload URL -> " + actualUrl);
        *//*

    *//*
        // We assert that the URL physically contains the exact word we just typed!
        Assert.assertTrue(actualUrl.toLowerCase().contains(searchWord.toLowerCase()), "CRITICAL BUG: The search query '" + searchWord + "' was missing from the server payload!");

        System.out.println("--- @Test: Search for '" + searchWord + "' completely verified! ---");
        *//*

    }

   */


    /*
    // 2. THE NEW GESTURE TEST
    @Test
    public void verifyHoverMenuNavigation() {
        System.out.println("--- @Test: Executing Complex Hover Gesture Flow ---");

        // Navigate to the homepage
        homePage.navigateToPage();

        // Execute our new Actions sequence!
        homePage.hoverOverMenuAndClickLeadership();

        // Wait for the new URL to load and assert we actually navigated away from the homepage!
        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);
        System.out.println("Results Page: Verifying URL changed after clicking submenu...");

        // A quick Explicit Wait directly in the test just to confirm the click worked
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.not(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe("https://www.jpmorgan.com/global")));

        String newUrl = resultsPage.getCurrentUrl();
        System.out.println("Success! Navigated to: " + newUrl);

        // Assert we actually made it to the Leadership/About page!
        Assert.assertTrue(newUrl.toLowerCase().contains("leadership") || newUrl.toLowerCase().contains("about"), "CRITICAL BUG: Hover and click failed. url did not route to the expected page!");

        System.out.println("--- @Test: Hover Gesture completely verified! ---");
    }

    */

    @AfterMethod
    public void tearDown(){
        System.out.println("--- @AfterMethod: Shutting down safely ---");
        if (driver != null) {
            driver.quit();
        }
    }
}






/*
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider; // NEW IMPORT!
import org.testng.annotations.Test;

import pages.JPMorganHomePage;
import pages.JPMorganSearchResultsPage;

public class JPMorganTitleTest {

    WebDriver driver;
    JPMorganHomePage homePage;

    @BeforeMethod
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up fresh browser ---");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new JPMorganHomePage(driver);
    }

    // 1. THE SPREADSHEET (The Data Provider)
    // We create a 2D Array. Think of this as an Excel file with 3 rows and 1 column.
    @DataProvider(name = "searchQueries")
    public Object[][] getSearchData() {
        return new Object[][] {
                {"Software Engineering"},
                {"Artificial Engineering"},
                {"Wealth Management"}
        };
    }

    // 2. THE INJECTION
    // We tell the @Test to pull data from the provider named "searchQueries"
    // We also add a String parameter (searchWord) so the test can catch the data!
    @Test(dataProvider = "searchQueries")
    public void verifyHomepageTitle(String searchWord) {
        System.out.println("--- @Test: Executing Search Flow for: '" + searchWord + "' ---");

        homePage.navigateToPage();
        homePage.clickSearchIcon();

        // 3. THE REPLACEMENT
        // We delete the hardcoded string and pass in our dynamic variable!
        homePage.enterSearchQuery(searchWord);

        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);
        String actualTitle = resultsPage.getResultsPageTitle();

        Assert.assertTrue(actualTitle.toLowerCase().contains("search"), "CRITICAL BUG: The browser never navigated to the Search esults page!");

        System.out.println("--- @Test: Search for '" + searchWord + "' completely verified! ---");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("--- @AfterMethod: Shutting down safely ----");
        if (driver != null) {
            driver.quit();
        }
    }
}

*/








/*
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// Import BOTH of your Page Objects
import pages.JPMorganHomePage;
import pages.JPMorganSearchResultsPage;

public class JPMorganTitleTest {

    WebDriver driver;
    JPMorganHomePage homePage;

    @BeforeMethod
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up browser ---");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new JPMorganHomePage(driver);
    }

    @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Executing End-to-End Search Flow ---");

        // 1. The Setup actions
        homePage.navigateToPage();
        homePage.clickSearchIcon();
        homePage.enterSearchQuery("Software Engineering");

        // --- THE HANDOFF (PAGE CHAINING) ---
        // We just hit Enter. The browser is now on a new page.
        // We must initialize the new Page Object and hand it the active browser engine!
        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);

        // 2. Extract the Browser Title instead of the HTML text
        String actualTitle = resultsPage.getResultsPageTitle();

        // 3. THE BULLETPROOF ASSERTION
        // We just verify the word "search" is in the new tab's title!
        Assert.assertTrue(actualTitle.toLowerCase().contains("search"), "CRITICAL BUG: The search results page did not load correctly!");

        System.out.println("--- @Test: Search completely verified! ---");
    } // End of @Test

*/






    /*  @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Executing End-to-End Search Flow ---");

        // 1. The Setup actions
        homePage.navigateToPage();
        homePage.clickSearchIcon();
        homePage.enterSearchQuery("Software Engineering");

        // --- THE HANDOFF (PAGE CHAINING) ---
        // We just hit Enter. The browser is now on a new page.
        // We must initialize the new Page Object and hand it the active browser engine!
        JPMorganSearchResultsPage resultsPage = new JPMorganSearchResultsPage(driver);

        // 2. The Verification action
        String actualHeaderText = resultsPage.getResultsHeaderText();

        // 3. THE FINAL ASSERTION (tHE TRUE TEST)
        // We assert that the new page physically acknowledges our search query.
        // We use toLowerCase() to make our test bulletproof against weird capitalization.
        Assert.assertTrue(actualHeaderText.toLowerCase().contains("software engineering") | actualHeaderText.toLowerCase().contains("results"), "CRITICAL BUG: The search results page did not load correctly!");

        System.out.println("--- @Test: Search completely verified! ---");
    }
*/

/*

    @AfterMethod
    public void tearDown() {
        System.out.println("--- @AfterMethod: Shutting down safely ---");
        if (driver != null) {
            driver.quit();
        }
    }
}

*/



/*import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// We must import the Page Class since it lives in a different folder!
import pages.JPMorganHomePage;

public  class JPMorganTitleTest {

    WebDriver driver;
    JPMorganHomePage homePage; // Declare the Page Object at the class level

    @BeforeMethod
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up browser ---");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // CRITICAL: We build the Page Object and hand ir the active browser engine
        homePage = new JPMorganHomePage(driver);
    }

    @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Validating using Page Object Model ---");

        homePage.navigateToPage();
        String actualTitle = homePage.getPageTitle();
        System.out.println("Found Title: " + actualTitle);
        Assert.assertTrue(actualTitle.contains("J.P. Morgan"), "BUG FOUND: The title was incorrect!");

        // 1. Click the magnifying glass
        homePage.clickSearchIcon();

        // 2. Type our query and hit Enter
        homePage.enterSearchQuery("Software Engineering");

        // We will pause for exactly 4 seconds here JUST so your human eyes can watch
        // the results page load before the @AfterMethod violently shuts the browser down.

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



   /* @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Validating using Page Object Model ---");

        homePage.navigateToPage();
        String actualTitle = homePage.getPageTitle();
        System.out.println("Found Title: " + actualTitle);
        Assert.assertTrue(actualTitle.contains("J.P. Morgan"), "BUG FOUND: The title was incorrect!");

        // NEW STEP: Command the Page Object Model to click the search icon
        homePage.clickSearchIcon();
*/
        // We will pause Java for 3 seconds just so your human eyes can see the search menu open
        // before the browser closes. ( We will learn proper 'Waits' later, Thread.sleep is just for today!)
        /*try {
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    /*}*/

   /* @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Validating using Page Object Model ---");

        // The test no longer contains raw driver.get() commands! It reads like in plain English.
        homePage.navigateToPage();
        String actualTitle = homePage.getPageTitle();

        System.out.println("Found Title: " + actualTitle);
        Assert.assertTrue(actualTitle.contains("J.P. Morgan"), "BUG FOUND: the title was incorrect!");
    }*/

  /*  @AfterMethod
    public void terDown(){
        System.out.println("--- @AfterMethod: Shutting down safely ---");
        if(driver != null){
            driver.quit();
        }
    }
}*/




/*
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JPMorganTitleTest {

    // 1. Declare the WebDriver at the Class level so all methods can see it.
    // CRITICAL: Notice it is NOT static!
    WebDriver driver;

    // 2. THE SETUP: Runs automatically before every single @Test
    @BeforeMethod
    public void setUp() {
        System.out.println("--- @BeforeMethod: Booting up fresh browser ---");
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Senior touch: a;ways maximize to avoid hidden elements
    }

    // 3. THE EXECUTION: The actual test case
    @Test
    public void verifyHomepageTitle() {
        System.out.println("--- @Test: Navigating and Validating ---");
        driver.get("https://www.jpmorgan.com");

        String actualTitle = driver.getTitle();
        System.out.println("Found Title: " + actualTitle);

        // 4. THE ASSERTION: This is how TestNG determines Pass/Fail
        // If this fails, the test stops immediately and marks it as a failure in the report.
        Assert.assertTrue(actualTitle.contains("J.P. Morgan"), "BUG FOUND: The title was incorrect");
    }

    // 5. THE TEARDOWN: Runs automatically after every single @Test, even if the test fails!
    @AfterMethod
    public void tearDown() {
        System.out.println("--- @AfterMethod: Shutting down safely ---");
        if (driver != null) {
            driver.quit();
        }
    }
}
*/
