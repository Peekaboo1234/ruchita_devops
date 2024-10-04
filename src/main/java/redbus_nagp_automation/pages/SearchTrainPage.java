package redbus_nagp_automation.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import redbus_nagp_automation.utility.WaitInterval;
import redbus_nagp_automation.utility.Utility;

/**
 * Page class for managing the Search Train page on RedBus.
 * This class provides methods to perform train searches and extract train details.
 * 
 * @author ruchitapathak
 */
public class SearchTrainPage {

    private static final Logger log = LogManager.getLogger(SearchTrainPage.class);

    @FindBy(how = How.XPATH, using = "//div[@class='img_rb_vertical_wrap']/div")
    private WebElement trainTickets;

    @FindBy(how = How.ID, using = "src")
    private WebElement fromField;

    @FindBy(how = How.ID, using = "dst")
    private WebElement toField;

    @FindBy(how = How.XPATH, using = "//button[text()='search trains']")
    private WebElement searchButton;

    /**
     * Initializes a new instance of the SearchTrainPage class.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     */
    public SearchTrainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    /**
     * Performs a train search and returns the first train element found in the search results.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @return The WebElement representing the first train element found in the search results.
     */
    public WebElement performTrainSearch(WebDriver driver) {
        log.info("Performing train search");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TwentySecond.getSeconds()));
        wait.until(ExpectedConditions.visibilityOf(trainTickets));
        trainTickets.click();

        // Enter "New Delhi" in the 'From' field
        Utility.sendKeysAndClick(driver, fromField, "New Delhi", "stn_name_code_wrap");

        // Enter "Lucknow" in the 'To' field
        Utility.sendKeysAndClick(driver, toField, "Lucknow - All Stations", "stn_name_code_wrap");

        // Click on the "Search Trains" button
        searchButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("search_tupple_content")));

        // Find all elements with class name 'search_tupple_content'
        List<WebElement> trainElements = driver.findElements(By.className("search_tupple_content"));

        if (!trainElements.isEmpty()) {
            // Return the first train element
            return trainElements.get(0);
        } else {
            log.info("No trains found.");
            return null;
        }
    }

    /**
     * Extracts and prints the details of a train element.
     * 
     * @param trainElement The WebElement representing the train element.
     */
    public void extractAndPrintTrainDetails(WebElement trainElement) {
        WebElement trainNameElement = trainElement.findElement(By.className("srp_train_name"));
        String trainName = trainNameElement.getText();
        WebElement trainNumberElement = trainElement.findElement(By.className("srp_train_code"));
        String trainNumber = trainNumberElement.getText().trim().replace("|", "");

        WebElement sourceDestinationElement = trainElement.findElement(By.className("srp_src_dst_stations"));
        String sourceStation = sourceDestinationElement.findElements(By.tagName("div")).get(0).getText();
        String destinationStation = sourceDestinationElement.findElements(By.tagName("div")).get(1).getText();

        // Print the train information
        log.info("Details of the first train in the search results:");
        log.info("Train Name: " + trainName);
        log.info("Train Number: " + trainNumber);
        log.info("Source Station: " + sourceStation);
        log.info("Destination Station: " + destinationStation);
    }
}
