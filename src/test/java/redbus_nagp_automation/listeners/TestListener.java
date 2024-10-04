package redbus_nagp_automation.listeners;
 
import java.io.File;
import java.io.IOException;
 
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
 
import redbus_nagp_automation.testclass.BaseTest;
import redbus_nagp_automation.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
/**
* TestListener class implements the ITestListener interface to listen to test events
* such as test start, test failure, test completion, etc.
*/
public class TestListener extends BaseTest implements ITestListener {
 
    private static final Logger logger = LogManager.getLogger(TestListener.class);
 
    /**
     * Invoked before the test method is invoked.
     */
    public void onTestStart(ITestResult result) {
        try {
            setUp();
        } catch (InterruptedException | IOException e) {
            logger.error("Exception occurred during test initialization: " + e.getMessage());
        }
    }
 
    /**
     * Invoked when a test method fails.
     */
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getMethod().getMethodName());
        takeScreenshot(result.getMethod().getMethodName());
        // Check if the test method is eligible for retry
        RetryAnalyzer retryAnalyzer = new RetryAnalyzer();
        if (retryAnalyzer.retry(result)) {
            logger.info("Retrying test: " + result.getMethod().getMethodName());
            result.setStatus(ITestResult.SKIP);
        } else {
            logger.info("Maximum retry count reached for test: " + result.getMethod().getMethodName());
        }
    }
 
    /**
     * Invoked before running all the test methods belonging to the classes inside the <test> tag and calling all their Configuration methods.
     */
    public void onStart(ITestContext context) {
        String dirPath = System.getProperty("user.dir");
        Utility.moveData(dirPath + File.separator + "Current test results",
                dirPath + File.separator + "Archived test results");
        logger.info("Current test results folder has been cleaned.");
    }
 
    /**
     * Invoked after all the test methods belonging to the classes inside the <test> tag have run and all their Configuration methods have been called.
     */
    public void onFinish(ITestContext context) {
        logger.info("Test execution finished.");
    }
}