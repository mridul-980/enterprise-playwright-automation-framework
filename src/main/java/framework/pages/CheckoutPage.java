package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class CheckoutPage {

    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator postalCodeInput;
    private final Locator continueButton;
    private final Locator finishButton;
    private final Locator backHomeButton;
    private final Locator checkoutTitle;
    private final Locator validationError;
    private final Locator overviewProductName;
    private final Locator overviewQuantity;
    private final Locator overviewUnitPrice;
    private final Locator subtotal;
    private final Locator tax;
    private final Locator total;
    private final Locator confirmationMessage;

    public CheckoutPage(Page page) {
        this.firstNameInput = page.getByPlaceholder("First Name");
        this.lastNameInput = page.getByPlaceholder("Last Name");
        this.postalCodeInput = page.getByPlaceholder("Zip/Postal Code");
        this.continueButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue").setExact(true));
        this.finishButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Finish").setExact(true));
        this.backHomeButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Back Home").setExact(true));
        this.checkoutTitle = page.locator("[data-test='title']");
        this.validationError = page.locator("[data-test='error']");
        this.overviewProductName = page.locator("[data-test='inventory-item-name']");
        this.overviewQuantity = page.locator(".cart_quantity");
        this.overviewUnitPrice = page.locator("[data-test='inventory-item-price']");
        this.subtotal = page.locator("[data-test='subtotal-label']");
        this.tax = page.locator("[data-test='tax-label']");
        this.total = page.locator("[data-test='total-label']");
        this.confirmationMessage =
                page.getByText("Thank you for your order!", new Page.GetByTextOptions().setExact(true));
    }

    public Locator firstNameInput() {
        return firstNameInput;
    }

    public Locator lastNameInput() {
        return lastNameInput;
    }

    public Locator postalCodeInput() {
        return postalCodeInput;
    }

    public Locator checkoutTitle() {
        return checkoutTitle;
    }

    public Locator validationError() {
        return validationError;
    }

    public Locator overviewProductName() {
        return overviewProductName;
    }

    public Locator overviewQuantity() {
        return overviewQuantity;
    }

    public Locator overviewUnitPrice() {
        return overviewUnitPrice;
    }

    public Locator subtotal() {
        return subtotal;
    }

    public Locator tax() {
        return tax;
    }

    public Locator total() {
        return total;
    }

    public Locator confirmationMessage() {
        return confirmationMessage;
    }

    public Locator backHomeButton() {
        return backHomeButton;
    }

    public void enterInformation(String firstName, String lastName, String postalCode) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        postalCodeInput.fill(postalCode);
    }

    public void continueToOverview() {
        continueButton.click();
    }

    public void finishPurchase() {
        finishButton.click();
    }

    public void returnHome() {
        backHomeButton.click();
    }
}
