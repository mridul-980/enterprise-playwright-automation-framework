package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class CartPage {

    private final Page page;
    private final Locator cartItems;
    private final Locator shoppingCartBadge;
    private final Locator checkoutButton;

    public CartPage(Page page) {
        this.page = page;
        this.cartItems = page.locator("[data-test='inventory-item']");
        this.shoppingCartBadge = page.locator("[data-test='shopping-cart-badge']");
        this.checkoutButton = page.locator("[data-test='checkout']");
    }

    public Locator cartItems() {
        return cartItems;
    }

    public Locator cartItem(String exactProductName) {
        return cartItems.filter(new Locator.FilterOptions()
                .setHas(page.getByText(
                        exactProductName,
                        new Page.GetByTextOptions().setExact(true))));
    }

    public Locator shoppingCartBadge() {
        return shoppingCartBadge;
    }

    public void openCheckout() {
        checkoutButton.click();
    }

    public void removeProduct(String exactProductName) {
        cartItem(exactProductName)
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Remove")
                                .setExact(true))
                .click();
    }
}
