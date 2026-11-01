package framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import framework.config.EnvironmentManager.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TestContext {

    private static final Logger LOGGER = LogManager.getLogger(TestContext.class);

    private final Configuration configuration;
    private final Thread ownerThread;
    private final PlaywrightManager playwrightManager = new PlaywrightManager();
    private final BrowserManager browserManager = new BrowserManager();
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private boolean closed;

    private TestContext(Configuration configuration) {
        this.configuration = configuration;
        this.ownerThread = Thread.currentThread();
    }

    public static TestContext create(Configuration configuration) {
        TestContext testContext = new TestContext(configuration);

        try {
            testContext.playwright = testContext.playwrightManager.createPlaywright();
            testContext.browser = testContext.browserManager.launchBrowser(
                    testContext.playwright,
                    configuration.browserName(),
                    configuration.headless());
            testContext.browserContext =
                    testContext.browserManager.createBrowserContext(testContext.browser);
            testContext.page =
                    testContext.browserManager.createPage(testContext.browserContext);
            return testContext;
        } catch (RuntimeException | Error setupFailure) {
            testContext.close(setupFailure);
            throw setupFailure;
        }
    }

    public Configuration configuration() {
        ensureOpen();
        return configuration;
    }

    public Playwright playwright() {
        ensureOpen();
        return playwright;
    }

    public Browser browser() {
        ensureOpen();
        return browser;
    }

    public BrowserContext browserContext() {
        ensureOpen();
        return browserContext;
    }

    public Page page() {
        ensureOpen();
        return page;
    }

    public void close(Throwable primaryFailure) {
        assertOwnerThread();
        if (closed) {
            return;
        }
        closed = true;
        page = null;

        Throwable cleanupFailure = null;
        if (browserContext != null) {
            cleanupFailure = closeResource(
                    "BrowserContext",
                    () -> browserManager.closeBrowserContext(browserContext),
                    cleanupFailure);
            browserContext = null;
        }
        if (browser != null) {
            cleanupFailure = closeResource(
                    "Browser", () -> browserManager.closeBrowser(browser), cleanupFailure);
            browser = null;
        }
        if (playwright != null) {
            cleanupFailure = closeResource(
                    "Playwright",
                    () -> playwrightManager.closePlaywright(playwright),
                    cleanupFailure);
            playwright = null;
        }

        if (cleanupFailure == null) {
            return;
        }
        if (primaryFailure != null) {
            if (primaryFailure != cleanupFailure) {
                primaryFailure.addSuppressed(cleanupFailure);
            }
            LOGGER.error("Resource cleanup failed; attached to the primary failure.", cleanupFailure);
            return;
        }
        rethrow(cleanupFailure);
    }

    private Throwable closeResource(
            String resourceName, Runnable closeOperation, Throwable previousFailure) {
        try {
            closeOperation.run();
        } catch (RuntimeException | Error failure) {
            LOGGER.error("Failed to close {}.", resourceName, failure);
            if (previousFailure == null) {
                return failure;
            }
            if (previousFailure != failure) {
                previousFailure.addSuppressed(failure);
            }
        }
        return previousFailure;
    }

    private void ensureOpen() {
        assertOwnerThread();
        if (closed) {
            throw new IllegalStateException("Test context has already been closed.");
        }
    }

    public void assertOwnerThread() {
        Thread currentThread = Thread.currentThread();
        if (currentThread != ownerThread) {
            throw new IllegalStateException(
                    "TestContext is owned by thread '" + ownerThread.getName()
                            + "' (id=" + ownerThread.getId() + "), but was accessed from thread '"
                            + currentThread.getName() + "' (id=" + currentThread.getId() + ").");
        }
    }

    private void rethrow(Throwable failure) {
        if (failure instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        throw (Error) failure;
    }
}
