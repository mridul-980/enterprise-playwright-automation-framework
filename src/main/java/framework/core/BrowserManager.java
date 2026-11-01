package framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public final class BrowserManager {

    public Browser launchBrowser(Playwright playwright, String browserName, boolean headless) {
        BrowserType.LaunchOptions launchOptions =
                new BrowserType.LaunchOptions().setHeadless(headless);

        return switch (browserName) {
            case "chromium" -> playwright.chromium().launch(launchOptions);
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit" -> playwright.webkit().launch(launchOptions);
            default -> throw new IllegalArgumentException(
                    "Invalid browser '" + browserName
                            + "'. Allowed values: chromium, firefox, webkit.");
        };
    }

    public BrowserContext createBrowserContext(Browser browser) {
        return browser.newContext();
    }

    public Page createPage(BrowserContext browserContext) {
        return browserContext.newPage();
    }

    public void closeBrowserContext(BrowserContext browserContext) {
        browserContext.close();
    }

    public void closeBrowser(Browser browser) {
        browser.close();
    }
}
