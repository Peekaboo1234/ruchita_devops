package redbus_nagp_automation.utility;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class providing commonly used methods for Selenium WebDriver interactions.
 */
public class Utility
{
	/**
     * Scrolls the page until the specified WebElement is found.
     *
     * @param driver The WebDriver instance used to interact with the browser.
     * @param ele    The WebElement to scroll to.
     */
	public void scrollToElement(WebDriver driver, WebElement ele) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollIntoView({ behavior: 'auto', block: 'center', inline: 'center' });", ele);
	}


	/**
     * Moves the mouse cursor to the specified WebElement to perform a hover action.
     *
     * @param driver The WebDriver instance used to interact with the browser.
     * @param ele    The WebElement to which the mouse cursor will be moved.
     */
	public static void hoverToElement(WebDriver driver, WebElement ele)
	{
		Actions actions = new Actions(driver);
		actions.moveToElement(ele).perform();
	}
	
	 /**
     * Switches to a newly opened window.
     *
     * @param driver The WebDriver instance used to interact with the browser.
     */
    public static void switchToNewWindow(WebDriver driver) {
        ArrayList<String> tab = new ArrayList<String>(driver.getWindowHandles());
        // switch to the user provided tab
        driver.switchTo().window(tab.get(1));
    }
    
    /**
     * Closes the current tab and switches back to the main window.
     *
     * @param driver            The WebDriver instance used to interact with the browser.
     * @param mainWindowHandle The handle of the main window.
     */
    public static void closeCurrentTab(WebDriver driver, String mainWindowHandle) {
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }
    
    /**
     * Checks if the element is displayed on the page.
     *
     * @param driver  The WebDriver instance used to interact with the browser.
     * @param element The WebElement to check for display.
     * @return True if the element is displayed, otherwise False.
     */
    public static boolean isElementDisplayed(WebDriver driver, WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Sends keys to the specified element and then clicks on it using the provided XPath type.
     *
     * @param driver    The WebDriver instance used to interact with the browser.
     * @param element   The WebElement to send keys and click.
     * @param value     The value to be sent to the element.
     * @param xpathType The type of XPath expression to use (e.g., "sc-gZMcBi" or "stn_name_code_wrap").
     */
    public static void sendKeysAndClick(WebDriver driver, WebElement element, String value, String xpathType) {
        element.sendKeys(value);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
        String xpathExpression = getXPathExpression(xpathType, value);
        WebElement chosenInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression)));
        chosenInput.click();
    }

    /**
     * Gets the XPath expression based on the type and value.
     *
     * @param xpathType The type of XPath expression to use (e.g., "sc-gZMcBi" or "stn_name_code_wrap").
     * @param value     The value to be used in the XPath expression.
     * @return The XPath expression.
     */
    private static String getXPathExpression(String xpathType, String value) {
        if (xpathType.equals("sc-gZMcBi")) {
            return String.format("//div[@class='sc-gZMcBi grvhsy']/text[text()='%s']", value);
        } else if (xpathType.equals("stn_name_code_wrap")) {
            return String.format("//div[@class='stn_name_code_wrap']/div[text()='%s']", value);
        } else {
            throw new IllegalArgumentException("Invalid XPath type: " + xpathType);
        }
    }


    /**
     * Waits for the given element to be clickable and then clicks on it.
     *
     * @param driver  The WebDriver instance used to interact with the browser.
     * @param element The WebElement to be clicked.
     */
	public void waitAndClick(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WaitInterval.TenSecond.getSeconds()));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            Thread.sleep(2000); // 2-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        element.click();
    }
	
 
	/**
	 * Recursively deletes all files and directories within the specified directory.
	 *
	 * @param dir The directory to be cleaned up.
	 */
	public static void cleanupDirectory(File dir)
	{
		for (File file : dir.listFiles())
		{
			if (file.isDirectory())
				cleanupDirectory(file);
			file.delete();
		}
	}
	
	/**
	 * Moves the data from a directory to another directory.
	 * 
	 * @param src         The source directory path.
	 * @param destination The destination directory path.
	 */
	public static void moveData(String src, String destination)
	{
		File source = new File(src);
		File dest = new File(destination);
		try
		{
			FileUtils.copyDirectory(source, dest);
			cleanupDirectory(source);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Searches for a file with the specified file name within the given directory.
	 * 
	 * @param dir      The directory path where the search will be performed.
	 * @param fileName The name of the file to search for.
	 * @return The name of the found file, or null if no file with the specified name is found.
	 */
	public String findFile(String dir, String fileName)
	{
		String name = null;
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles)
		{
			if (file.isFile())
			{
				if (file.getName().contains(fileName))
				{
					// name = file.getName().substring(0, file.getName().indexOf(".xlsx"));
					name = file.getName();
					break;
				}
			}
		}
		return name;
	}
}
