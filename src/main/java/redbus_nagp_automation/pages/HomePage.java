package redbus_nagp_automation.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing the RedBus homepage.
 * This class provides methods to verify essential elements on the homepage and check for error messages.
 * 
 * @author ruchitapathak
 */
public class HomePage {
    private static Logger log = LogManager.getLogger(HomePage.class);

    @FindBy(how = How.XPATH, using = "//img[@class='rb_logo']")
    private WebElement logo;

    @FindBy(how = How.XPATH, using = "//input[@class='sc-bxivhb hrsLPT']/following-sibling::label[text()='From']")
    private WebElement fromDestination;

    @FindBy(how = How.XPATH, using = "//input[@class='sc-bxivhb hrsLPT']/following-sibling::label[text()='To']")
    private WebElement toDestination;

    @FindBy(how = How.ID, using = "search_button")
    private WebElement searchButton;

    @FindBy(how = How.XPATH, using = "//span[text()='Date']")
    private WebElement date;

    @FindBy(how = How.CSS, using = "div.DatePicker__CalendarContainer-sc-1kf43k8-0.jQCNYF")
    private WebElement calendar;

    @FindBy(how = How.XPATH, using = "//div[@class='sc-jTzLTM bIZuun' and text()='Enter \'From\' and \'To\']")
    private WebElement errorMessage;

    /**
     * Verifies the presence of essential elements on the homepage.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     */
    public void verifyHomePageElements(WebDriver driver) {
        log.info("Verify all essential elements are present on the homepage.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));

        try {
            // Verify presence of RedBus logo
            Assert.assertTrue(logo.isDisplayed(), "RedBus logo is not displayed on the homepage");

            // Verify presence of From Destination field
            Assert.assertTrue(fromDestination.isDisplayed(), "From Destination field is not displayed on the homepage");

            // Verify presence of To Destination field
            Assert.assertTrue(toDestination.isDisplayed(), "To Destination field is not displayed on the homepage");

            // Verify presence of Search button
            Assert.assertTrue(searchButton.isDisplayed(), "Search button is not displayed on the homepage");

            // Verify presence of Date field
            Assert.assertTrue(date.isDisplayed(), "Date field is not displayed on the homepage");

            // Click on the Date field to trigger the date picker
            date.click();

            // Wait for the presence and visibility of the date picker
            wait.until(ExpectedConditions.visibilityOfAllElements(calendar));
            Assert.assertTrue(calendar.isDisplayed(), "Date picker is not displayed on the homepage");

            log.info("All essential elements are present and visible on the homepage");
        } catch (AssertionError e) {
            log.error("One or more essential elements on the homepage are not displayed");
            throw e; // Rethrow the AssertionError to indicate test failure
        }
    }

    /**
     * Checks if the error message is displayed on the homepage.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @return True if the error message is displayed, otherwise False.
     */
    public boolean isErrorMessageDisplayed(WebDriver driver) {
        log.info("Checking if the error message is displayed on the homepage.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));

        try {
            // Click on the search button without entering any field
            searchButton.click();

            // Wait for the presence and visibility of the error message
            wait.until(ExpectedConditions.visibilityOf(errorMessage));

            log.info("Error message is displayed on the homepage");
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            log.error("Error message is not displayed on the homepage");
            return false;
        }
    }
}
