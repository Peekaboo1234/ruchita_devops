package redbus_nagp_automation.pages;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import redbus_nagp_automation.utility.Utility;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing the Offers page on RedBus.
 * This class provides methods to verify navigation to the Offers page, second page title, and offer cards.
 * 
 * @author ruchitapathak
 */
public class OffersPage {

    private static Logger log = LogManager.getLogger(OffersPage.class);

    @FindBy(how = How.XPATH, using = "//a[@class='OfferSection__ViewAllText-sc-16xojcc-1 eVcjqm']")
    private WebElement viewAllOffers;

    @FindBy(how = How.XPATH, using = "//td[@class='tiles offerBlock']")
    private List<WebElement> offerCards;
    
    @FindBy(how = How.ID, using = "offerClose")
    private WebElement close;
    
    @FindBy(how = How.XPATH, using = "//p[contains(text(), 'What is the Offer')]")
    private WebElement message; 

    /**
     * Verifies navigation to the Offers section.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @throws InterruptedException If the thread is interrupted while sleeping.
     */
    public void verifyNavigationToOffers(WebDriver driver) throws InterruptedException {
        log.info("Navigate to the Offers section");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
        Utility util = new Utility();
        util.scrollToElement(driver, viewAllOffers);
        util.waitAndClick(driver, viewAllOffers);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
       
        // Switch to new tab
        String mainWindowHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        wait.until(ExpectedConditions.urlContains("Offer"));
        
        Assert.assertTrue(driver.getCurrentUrl().contains("Offer"), "Navigation to Offers page failed.");
        log.info("Landed on Offers page successfully.");
    }

    /**
     * Verifies the title of the second page.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     */
    public void verifySecondPageTitle(WebDriver driver) {
        log.info("Verify Title of the second page");
        String secondPageTitle = driver.getTitle();
        Assert.assertTrue(secondPageTitle.contains("Offers"), "Title of the second page does not contain 'Offers'. Verification failed.");
        log.info("Title of the second page contains 'Offers'. Verification passed.");
    }

    /**
     * Verifies the presence of offer cards and functionality of one card.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @throws NumberFormatException If the string does not contain a parsable integer.
     * @throws IOException If an I/O error occurs.
     */
    public void verifyOfferCards(WebDriver driver) throws NumberFormatException, IOException {
    	// Verify presence of all offer cards and functionality of one card
        int actualOfferCardsCount = offerCards.size();
        
        Assert.assertTrue(actualOfferCardsCount > 1,
            "Expected offer cards count to be greater than 1, Actual count: " + actualOfferCardsCount);

        // Click on the first offer card
        if (!offerCards.isEmpty()) {
            offerCards.get(0).click();
            Assert.assertTrue(message.isDisplayed(), "Message 'What is the Offer' is not displayed. Verification failed.");
            log.info("Message 'What is the Offer' is displayed. Verification passed.");
            close.click();
        }
        driver.close();
    }
}
