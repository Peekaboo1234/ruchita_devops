package redbus_nagp_automation.testclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterMethod;
import io.github.bonigarcia.wdm.WebDriverManager;
import redbus_nagp_automation.utility.WaitInterval;

/**
 * Base class for setting up and tearing down test environment.
 *
 * @author ruchitapathak
 */
public class BaseTest {
	private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

	public static WebDriver getDriver() {
		return driverThreadLocal.get();
	}

	static String dirPath = System.getProperty("user.dir");

	/**
	 * Sets up the WebDriver instance based on the specified browser.
	 *
	 * @param browser The browser to be used for testing.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void setUp() throws InterruptedException, IOException {
		String browser = readProperties("browser");
		System.out.println("Launching " + browser + " browser...");
		WebDriver driver = null;

		switch (browser.toLowerCase()) {
		case "chrome":
			ChromeOptions options = getChromeOptions(dirPath);
			driver = WebDriverManager.chromedriver().capabilities(options).create();
			configureImplicitWait(driver);
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addPreference("browser.download.dir", dirPath);
			firefoxOptions.addPreference("browser.link.open_newwindow", 3);
			firefoxOptions.addPreference("browser.download.folderList", 2);
			firefoxOptions.addPreference("browser.download.manager.alertOnEXEOpen", false);
			firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk",
					"application/x-msexcel, application/excel, application/x-excel, "
							+ "application/vnd.ms-excel, application/octet-stream, application/x-compress, "
							+ "application/msword, application/csv, text/csv, image/png, image/jpeg, "
							+ "application/pdf, text/html, text/plain");
			driver = new FirefoxDriver(firefoxOptions);
			break;
		case "ie":
			WebDriverManager.iedriver().setup();
			InternetExplorerOptions ieOptions = new InternetExplorerOptions();
			ieOptions.ignoreZoomSettings();
			ieOptions.introduceFlakinessByIgnoringSecurityDomains();
			ieOptions.requireWindowFocus();
			ieOptions.enablePersistentHovering();
			ieOptions.destructivelyEnsureCleanSession();
			driver = new InternetExplorerDriver(ieOptions);
			configureImplicitWait(driver);
			break;
		case "safari":
			SafariOptions safariOptions = new SafariOptions();
			driver = new SafariDriver(safariOptions);
			configureImplicitWait(driver);
			break;
		default:
			System.out.println("No driver for Local");
			break;
		}
		driver.manage().window().maximize();
		driverThreadLocal.set(driver);
		driver.get(readProperties("url"));
		Thread.sleep(2000);
	}

	/**
	 * Configures the implicit wait time for the WebDriver instance.
	 *
	 * @param driver The WebDriver instance.
	 */
	private static void configureImplicitWait(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WaitInterval.OneSecond.getSeconds()));
	}

	/**
	 * Configures the ChromeOptions for Chrome browser.
	 *
	 * @param downloadFilepath The filepath for downloading files.
	 * @return The configured ChromeOptions.
	 */
	private static ChromeOptions getChromeOptions(String downloadFilepath) {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);

		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", false);
		if (downloadFilepath != null && !downloadFilepath.isEmpty()) {
			chromePrefs.put("download.default_directory", downloadFilepath);
		}
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("disable-infobars");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--remote-allow-origins=*");
		return options;
	}

	/**
	 * Reads properties from the configuration file.
	 *
	 * @param property The name of the property to read.
	 * @return The value of the specified property.
	 * @throws IOException
	 */
	public static String readProperties(String property) throws IOException {
		Properties prop = new Properties();
		FileInputStream ip = new FileInputStream(dirPath + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "config.properties");
		prop.load(ip);
		property = prop.getProperty(property);
		return property;
	}

	/**
	 * Tears down the WebDriver instance after each test method execution.
	 */
	@AfterMethod
	public void tearDown() {
		WebDriver driver = getDriver();
		if (driver != null) {
			driver.quit();
		}
		driverThreadLocal.remove();
	}

	/**
	 * Captures a screenshot during test execution.
	 *
	 * @param testMethodName The name of the test method.
	 */
	public void takeScreenshot(String testMethodName) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		Date date = new Date();
		WebDriver driver = getDriver(); // Access the WebDriver instance using getDriver()
		if (driver != null) {
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(srcFile, new File(dirPath + File.separator + "screenshots" + File.separator
						+ testMethodName + "_" + formatter.format(date) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to capture screenshot: " + e.getMessage());
			}
		} else {
			System.out.println("Driver is null. Cannot take screenshot.");
		}
	}
}