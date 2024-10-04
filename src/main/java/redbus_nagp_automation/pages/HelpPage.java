package redbus_nagp_automation.pages;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing help options. This class provides methods to navigate
 * to the help section, verify elements on the help page, and change language.
 * 
 * @author ruchitapathak
 */
public class HelpPage {
	private static Logger log = LogManager.getLogger(HelpPage.class);

	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'Help')]")
	static WebElement help;

	@FindBy(how = How.XPATH, using = "//div[contains(text(), 'redBus Help')]")
	WebElement message;

	@FindBy(how = How.XPATH, using = "//iframe[contains(@src, 'redbus.in/help')]")
	WebElement iframeElement;

	@FindBy(how = How.XPATH, using = "//span[@class='faq']")
	WebElement faq;

	@FindBy(how = How.XPATH, using = "//div[text()='Bus Tickets']")
	WebElement busTickets;

	@FindBy(how = How.XPATH, using = "//img[@alt='windowback']")
	WebElement goBack;

	@FindBy(how = How.XPATH, using = "//div[text()='Train Tickets']")
	WebElement trainTickets;

	@FindBy(how = How.XPATH, using = "//span[@class='languagePreference' and text()='English']")
	WebElement currentLanguage;

	@FindBy(how = How.XPATH, using = "//*[@id='modal-transisition']")
	WebElement languageModal;

	@FindBy(how = How.XPATH, using = "//span[text()='हिन्दी']/preceding-sibling::div")
	WebElement hindiLanguage;

	@FindBy(how = How.XPATH, using = "//div[@class='setLanguageButton']/button")
	WebElement setLanguage;

	@FindBy(how = How.XPATH, using = "//span[text()='हिन्दी']")
	WebElement changedLanguage;

	/**
	 * Verifies navigation to the help section.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 * @throws InterruptedException if the current thread is interrupted while
	 *                              waiting.
	 */
	public void verifyNavigationToHelp(WebDriver driver) throws InterruptedException {
		log.info("Navigate to the Help section");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
		help.click();
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		// Switch to new tab
		String mainWindowHandle = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(mainWindowHandle)) {
				driver.switchTo().window(handle);
				break;
			}
		}
		wait.until(ExpectedConditions.urlContains("redcare"));
		wait.until(ExpectedConditions.visibilityOf(message));
		Assert.assertTrue(message.isDisplayed(), "Message is not displayed on the Help page.");
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.equals("https://www.redbus.in/info/redcare"),
				"Current URL is not as expected. Verification failed.");
		log.info("Landed on Help page successfully.");
	}

	/**
	 * Verifies the presence of elements on the help page.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 */
    public void verifyElementsOnHelpPage(WebDriver driver) {
        log.info("Verify all essential elements are present on the Help page.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TwentySecond.getSeconds()));
        driver.get("https://www.redbus.in/info/redcare");

        try {
            // Wait for the iframe containing the help page to be visible
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//iframe[contains(@src, 'redbus.in/help')]")));
            driver.switchTo().frame(iframeElement);

            // Wait for FAQ element to be visible
            wait.until(ExpectedConditions.visibilityOf(faq));
            
            // Verify FAQ, busTickets, and trainTickets elements
            Assert.assertTrue(faq.isDisplayed(), "FAQ is not displayed on the Help page.");
            Assert.assertTrue(busTickets.isDisplayed(), "Current language is not displayed on the Help page.");
            Assert.assertTrue(trainTickets.isDisplayed(), "Train Tickets is not displayed on the Help page.");
            
            log.info("All essential elements are present on the Help page.");
        } catch (Exception e) {
            log.error("An error occurred while verifying elements on the Help page: " + e.getMessage());
        }
    }


	/**
	 * Verifies the language change functionality.
	 * 
	 * @param driver The WebDriver instance used to interact with the browser.
	 * @throws InterruptedException if the current thread is interrupted while
	 *                              waiting.
	 */
	public void verifyLanguageChange(WebDriver driver) throws InterruptedException {
		log.info("Verify change in language");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TwentySecond.getSeconds()));

		wait.until(ExpectedConditions.visibilityOfAllElements(currentLanguage));
		String selectedLanguage = currentLanguage.getText();
		Assert.assertEquals(selectedLanguage, "English", "Language is not English. Verification failed.");

		currentLanguage.click();

		driver.switchTo().defaultContent();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		Long visibleHeight = (Long) jsExecutor.executeScript("return window.innerHeight");
		// Get the current scroll position
		Long scrollPosition = (Long) jsExecutor.executeScript("return window.pageYOffset");
		// Scroll to the middle of the page
		jsExecutor.executeScript("window.scrollTo(0, " + (scrollPosition + visibleHeight / 2) + ")");
		driver.switchTo().frame(iframeElement);

		// Wait for language modal to appear
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".repositionModal")));

		// Change the language
		String script = "var element = document.evaluate(\"//span[text()='हिन्दी']/preceding-sibling::input\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; if (element) { element.click(); }";
		jsExecutor.executeScript(script);

		// Click on the Set Language button
		setLanguage.click();
		// Switch back to default content
		driver.switchTo().defaultContent();
		// Scroll to the top of the page
		jsExecutor.executeScript("window.scrollTo(0, 0);");

		driver.switchTo().frame(iframeElement);
		String newChosenLanguage = changedLanguage.getText();
		Assert.assertEquals(newChosenLanguage, "हिन्दी", "Language is not हिन्दी. Verification failed.");
		log.info("Change in language happened successfully. Verification passed.");
	}
}
