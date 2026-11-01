package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class ProductDetailsPage {

    private final Locator productName;
    private final Locator productDescription;
    private final Locator productPrice;
    private final Locator addToCartButton;

    public ProductDetailsPage(Page page) {
        this.productName = page.locator(".inventory_details_name");
        this.productDescription = page.locator(".inventory_details_desc");
        this.productPrice = page.locator(".inventory_details_price");
        this.addToCartButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Add to cart")
                        .setExact(true));
    }

    public Locator productName() {
        return productName;
    }

    public Locator productDescription() {
        return productDescription;
    }

    public Locator productPrice() {
        return productPrice;
    }

    public Locator addToCartButton() {
        return addToCartButton;
    }
}
