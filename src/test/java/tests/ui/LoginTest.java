package tests.ui;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import framework.pages.LoginPage;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.regex.Pattern;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginNavigatesToInventory() {
        Page page = getTestContext().page();
        page.navigate(getTestContext().configuration().baseUrl());

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/inventory\\.html$"));
        PlaywrightAssertions.assertThat(
                page.getByText("Products", new Page.GetByTextOptions().setExact(true)))
                .isVisible();
    }

    @Test
    public void invalidLoginShowsErrorMessage() {
        Page page = getTestContext().page();
        page.navigate(getTestContext().configuration().baseUrl());

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("invalid_user", "secret_sauce");

        PlaywrightAssertions.assertThat(loginPage.errorMessage())
                .isVisible();
        PlaywrightAssertions.assertThat(loginPage.errorMessage())
                .hasText("Epic sadface: Username and password do not match any user in this service");
    }
}
