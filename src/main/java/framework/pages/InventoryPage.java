package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class InventoryPage {

    private static final String INVENTORY_ITEM_SELECTOR = "[data-test='inventory-item']";
    private static final String ITEM_NAME_SELECTOR = "[data-test='inventory-item-name']";
    private static final String ITEM_DESCRIPTION_SELECTOR = "[data-test='inventory-item-desc']";
    private static final String ITEM_PRICE_SELECTOR = "[data-test='inventory-item-price']";
    private static final String CART_BADGE_SELECTOR = "[data-test='shopping-cart-badge']";

    private final Page page;
    private final Locator inventoryItems;
    private final Locator shoppingCartBadge;
    private final Locator shoppingCartLink;

    public InventoryPage(Page page) {
        this.page = page;
        this.inventoryItems = page.locator(INVENTORY_ITEM_SELECTOR);
        this.shoppingCartBadge = page.locator(CART_BADGE_SELECTOR);
        this.shoppingCartLink = page.locator("[data-test='shopping-cart-link']");
    }

    public Locator inventoryItems() {
        return inventoryItems;
    }

    public Locator productCard(String exactProductName) {
        return inventoryItems.filter(new Locator.FilterOptions()
                .setHas(page.getByText(
                        exactProductName,
                        new Page.GetByTextOptions().setExact(true))));
    }

    public Locator productName(String exactProductName) {
        return productCard(exactProductName).locator(ITEM_NAME_SELECTOR);
    }

    public Locator productDescription(String exactProductName) {
        return productCard(exactProductName).locator(ITEM_DESCRIPTION_SELECTOR);
    }

    public Locator productPrice(String exactProductName) {
        return productCard(exactProductName).locator(ITEM_PRICE_SELECTOR);
    }

    public Locator shoppingCartBadge() {
        return shoppingCartBadge;
    }

    public void openProductDetails(String exactProductName) {
        productName(exactProductName).click();
    }

    public void addProductToCart(String exactProductName) {
        productCard(exactProductName)
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Add to cart")
                                .setExact(true))
                .click();
    }

    public void openCart() {
        shoppingCartLink.click();
    }
}
