package redbus_nagp_automation.pages;

import java.time.Duration;

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

import redbus_nagp_automation.utility.Utility;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing bus details.
 * This class provides methods to verify searched bus details.
 * 
 * @author ruchitapathak
 */
public class BusDetailsPage {
    private static final Logger log = LogManager.getLogger(BusDetailsPage.class);

    @FindBy(how = How.XPATH, using = "//label[text()='From']/preceding-sibling::input")
    private static WebElement sourceInput;

    @FindBy(how = How.XPATH, using = "//label[text()='To']/preceding-sibling::input")
    private WebElement destinationInput;

    @FindBy(how = How.XPATH, using = "//span[text()='Date']")
    private WebElement dateInput;

    @FindBy(how = How.ID, using = "search_button")
    private WebElement searchButton;

    @FindBy(how = How.CSS, using = "div.DatePicker__CalendarContainer-sc-1kf43k8-0.jQCNYF")
    private WebElement calendar;

    @FindBy(how = How.XPATH, using = "//div[@class='DayNavigator__IconBlock-qj8jdz-2 iZpveD'][3]/*[name()='svg']")
    private WebElement nextMonth;

    @FindBy(how = How.XPATH, using = "//span[text()='26']")
    private WebElement selectedDate;

    @FindBy(how = How.XPATH, using = "//span[contains(text(), 'found')]")
    private WebElement searchResult;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='travels lh-24 f-bold d-color']")
    private WebElement busOperatorName;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='dp-time f-19 d-color f-bold']")
    private WebElement departureTime;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='dp-loc l-color w-wrap f-12 m-top-16']")
    private WebElement departureLocation;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='dur l-color lh-24']")
    private WebElement journeyDuration;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='bp-time f-19 d-color disp-Inline']")
    private WebElement arrivalTime;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='bp-loc l-color w-wrap f-12 m-top-16']")
    private WebElement arrivalLocation;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='rating-sec lh-24']")
    private WebElement rating;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='no-ppl l-color  m-top-16']")
    private WebElement noOfPeopleRated;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='fare d-block']")
    private WebElement fare;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='seat-left m-top-16']")
    private WebElement noOfSeatsAvailable;

    @FindBy(how = How.XPATH, using = "//div[@class='clearfix bus-item']//div[@class='window-left m-top-8']")
    private WebElement windowSeatsAvailable;

    /**
     * Constructs a new BusDetailsPage object.
     * Initializes web elements using the provided WebDriver instance.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     */
    public BusDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifies the details of the searched bus.
     * 
     * @param driver          The WebDriver instance used to interact with the browser.
     * @param sourceCity      The source city for the bus journey.
     * @param destinationCity The destination city for the bus journey.
     */
    public void verifySearchedBusDetails(WebDriver driver, String sourceCity, String destinationCity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));

        // Entering source city and selecting from dropdown
        Utility.sendKeysAndClick(driver, sourceInput, sourceCity, "sc-gZMcBi");

        // Entering destination city and selecting from dropdown
        Utility.sendKeysAndClick(driver, destinationInput, destinationCity, "sc-gZMcBi");

        // Selecting date, searching for buses, and waiting for search result
        dateInput.click();
        wait.until(ExpectedConditions.visibilityOfAllElements(calendar));
        wait.until(ExpectedConditions.elementToBeClickable(nextMonth)).click();
        selectedDate.click();
        searchButton.click();
        wait.until(ExpectedConditions.visibilityOf(searchResult));

        // Hovering over the parent element to ensure child elements are visible
        WebElement element = driver.findElement(By.xpath("//li[contains(@class,'row-sec')]"));
        Utility.hoverToElement(driver, element);

        // Waiting for child elements to be visible
        WebElement amenitiesElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='amenties b-p-list']/span[contains(text(),'Amenities')]")));
        WebElement busPhotosElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='amenties b-p-list']/span[contains(text(),'Bus Photos')]")));
        WebElement boardingDroppingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='amenties b-p-list']/span[contains(text(),'Boarding & Dropping Points')]")));
        WebElement reviewsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='amenties b-p-list']/span[contains(text(),'Reviews')]")));
        WebElement bookingPoliciesElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='amenties b-p-list']/span[contains(text(),'Booking policies')]")));

        // Logging the display status of elements
        if (amenitiesElement.isDisplayed() &&
                busPhotosElement.isDisplayed() &&
                boardingDroppingElement.isDisplayed() &&
                reviewsElement.isDisplayed() &&
                bookingPoliciesElement.isDisplayed()) {
            log.info("Amenities, Bus Photos, Boarding & Dropping Points, Reviews, and Booking policies are displayed.");
        } else {
            log.error("One or more elements are not displayed.");
        }
    }
}
