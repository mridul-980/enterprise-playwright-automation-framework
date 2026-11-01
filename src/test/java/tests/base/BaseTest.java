package tests.base;

import framework.config.EnvironmentManager;
import framework.core.TestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

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
                testContext.close(testResult.getThrowable());
            }
        } finally {
            CURRENT_CONTEXT.remove();
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
