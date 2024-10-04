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
import org.testng.Assert;

import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing the RedRail page on RedBus.
 * This class provides methods to verify navigation to the RedRail page and its elements.
 * 
 * @author ruchitapathak
 */
public class RedRailPage {
	private static Logger log = LogManager.getLogger(RedRailPage.class);

	@FindBy(how = How.XPATH, using = "//div[@class='img_rb_vertical_wrap']/div")
	private WebElement trainTickets;

	@FindBy(how = How.ID, using = "src")
	private WebElement fromField;

	@FindBy(how = How.ID, using = "dst")
	private WebElement toField;

	@FindBy(how = How.XPATH, using = "//img[@alt='calendar_icon']")
	private WebElement calendarField;

	@FindBy(how = How.CLASS_NAME, using = "fc_optin_main_wrap_home")
	private WebElement optInField;

	@FindBy(how = How.XPATH, using = "//button[text()='search trains']")
	private WebElement searchButton;

	@FindBy(how = How.XPATH, using = "//div[@class='sc-jTzLTM cSXDfm']")
	private WebElement calendarWidget;

	/**
	 * Initializes a new instance of the RedRailPage class.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 */
	public RedRailPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Verifies navigation to the RedRail page and its elements.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 * @throws InterruptedException If the thread is interrupted while sleeping.
	 */
	public void verifyNavigationToRedRailPage(WebDriver driver) throws InterruptedException {
		log.info("Verify red rail page and its elements");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
		wait.until(ExpectedConditions.visibilityOf(trainTickets));
		trainTickets.click();
		driver.getCurrentUrl();
		Assert.assertTrue(driver.getCurrentUrl().contains("railways"),
				"Navigation to redRail page failed. Verification failed.");
		String pageTitle = driver.getTitle();
		Assert.assertTrue(pageTitle.contains("redRail"),
				"Title of the page does not contain 'redRail'. Verification failed.");

		// Verify if the "Book Train tickets" radio button is selected by default
		List<WebElement> risSelectors = driver.findElements(By.cssSelector(".ris-selector"));
		for (WebElement selector : risSelectors) {
			// Check if the current selector is the one that should be selected by default
			WebElement radioButton = selector.findElement(By.cssSelector(".radio-button-inner"));
			if (radioButton.getAttribute("class").contains("red-background")) {
				System.out.println(selector.findElement(By.tagName("p")).getText() + " is selected by default.");
			} else {
				System.out.println(selector.findElement(By.tagName("p")).getText() + " is not selected by default.");
			}
		}
	}

	/**
	 * Verifies the presence of elements on the RedRail page.
	 */
	public void verifyRailPageElements() {
		log.info("Verify red rail page and its elements");
		Assert.assertTrue(fromField.isDisplayed(), "'From' field is not displayed");
		Assert.assertTrue(toField.isDisplayed(), "'To' field is not displayed");
		Assert.assertTrue(calendarField.isDisplayed(), "'Calendar' field is not displayed");
		Assert.assertTrue(optInField.isDisplayed(), "'Opt-in for' field is not displayed");
		Assert.assertTrue(searchButton.isDisplayed(), "Search button is not displayed");

	}
}
