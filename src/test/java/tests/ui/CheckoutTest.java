package tests.ui;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import framework.pages.CartPage;
import framework.pages.CheckoutPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.regex.Pattern;

public class CheckoutTest extends BaseTest {

    private static final String STANDARD_USERNAME = "standard_user";
    private static final String STANDARD_PASSWORD = "secret_sauce";
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String FIRST_NAME = "Mridul";
    private static final String LAST_NAME = "Tripathi";
    private static final String POSTAL_CODE = "226001";

    @Test
    public void TC_010_navigateFromCartToCheckout() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/checkout-step-one\\.html$"));
        PlaywrightAssertions.assertThat(checkoutPage.checkoutTitle())
                .hasText("Checkout: Your Information");
    }

    @Test
    public void TC_011_checkoutInformationFormIsVisible() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);

        PlaywrightAssertions.assertThat(checkoutPage.firstNameInput()).isVisible();
        PlaywrightAssertions.assertThat(checkoutPage.lastNameInput()).isVisible();
        PlaywrightAssertions.assertThat(checkoutPage.postalCodeInput()).isVisible();
        PlaywrightAssertions.assertThat(
                page.getByRole(
                        com.microsoft.playwright.options.AriaRole.BUTTON,
                        new Page.GetByRoleOptions()
                                .setName("Continue")
                                .setExact(true)))
                .isVisible();
    }

    @Test
    public void TC_012_submitValidCheckoutInformation() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.enterInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutPage.continueToOverview();

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/checkout-step-two\\.html$"));
        PlaywrightAssertions.assertThat(checkoutPage.checkoutTitle())
                .hasText("Checkout: Overview");
    }

    @Test
    public void TC_013_checkoutOverviewShowsProductAndTotals() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.enterInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutPage.continueToOverview();

        PlaywrightAssertions.assertThat(checkoutPage.overviewProductName())
                .hasText(BACKPACK);
        PlaywrightAssertions.assertThat(checkoutPage.overviewQuantity()).hasText("1");
        PlaywrightAssertions.assertThat(checkoutPage.overviewUnitPrice())
                .hasText("$29.99");
        PlaywrightAssertions.assertThat(checkoutPage.subtotal())
                .hasText("Item total: $29.99");
        PlaywrightAssertions.assertThat(checkoutPage.tax())
                .hasText("Tax: $2.40");
        PlaywrightAssertions.assertThat(checkoutPage.total())
                .hasText("Total: $32.39");
    }

    @Test
    public void TC_014_completePurchaseAndVerifyConfirmation() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.enterInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutPage.continueToOverview();
        checkoutPage.finishPurchase();

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/checkout-complete\\.html$"));
        PlaywrightAssertions.assertThat(checkoutPage.checkoutTitle())
                .hasText("Checkout: Complete!");
        PlaywrightAssertions.assertThat(checkoutPage.confirmationMessage())
                .isVisible();
        PlaywrightAssertions.assertThat(checkoutPage.backHomeButton())
                .isVisible();
    }

    @Test
    public void TC_015_checkoutRequiredFieldValidation() {
        Page page = prepareCart();
        new CartPage(page).openCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(page);

        checkoutPage.enterInformation("", "", "");
        checkoutPage.continueToOverview();
        PlaywrightAssertions.assertThat(checkoutPage.validationError())
                .hasText("Error: First Name is required");

        checkoutPage.enterInformation(FIRST_NAME, "", "");
        checkoutPage.continueToOverview();
        PlaywrightAssertions.assertThat(checkoutPage.validationError())
                .hasText("Error: Last Name is required");

        checkoutPage.enterInformation(FIRST_NAME, LAST_NAME, "");
        checkoutPage.continueToOverview();
        PlaywrightAssertions.assertThat(checkoutPage.validationError())
                .hasText("Error: Postal Code is required");
    }

    private Page prepareCart() {
        Page page = getTestContext().page();
        page.navigate(getTestContext().configuration().baseUrl());

        new LoginPage(page).login(STANDARD_USERNAME, STANDARD_PASSWORD);
        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/inventory\\.html$"));

        new InventoryPage(page).addProductToCart(BACKPACK);
        new InventoryPage(page).openCart();
        return page;
    }
}
