package tests.base;

import framework.api.ApiTestContext;
import framework.config.ConfigReader;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class ApiBaseTest {

    private static final ThreadLocal<ApiTestContext> CURRENT_CONTEXT = new ThreadLocal<>();

    @BeforeMethod
    protected void setUpApi() {
        if (CURRENT_CONTEXT.get() != null) {
            throw new IllegalStateException(
                    "An ApiTestContext is already associated with this TestNG worker thread.");
        }

        ConfigReader configReader = new ConfigReader();
        String environmentName = configReader.environmentName();
        String apiBaseUrl = configReader.apiBaseUrl(environmentName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No API base URL configured for environment '" + environmentName
                                + "'. Set -DapiBaseUrl or API_BASE_URL "
                                + "(or an environment-specific override)."));
        ApiTestContext apiTestContext = ApiTestContext.create(apiBaseUrl);
        try {
            CURRENT_CONTEXT.set(apiTestContext);
        } catch (RuntimeException | Error setupFailure) {
            apiTestContext.close(setupFailure);
            throw setupFailure;
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDownApi(ITestResult testResult) {
        ApiTestContext apiTestContext = CURRENT_CONTEXT.get();
        try {
            if (apiTestContext != null) {
                apiTestContext.close(testResult.getThrowable());
            }
        } finally {
            CURRENT_CONTEXT.remove();
        }
    }

    protected final ApiTestContext getApiTestContext() {
        ApiTestContext apiTestContext = CURRENT_CONTEXT.get();
        if (apiTestContext == null) {
            throw new IllegalStateException(
                    "No ApiTestContext is associated with the current TestNG worker thread.");
        }
        return apiTestContext;
    }
}
