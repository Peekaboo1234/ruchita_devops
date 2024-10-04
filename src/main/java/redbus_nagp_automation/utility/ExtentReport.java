package redbus_nagp_automation.utility;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.configuration.Theme;

import redbus_nagp_automation.testclass.BaseTest;

/**
 * Utility class to manage Extent Reports for test execution.
 */
public class ExtentReport extends BaseTest {
    public static ExtentSparkReporter spark;
    public static ExtentReports extent;
    public static ExtentTest test;
    static String dirPath = System.getProperty("user.dir") + File.separator + "Current test results";

    /**
     * Method to set up and initialize the extent report.
     */
    @BeforeSuite
    public void setUpReport() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm");
        Date date = new Date();

        spark = new ExtentSparkReporter(
                dirPath + File.separator + "ExtentReport" + formatter.format(date) + ".html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Host Name", System.getProperty("hostName"));
        extent.setSystemInfo("Environment", System.getProperty("environment"));
        extent.setSystemInfo("User Name", System.getProperty("userName"));

        spark.config().setDocumentTitle("RedBus NAGP");
        spark.config().setReportName("RedBus NAGP");
        spark.config().setTheme(Theme.DARK);
    }

    /**
     * Method to capture the result of the test case and include it in the report.
     *
     * @param result The result of the test case.
     * @throws IOException If an error occurs while handling files.
     */
    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            Utility util = new Utility();
            String fileName = util.findFile(dirPath, result.getName());

            test.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
            test.log(Status.FAIL, "Test Caase Failed due to: " + test
                    .addScreenCaptureFromPath(dirPath + File.separator + fileName));
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS,
                    MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
        } else {
            test.log(Status.SKIP,
                    MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }
}
