package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class LoginPage {

    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.usernameInput = page.getByPlaceholder("Username");
        this.passwordInput = page.getByPlaceholder("Password");
        this.loginButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login"));
        this.errorMessage = page.locator("[data-test='error']");
    }

    public void login(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
    }

    public Locator errorMessage() {
        return errorMessage;
    }
}
