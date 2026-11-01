package framework.core;

import com.microsoft.playwright.Playwright;

public final class PlaywrightManager {

    public Playwright createPlaywright() {
        return Playwright.create();
    }

    public void closePlaywright(Playwright playwright) {
        playwright.close();
    }
}
