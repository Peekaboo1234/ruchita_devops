package redbus_nagp_automation.pages;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import redbus_nagp_automation.testclass.BaseTest;
import redbus_nagp_automation.utility.ExcelReader;
import redbus_nagp_automation.utility.Utility;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing the Search Bus page on RedBus.
 * This class provides methods to search for buses and verify search results.
 * 
 * @author ruchitapathak
 */
public class SearchBusPage {

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

	@FindBy(how = How.XPATH, using = "//text[@class='dateText']")
	private WebElement dateText;

	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'found')]")
	private WebElement searchResults;

	@FindBy(how = How.XPATH, using = "//img[@class='rb_logo']")
	private WebElement redBusLogo;

	@FindBy(how = How.XPATH, using = "//div[text()='Oops! No buses found.']")
	private WebElement firstSearchResult;
	
	private static final Logger log = LogManager.getLogger(SearchBusPage.class);

	/**
	 * Initializes a new instance of the SearchBusPage class.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 */
	public SearchBusPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	/**
	 * Provides test data for searching buses.
	 * 
	 * @return A two-dimensional array of test data.
	 * @throws IOException If an I/O error occurs.
	 */
	@DataProvider(name = "searchData")
	public static Object[][] searchData() throws IOException {
		String filePath = "src\\test\\resources\\BusRoutes.xlsx";
		String sheetName = "Sheet1";
		return ExcelReader.readTestData(filePath, sheetName);
	}

	/**
	 * Tests the search functionality for the first bus.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 * @throws IOException If an I/O error occurs.
	 */
	@Test
	public void firstBusSearch(WebDriver driver) throws IOException {
		String sourceCity = BaseTest.readProperties("SourceCity");
		String destinationCity = BaseTest.readProperties("DestinationCity");
		String expectedBusesFound = BaseTest.readProperties("ExpectedBusesFound");
		searchBuses(driver, sourceCity, destinationCity, expectedBusesFound, true);
	}

	/**
	 * Tests the search functionality for multiple buses using test data.
	 * 
	 * @param driver             The WebDriver instance used to interact with the browser.
	 * @param sourceCity         The source city for the bus search.
	 * @param destinationCity    The destination city for the bus search.
	 * @param expectedBusesFound The expected number of buses found for the given route.
	 * @throws IOException 
	 */
	@Test(dataProvider = "searchData")
	public void busSearches(WebDriver driver, String sourceCity, String destinationCity, String expectedBusesFound) throws IOException {
		searchBuses(driver, sourceCity, destinationCity, expectedBusesFound, false);
	}

	/**
	 * Performs a bus search based on the provided parameters and verifies the search results.
	 * 
	 * @param driver             The WebDriver instance used to interact with the browser.
	 * @param sourceCity         The source city for the bus search.
	 * @param destinationCity    The destination city for the bus search.
	 * @param expectedBusesFound The expected number of buses found for the given route.
	 * @param startFromBeginning Indicates whether to start the search from the beginning or not.
	 * @throws IOException 
	 */
	private void searchBuses(WebDriver driver, String sourceCity, String destinationCity, String expectedBusesFound,
			boolean startFromBeginning) throws IOException {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
		driver.get(BaseTest.readProperties("url"));
		try {
			if (startFromBeginning) {
				Assert.assertTrue(Utility.isElementDisplayed(driver, sourceInput),
						"Source city input field is not displayed");
				Assert.assertTrue(Utility.isElementDisplayed(driver, destinationInput),
						"Destination city input field is not displayed");
				Assert.assertTrue(Utility.isElementDisplayed(driver, dateInput), "Date input field is not displayed");
			}

			Utility.sendKeysAndClick(driver, sourceInput, sourceCity, "sc-gZMcBi");
			Utility.sendKeysAndClick(driver, destinationInput, destinationCity, "sc-gZMcBi");

			try {
				// Attempt to click date input
				dateInput.click();
				wait.until(ExpectedConditions.visibilityOfAllElements(calendar));
				wait.until(ExpectedConditions.elementToBeClickable(nextMonth)).click();
				selectedDate.click();
			} catch (Exception e) {
				// Log a message if date input is already selected
				log.info("Date input is already selected.");
			}

			searchButton.click();

			try {
				wait.until(ExpectedConditions.visibilityOf(firstSearchResult));
				redBusLogo.click();
				wait.until(ExpectedConditions.visibilityOf(searchButton));
				log.info("No buses found for the given route.");
			} catch (Exception e) {
				wait.until(ExpectedConditions.visibilityOf(searchResults));
				System.out.println(searchResults.getText());
				// Get the actual message displayed
				String actualBusesFound = searchResults.getText();
				// Assert that the actual message matches the expected message from test data
				Assert.assertTrue(
						actualBusesFound.toLowerCase().contains(expectedBusesFound.toLowerCase()),
						"Incorrect message for buses found");
				redBusLogo.click();
			}
			wait.until(ExpectedConditions.visibilityOf(searchButton));
			log.info("Search completed successfully.");
		} catch (Exception e) {
			log.error("An error occurred during the search process: " + e.getMessage());
			Assert.fail("An error occurred during the search process: " + e.getMessage());
		}
	}
}
