package tests.base;

import com.microsoft.playwright.Page;
import framework.config.EnvironmentManager;
import framework.core.TestContext;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);
    private static final ThreadLocal<TestContext> CURRENT_CONTEXT = new ThreadLocal<>();

    @BeforeMethod
    protected void setUp() {
        if (CURRENT_CONTEXT.get() != null) {
            throw new IllegalStateException(
                    "A TestContext is already associated with this TestNG worker thread.");
        }

        EnvironmentManager.Configuration configuration =
                new EnvironmentManager().resolve();
        TestContext testContext = TestContext.create(configuration);
        try {
            CURRENT_CONTEXT.set(testContext);
        } catch (RuntimeException | Error setupFailure) {
            testContext.close(setupFailure);
            throw setupFailure;
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown(ITestResult testResult) {
        TestContext testContext = CURRENT_CONTEXT.get();
        try {
            if (testContext != null) {
                if (testResult.getStatus() == ITestResult.FAILURE) {
                    captureFailureScreenshot(testContext, testResult.getThrowable());
                }
                testContext.close(testResult.getThrowable());
            }
        } finally {
            CURRENT_CONTEXT.remove();
        }
    }

    private void captureFailureScreenshot(TestContext testContext, Throwable testFailure) {
        try {
            Page page = testContext.page();
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions());
            Allure.addAttachment(
                    "UI failure screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png");
        } catch (RuntimeException | Error diagnosticFailure) {
            if (testFailure != null && testFailure != diagnosticFailure) {
                testFailure.addSuppressed(diagnosticFailure);
            }
            LOGGER.warn(
                    "Unable to capture or attach UI failure screenshot ({}).",
                    diagnosticFailure.getClass().getSimpleName());
        }
    }

    protected final TestContext getTestContext() {
        TestContext testContext = CURRENT_CONTEXT.get();
        if (testContext == null) {
            throw new IllegalStateException(
                    "No TestContext is associated with the current TestNG worker thread.");
        }
        return testContext;
    }
}
