package redbus_nagp_automation.pages;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

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

import redbus_nagp_automation.testclass.BaseTest;
import redbus_nagp_automation.utility.Utility;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Page class for managing account options.
 * This class provides methods to interact with various account options like cancel ticket, change travel date, etc.
 * 
 * @author ruchitapathak
 */
public class AccountOptionsPage {
    private static Logger log = LogManager.getLogger(AccountOptionsPage.class);

    @FindBy(how = How.XPATH, using = "//span[text()='Account']")
    WebElement account;

    @FindBy(how = How.ID, using = "cancel_ticket")
    WebElement cancelTicket;

    @FindBy(how = How.ID, using = "reschedule_ticket")
    WebElement changeTravelDate;

    @FindBy(how = How.ID, using = "ticket_details")
    WebElement showMyTicket;

    @FindBy(how = How.ID, using = "smsandemail_ticket")
    WebElement emailSMS;

    @FindBy(how = How.ID, using = "user_sign_in_sign_up")
    WebElement login;

    /**
     * Verifies the count of options in the account dropdown menu.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @throws IOException if an I/O error occurs.
     */
    public void verifyAccountDropdownOptionsCount(WebDriver driver) throws IOException {
        log.info("Navigate to the Help section");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
        // Locate the dropdown element
        account.click();
        WebElement dropdownMenu = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.header_dropdown_menu")));
        // Get all the options from the dropdown
        List<WebElement> options = dropdownMenu.findElements(By.cssSelector("li.header_dropdown_item"));
        // Verify the count of options
        int actualOptionsCount = options.size();
        Assert.assertEquals(actualOptionsCount, Integer.parseInt(BaseTest.readProperties("AccountDropdownCount")),
                "Dropdown options count does not match the expected count.");
        log.info("Dropdown options count verification passed. Total options: " + actualOptionsCount);
    }

    /**
     * Visits various options available in the account dropdown menu.
     * 
     * @param driver The WebDriver instance used to interact with the browser.
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public void visitOptions(WebDriver driver) throws IOException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
        String mainWindowHandle = driver.getWindowHandle();

        // Open the Cancellation tab
        cancelTicket.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Utility.switchToNewWindow(driver);
        wait.until(ExpectedConditions.urlContains("Cancellation"));
        Assert.assertTrue(driver.getCurrentUrl().contains("Cancellation"), "Navigation to Cancel Ticket page failed.");
        log.info("Landed on Cancel Ticket page successfully.");
        Utility.closeCurrentTab(driver, mainWindowHandle);

        // Switch back to the main window
        driver.switchTo().window(mainWindowHandle);

        // Open the Change Travel Date tab
        account.click();
        changeTravelDate.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // Assuming this also opens in a new tab
        Utility.switchToNewWindow(driver);
        wait.until(ExpectedConditions.urlContains("Reschedule"));
        Assert.assertTrue(driver.getCurrentUrl().contains("Reschedule"), "Navigation to Change Travel Date page failed.");
        log.info("Landed on Change Travel Date page successfully.");
        Utility.closeCurrentTab(driver, mainWindowHandle);

        // Switch back to the main window
        driver.switchTo().window(mainWindowHandle);

        // Open the Show My Ticket tab
        account.click();
        showMyTicket.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // Assuming this also opens in a new tab
        Utility.switchToNewWindow(driver);
        wait.until(ExpectedConditions.urlContains("PrintTicket"));
        Assert.assertTrue(driver.getCurrentUrl().contains("PrintTicket"), "Navigation to Show My Ticket page failed.");
        log.info("Landed on Show My Ticket page successfully.");
        Utility.closeCurrentTab(driver, mainWindowHandle);

        // Switch back to the main window
        driver.switchTo().window(mainWindowHandle);

        // Open the Email/SMS tab
        account.click();
        emailSMS.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // Assuming this also opens in a new tab
        Utility.switchToNewWindow(driver);
        wait.until(ExpectedConditions.urlContains("SmsAndEmailTicket"));
        Assert.assertTrue(driver.getCurrentUrl().contains("SmsAndEmailTicket"), "Navigation to Email/SMS page failed.");
        log.info("Landed on Email/SMS page successfully.");
        Utility.closeCurrentTab(driver, mainWindowHandle);

        // Switch back to the main window and perform additional actions
        driver.switchTo().window(mainWindowHandle);
        account.click();
        login.click();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String script = "$('.modalCloseSmall').eq($('.modalCloseSmall').length - 1).click();";
        jsExecutor.executeScript(script);
    }
}
