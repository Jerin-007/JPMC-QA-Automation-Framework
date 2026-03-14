package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys; // Imports keyboard actions
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import org.openqa.selenium.interactions.Actions;


public class JPMorganHomePage {
    private WebDriver driver;

    // 1. THE LOCATORS
    private By searchButton = By.xpath("//a[contains(@class, 'search')] | //button[contains(@class, 'search')] | //div[contains(@class, 'search-icon')]");

    // We add a flexible XPath to find the input box that pops up

    private By searchInputBox = By.xpath("//input[contains(@placeholder, 'Search') or contains(@aria-label, 'Search') or @id='global-search-input']");


    /*
    // 1. The Main Menu Item we want to hover over (e.g., "Who We Are"or "About Us"
    // Using a dynamic XPath that looks for a button/link containing text!
    private By aboutUsMenu = By.xpath("//*[contains(text(), 'Who We Are') or contains(text(), 'About Us')]");
*/

    /*
    //2. The Hidden Sub-Menu Item we want to click after the hover reveals it
    private By leadershipSubMenu = By.xpath("//a[contains(text(), 'Leadership')]");
*/
    //--- THE MASTER LAOCATORS ---
    // (Notice we renamed the old locators above to match what our method expects)
    private By menuLocator = By.xpath("//*[self::a or self::button][contains(., 'Who We Are') or contains(., 'About Us') or contains(., 'Our Firm') or contains(., 'Company') or contains(., 'Insights')]");

    private By subMenuLocator = By.xpath("//*[self::a or self::button or self::span][contains(., 'Leadership') or contains(., 'History') or contains(., 'Executives') or contains(., 'Careers')]");

    /*private By searchInputBox = By.xpath("//input[@type='text' or @type='search' or contains(@name, 'search')]");
*/
    public JPMorganHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToPage() {
        System.out.println("Page Object: Navigating to J.P. Morgan homepage ...");
        driver.get("https://www.jpmorgan.com");
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void clickSearchIcon() {
        System.out.println("Page Object: Waiting for search icon to be clickable...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void enterSearchQuery(String query) {
        System.out.println("Page Object: Hunting for the VISIBLE search bar...");

        // 1. Wait for the HTML to at least load the elements into the DOM
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(searchInputBox));

        // 2. Grab EVERY matching search box on the page (mobile, desktop, hidden, etc.)
        java.util.List<org.openqa.selenium.WebElement> allSearchInputs = driver.findElements(searchInputBox);

        System.out.println("Page Object: Found " + allSearchInputs.size() + "search box in the HTML. Filtering for the visible one...");

        // 3. Loop through the list and interrogate each element
        for (org.openqa.selenium.WebElement input : allSearchInputs) {

            // The golden question: "Can the human eye see you?"
            if ( input.isDisplayed()) {
                System.out.println("Page Object: Visibe input found! Typing query...");
                input.sendKeys(query, Keys.ENTER);
                return; // Mission accomplished. Instantly exit the method.
            }
        }

        // 4. If the loop finishes and none were visible, we throw a custom error
        throw new RuntimeException("CRITICAL BUG: Search icon was clicked, but no visible input box appeared!");
    }


    public void hoverOverMenuAndClickLeadership() {
        System.out.println("Page Object: Hunting for the VISIBLE Main Menu...");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));

        /*//Notice the below line is no more needed and can be deleted.
        org.openqa.selenium.By menuLocator = org.openqa.selenium.By.xpath("//*[self::a or self::button][contains(., 'Who We Are') or contains(., 'About Us') or contains(., 'Our Firm') or contains(., 'Company') or contains(., 'Insights')]");
*/
        System.out.println("Page Object: Waiting for React DOM and CSS Paint to fully render...");

        // 1. THE LAMBDA WAIT ENGINE (Bypassing the Race Condition!)
        org.openqa.selenium.WebElement visibleDesktopMenu = wait.until(d -> {
            // Grab the elements
            java.util.List<org.openqa.selenium.WebElement> elements = d.findElements(menuLocator);

            // Loop through them
            for (org.openqa.selenium.WebElement el : elements) {
                if (el.isDisplayed()) {
                    return el; // Found it! The CSS has painted! Return the element and break the wait!
                }
            }
            return null; // Still hidden? Return null to force the Wait Engine to keep trying!
        });

        System.out.println("Page Object: Visible Desktop Menu found! Text: '" + visibleDesktopMenu.getText() + "'");

        // 2. Initialize the Ghost Hand and perform hover
        System.out.println("Page Object: Initializing the Ghost Hand and performing hover...");
        Actions ghostHand = new Actions(driver);
        ghostHand.moveToElement(visibleDesktopMenu).perform();

        // 3. Wait for the sub-menu animation to cascade down
        System.out.println("Page Object: Waiting for the hidden sub-menu to physically render...");


        /*// notice the beelow line can be deleted as it is no longer used
        org.openqa.selenium.By subMenuLocator = org.openqa.selenium.By.xpath("//*[self::a or self::button or self::span][contains(., 'Leadership') or contains(., 'History') or contains(., 'Executives') or contains(., 'Careers')]");
*/
        // We use our same Lambda trick here just in case the dropdown animation is slow!
        org.openqa.selenium.WebElement visibleSubMenu = wait.until(d -> {

            //It now uses the 'By subMenuLocator' from the top of the class!
            java.util.List<org.openqa.selenium.WebElement> elements = d.findElements(subMenuLocator);
            for (org.openqa.selenium.WebElement el : elements) {
                if (el.isDisplayed()) return el;
            }
            return null;
        });


        System.out.println("Page Object: Sub-menu '" + visibleSubMenu.getText() + "' revealed!");

        // 1. THE SENIOR FIX: Wait for the CSS slide-down animation to completely finish!
        System.out.println("Page Object: Waiting for CSS dropdown animation to finish...");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(visibleSubMenu));

        // 2. THE GHOST HAND CLICK: We physically glide the mouse down to the sub-menu and click!
        System.out.println("Page Object: Gliding mouse to sub-menu and clicking...");
        ghostHand.moveToElement(visibleSubMenu).click().perform();


        /*
        // DELETE THESE TWO LLINES
        System.out.println("Page Object: Sub-menu revealed! Clicking it now...");
        visibleSubMenu.click();
        */

    }




    /*

    public void hoverOverMenuAndClickLeadership() {
        System.out.println("Page Object: Hunting for the VISIBLE Main Menu...");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));

        // 1. THE SENIOR FIX: Scoping. Find the top header/nav area first!
        System.out.println("Page Object: Locating the Top Navigation Bar...");
        org.openqa.selenium.By headerLocator = org.openqa.selenium.By.cssSelector("header, nav");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(headerLocator));
        org.openqa.selenium.WebElement topNavBar = driver.findElement(headerLocator);

        // 2. Search ONLY inside the topNavBar.
        // CRITICAL: Notice the '.' at the very beginning (.//*).
        // Without the dot, Selenium ignores the scope and searches the whole page again!
        org.openqa.selenium.By menuLocator = org.openqa.selenium.By.xpath(".//*[self::a or self::button][contains(., 'Who We Are') or contains(., 'About Us') or contains(., 'Our Firm') or contains(., 'Company') or contains(., 'Insights')]");

        java.util.List<org.openqa.selenium.WebElement> menus = topNavBar.findElements(menuLocator);
        System.out.println("Page Object: Found " + menus.size() + " potential menu items strictly inside the Header. Checking visibility...");

        org.openqa.selenium.WebElement visibleDesktopMenu = null;

        for (org.openqa.selenium.WebElement menu : menus) {
            if (menu.isDisplayed()) {
                visibleDesktopMenu = menu;
                System.out.println("Page Object: Visible Desktop Menu found! Text: '" + menu.getText() + "'");
                break;
            }
        }

        if (visibleDesktopMenu == null) {
            throw new RuntimeException("CRITICAL BUG: No visible menus found in the Header! (Is a Cookie Banner blocking the screen?)");
        }

        // 3. Initialize the Ghost Hand and perform hover
        System.out.println("Page Object: Initializing the Ghost Hand and performing hover...");
        Actions ghostHand = new Actions(driver);
        ghostHand.moveToElement(visibleDesktopMenu).perform();

        // 4. Wait for the sub-menu animation to cascade down
        System.out.println("Page Object: Waiting for the hidden sub-menu to physically render...");
        org.openqa.selenium.By subMenuLocator = org.openqa.selenium.By.xpath("//*[self::a or self::button or self::span][contains(., 'Leadership') or contains(., 'History') or contains(., 'Executives') or contains(., 'Careers')]");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(subMenuLocator));

        System.out.println("Page Object: Sub-menu revealed! Clicking it now...");
        driver.findElement(subMenuLocator).click();
    }
    */




/*
    public void hoverOverMenuAndClickLeadership() {
        System.out.println("Page Object: Hunting for the VISIBLE Main Menu...");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));


        *//*
        // 1. Broaden the locator to catch current live JPMC text variations
        org.openqa.selenium.By menuLocator = org.openqa.selenium.By.xpath("//*[contains(text(), 'Who We Are') or contains(text(), 'About Us') or contains(text(), 'Our Firm') or contains(text(), 'Company')]" );
        *//*


        *//*
        // 1.The Bulletproof Senior Xpath
        // We use //a to filter out non-links, and the '.' operator to pierce through hidden spans!
        org.openqa.selenium.By menuLocator = org.openqa.selenium.By.xpath("//a[contains(., 'Who We Are') or contains(., 'Our Firm') or contains(., 'Company') or contains(., 'Insights')]");

*//*

        // Catching Links, Buttons, OR Spans with the text!
        org.openqa.selenium.By menuLocator = org.openqa.selenium.By.xpath("//*[self::a or self::button or self::span][contains(text(), 'Who We Are') or contains(text(), 'About Us') or contains(text(), 'Our Firm') or contains(text(), 'Company') or contains(text(), 'Insights')]");


        // 2. THE SENIOR FIX: Force the engine to wait for the HTML to actually render the menu!
        System.out.println("Page Object:  Waiting for React DOM to inject navigation items...");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(menuLocator));

        // 3. Now that we know it exits, grab EVERY matching element (Mobile + Desktop)
        java.util.List<org.openqa.selenium.WebElement> menus = driver.findElements(menuLocator);
        System.out.println("Page Oject: Found" + menus.size() + "potential menu items in the HTML. Checking visibility...");

        org.openqa.selenium.WebElement visibleDesktopMenu = null;

        // 4. Filter for the visible desktop screen element
        for (org.openqa.selenium.WebElement menu: menus) {
            if(menu.isDisplayed()) {
                visibleDesktopMenu = menu;
                // Let's print put exactly what word it found so we know for next time!
                System.out.println("Page Object: Visible Desktop Menu found! Text: '" + menu.getText() + "'");
                break;
            }
        }
        if ( visibleDesktopMenu == null) {
            throw new RuntimeException("CRITICAL BUG: Menus were found in HTML, but ALL of them are hidden! (Is the browser window too small?)");
        }

        // 5. Initialize the Ghost Hand and perform hover
        System.out.println("Page Object: Initializing the Ghost Hand and performing hover...");
        Actions ghostHand = new Actions(driver);
        ghostHand.moveToElement(visibleDesktopMenu).perform();

        // 6. Wait for the sub-menu animation to cascade down
        System.out.println("Page Object: Waiting for the hidden sub-menu to physically render...");

        *//*
        org.openqa.selenium.By subMenuLocator = org.openqa.selenium.By.xpath("//a[contains(text(), 'Leadership') or contains(text(), 'History') or contains(text(), 'Executives')]");
        *//*


        *//*
        // 2. Upgrading the dub-menu locator with the dot operator a well!
        org.openqa.selenium.By subMenuLocator = org.openqa.selenium.By.xpath("//a[contains(., 'Leadership') or contains(., 'History') or contains(., 'Executives') or contains(., 'Careers')]");

        *//*


        // Catching the sub-menu items using the same bulletproof logic!
        org.openqa.selenium.By subMenuLocator = org.openqa.selenium.By.xpath("//*[self::a or self::button or self::span][contains(text(), 'Leadership') or contains(text(), 'History') or contains(text(), 'Executives') or contains(text(), 'Careers')]");

        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(subMenuLocator));

        System.out.println("Page Object: Sub-menu revealed! Clicking it now...");
        driver.findElement(subMenuLocator).click();
    }
    */




/*
    // --- THE SENIOR ACTIONS ENGINE (Upgraded with Visibility Filtering) ---
    public void hoverOverMenuAndClickLeadership() {
        System.out.println("Page Object: Hunting for the VISIBLE Main Menu...");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        // 1. Grab EVERY 'Who We Are' or 'About Us' element (Mobile + Desktop)
        java.util.List<org.openqa.selenium.WebElement> menus = driver.findElements(By.xpath("//*[contains(text(), 'Who We Are') or contains(text(), 'About Us')]"));
        org.openqa.selenium.WebElement visibleDesktopMenu = null;

        // 2. Filter for the one that is physically visible on the desktop screen
        for (org.openqa.selenium.WebElement menu : menus) {
            if (menu.isDisplayed()) {
                visibleDesktopMenu = menu;
                System.out.println("Page Object: Visible Desktop Menu Found!");
                break;
            }
        }

        if (visibleDesktopMenu == null) {
            throw new RuntimeException("CRITICAL BUG: Could not find any visible Main Menu to hover over!");
        }

        // 3. Initialize the Ghost Hand and hover over the CORRECT element
        System.out.println("Page Object: Initializing the Ghost Hand and performing hover...");
        Actions ghostHand = new Actions(driver);
        ghostHand.moveToElement(visibleDesktopMenu).perform();

        // 4. Wait for the sub-menu animation to cascade down and become visible
        System.out.println("Page Object: Waiting for the hidden sub-menu to physically render...");

        // Broadened the locator to handle JPMC content updates (leadership or History)
        By subMenuLocator = By.xpath("//a[contains(text(), 'Leadership') or contains(text(), 'History')]");
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(subMenuLocator));

        System.out.println("Page Object: Sub-menu revealed! Clicking it now...");
        driver.findElement(subMenuLocator).click();
    }

    */

    /*
    public void hoverOverMenuAndClickLeadership() {
        System.out.println("Page Object: Initializing the Ghost Hand (Actions class)...");

        // 1. Create the Actions object and hand it our driver
        Actions ghostHand = new Actions(driver);

        // 2. Wait for the main menu to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(aboutUsMenu));

        // 3. Find the physical web element
        org.openqa.selenium.WebElement menuElement = driver.findElement(aboutUsMenu);

        System.out.println("Page Object: Hovering mouse over the Main Menu...");
        // 4. THE MAGIC HOVER COMMAND (Do not forget .perform()! )
        ghostHand.moveToElement(menuElement).perform();

        // 5. Now that the hover revealed the hidden menu, we wait for the sub-item to appear!
        System.out.println("Page Object: Waiting for the hidden sub-menu to physically render...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(leadershipSubMenu));

        System.out.println("Page Object: Clicking the newly revealed 'Leadership'link...");
        driver.findElement(leadershipSubMenu).click();
    }

    */

    // 2. THE NEW DATA ENTRY METHOD
    /*public void enterSearchQuery(String query) {
        System.out.println("Page Object: Typing '" + query + "' into the search bar...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Upgraded from 'visibilityOfElementLocated' to 'elementToBeClickable'
        wait.until(ExpectedConditions.elementToBeClickable(searchInputBox))
                .sendKeys(query, Keys.ENTER);
    }*/


   /* public void enterSearchQuery(String query) {
        System.out.println("Page Object: Typing '" + query + "'into the search bar...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Notice we wait for it to be VISIBLE, not just clickable.
        // The we send the text, and instantly send the ENTER key!
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputBox))
                .sendKeys(query, Keys.ENTER);
    }*/

}




/*
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
// New Imports for Explicit Waits
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class JPMorganHomePage {

    private WebDriver driver;
    private By searchButton = By.xpath("//a[contains(@class, 'search')] | //button[contains(@class, 'search')] | //div[contains(@class, 'search-icon')]");

    public JPMorganHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToPage() {
        System.out.println("Page Object: Navigating to J.P. Morgan homepage.....");
        driver.get("https://www.jpmorgan.com");
    }
    public String getPageTitle() {
        return driver.getTitle();
    }

    public void clickSearchIcon() {
        System.out.println("Page Object: Waiting for Search icon to be clickable...");

        // 1. We create the Wait Engine (Maximum 10 seconds)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 2. We command the engine to wait UNTIL the button is physically clickable, then click it!
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}
*/




/*
package pages;

// Notice the new import for 'By'
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class JPMorganHomePage {

    private WebDriver driver;

    // 1. THE LOCATORS (Locked at the top)
    // We are using an Xpath to find the search button on the JPMC navigation bar
    private By searchButton = By.xpath("//a[contains(@class, 'search')] | //button[contains(@class, 'search')] | //div[contains(@class, 'search-icon')]");

    // (Note: Enterprise sites change their code frequently. I provided a flexible XPath above
    // that looks for common 'search' class names just in case they updated their UI today!)

    public JPMorganHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToPage(){
        System.out.println("Page Object: Navigating to J.P. Morgan homepage...");
        driver.get("https://www.jpmorgan.com");
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // 2. THE NEW ACTION METHOD
    public void clickSearchIcon() {
        System.out.println("Page Object: Locating and clicking the Search Icon...");
        // We tell the driver to find the element using our locator, and then click it!
        driver.findElement(searchButton).click();
    }
}
*/




/*
package pages; // This tells Java this file lives in the 'pages' folder

import org.openqa.selenium.WebDriver;

public class JPMorganHomePage {

    // 1. The Class Variable
    // We make it private so tests can't accidentally mess with the driver diredtly.
    private WebDriver driver;

    // 2. The Constructor
    // When a test creates this page, it MUST hand over its active browser.
    public JPMorganHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // The Actions (Methods)
    // Notice there are no assertions here! Pages don't verify; they just perform actions.
    public void navigateToPage() {
        System.out.println("Navigating to J.P. Morgan homepage...");
        driver.get("https://www.jpmorgan.com");
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
*/
