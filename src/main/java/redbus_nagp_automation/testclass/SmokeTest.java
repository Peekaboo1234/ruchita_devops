package redbus_nagp_automation.testclass;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import redbus_nagp_automation.pages.AccountOptionsPage;
import redbus_nagp_automation.pages.BusDetailsPage;
import redbus_nagp_automation.pages.HelpPage;
import redbus_nagp_automation.pages.HomePage;
import redbus_nagp_automation.pages.OffersPage;
import redbus_nagp_automation.pages.RedRailPage;
import redbus_nagp_automation.pages.SearchBusPage;
import redbus_nagp_automation.pages.SearchTrainPage;
import redbus_nagp_automation.utility.ExtentReport;

/**
 * Class to perform smoke tests on the RedBus application.
 *
 * @author ruchitapathak
 */
public class SmokeTest extends ExtentReport {

	private static final Logger log = LogManager.getLogger(SmokeTest.class);

	/**
	 * Test to verify the elements on the Home Page.
	 *
	 * @throws IOException
	 */
//	@Test(description = "Verify HomePage Elements", groups = "smoke")
//	public void verifyHomePage() throws IOException {
//		WebDriver driver = BaseTest.getDriver();
//		test = extent.createTest("verifyHomePage");
//		test.info("Verifying the homepage elements");
//		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
//		homePage.verifyHomePageElements(driver);
//		homePage.isErrorMessageDisplayed(driver);
//		log.info("Homepage elements verified successfully");
//	}
//
//	/**
//	 * Test to verify the Account Options Page.
//	 *
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	@Test(description = "Verify Account Details Page", groups = "smoke")
//	public void verifyAccountDetails() throws IOException, InterruptedException {
//		WebDriver driver = BaseTest.getDriver();
//		test = extent.createTest("verifyAccountDetails");
//		test.info("Verifying the account options");
//		AccountOptionsPage accountOptionsPage = PageFactory.initElements(driver, AccountOptionsPage.class);
//		accountOptionsPage.verifyAccountDropdownOptionsCount(driver);
//		accountOptionsPage.visitOptions(driver);
//		log.info("Account options verified successfully");
//	}
//
//	/**
//	 * Test to verify the Bus Search functionality.
//	 *
//	 * @throws IOException
//	 */
//	@Test(description = "Verify Bus Search")
//	public void verifyBusSearch() throws IOException {
//		WebDriver driver = BaseTest.getDriver();
//		test = extent.createTest("verifyBusSearch");
//		test.info("Verifying the bus search functionality");
//		SearchBusPage searchPage = PageFactory.initElements(driver, SearchBusPage.class);
//		searchPage.firstBusSearch(driver);
//		Object[][] searchData = SearchBusPage.searchData(); // Get test data from data provider
//		for (Object[] searchDataEntry : searchData) {
//			String sourceCity = (String) searchDataEntry[0];
//			String destinationCity = (String) searchDataEntry[1];
//			String searchResult = (String) searchDataEntry[2];
//			searchPage.busSearches(driver, sourceCity, destinationCity, searchResult);
//		}
//		log.info("Bus search functionality verified successfully");
//	}

	/**
	 * Test to verify the elements on the RedRail Page.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(description = "Verify Train page elements", groups = "smoke")
	public void verifyRedRailPage() throws IOException, InterruptedException {
		
		WebDriver driver = BaseTest.getDriver();
		test = extent.createTest("verifyRedRailPage");
		test.info("Verifying train details page elements");
		RedRailPage redRailPage = PageFactory.initElements(driver, RedRailPage.class);
		redRailPage.verifyNavigationToRedRailPage(driver);
		redRailPage.verifyRailPageElements();
		log.info("RedRail page elements verified successfully");
	}

//	/**
//	 * Test to verify the Train Search functionality.
//	 *
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	@Test(description = "Verify Train search", groups = "smoke")
//	public void verifyTrainSearch() throws IOException, InterruptedException {
//		WebDriver driver = BaseTest.getDriver();
//		test = extent.createTest("verifyTrainSearch");
//		test.info("Verifying the train search functionality");
//		SearchTrainPage searchTrainPage = PageFactory.initElements(driver, SearchTrainPage.class);
//		WebElement firstTrainElement = searchTrainPage.performTrainSearch(driver);
//		searchTrainPage.extractAndPrintTrainDetails(firstTrainElement);
//		log.info("Train search page verified successfully");
//	}

	/**
	 * Test to verify the Offers Page.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(description = "Verify Offers Page", groups = "smoke")
	public void verifyOffersPage() throws IOException, InterruptedException {
		WebDriver driver = BaseTest.getDriver();
		test = extent.createTest("verifyOffersPage");
		test.info("Verifying the offers page");
		OffersPage offersPage = PageFactory.initElements(driver, OffersPage.class);
		offersPage.verifyNavigationToOffers(driver);
		offersPage.verifySecondPageTitle(driver);
		offersPage.verifyOfferCards(driver);
		log.info("Offers page verified successfully");
	}

	/**
	 * Test to verify the Help Page.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(description = "Verify Help Page", groups = "smoke")
	public void verifyHelpPage() throws IOException, InterruptedException {
		WebDriver driver = BaseTest.getDriver();
		test = extent.createTest("verifyHelpPage");
		test.info("Verifying the help page");
		HelpPage helpPage = PageFactory.initElements(driver, HelpPage.class);
		helpPage.verifyNavigationToHelp(driver);
		helpPage.verifyElementsOnHelpPage(driver);
		helpPage.verifyLanguageChange(driver);
		log.info("Help page verified successfully");
	}
//
//	/**
//	 * Test to verify the searched bus details.
//	 *
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	@Test(description = "Verify bus details", groups = "smoke")
//	public void verifySearchedBusDetails() throws IOException, InterruptedException {
//		WebDriver driver = BaseTest.getDriver();
//		test = extent.createTest("verifyHelpPage");
//		test.info("Verifying the searched bus details");
//		BusDetailsPage busDetails = PageFactory.initElements(driver, BusDetailsPage.class);
//		busDetails.verifySearchedBusDetails(driver, "Delhi", "Lucknow");
//		log.info("Bus details verified successfully");
//	}

	
	/**
	 * Method to write test information from the started reporters.
	 */
	@AfterSuite
	public void flushReport() {
		extent.flush();
		log.info("Extent report created successfully");
	}
}