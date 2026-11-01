package tests.ui;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.pages.ProductDetailsPage;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.Map;
import java.util.regex.Pattern;

public class InventoryTest extends BaseTest {

    private static final String STANDARD_USERNAME = "standard_user";
    private static final String STANDARD_PASSWORD = "secret_sauce";
    private static final String BACKPACK = "Sauce Labs Backpack";

    @Test
    public void TC_005_inventoryPageAfterSuccessfulLogin() {
        Page page = openInventoryAfterLogin();
        InventoryPage inventoryPage = new InventoryPage(page);

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/inventory\\.html$"));
        PlaywrightAssertions.assertThat(
                page.getByText("Products", new Page.GetByTextOptions().setExact(true)))
                .isVisible();
        PlaywrightAssertions.assertThat(inventoryPage.inventoryItems()).hasCount(6);
    }

    @Test
    public void TC_006_productNamesAndPrices() {
        Page page = openInventoryAfterLogin();
        InventoryPage inventoryPage = new InventoryPage(page);
        Map<String, String> expectedProducts = Map.of(
                "Sauce Labs Backpack", "$29.99",
                "Sauce Labs Bike Light", "$9.99",
                "Sauce Labs Bolt T-Shirt", "$15.99",
                "Sauce Labs Fleece Jacket", "$49.99",
                "Sauce Labs Onesie", "$7.99",
                "Test.allTheThings() T-Shirt (Red)", "$15.99");

        for (Map.Entry<String, String> product : expectedProducts.entrySet()) {
            Locator productCard = inventoryPage.productCard(product.getKey());
            PlaywrightAssertions.assertThat(productCard).hasCount(1);
            PlaywrightAssertions.assertThat(
                    productCard.locator("[data-test='inventory-item-name']"))
                    .hasText(product.getKey());
            PlaywrightAssertions.assertThat(
                    productCard.locator("[data-test='inventory-item-price']"))
                    .hasText(product.getValue());
        }
    }

    @Test
    public void TC_007_productDetails() {
        Page page = openInventoryAfterLogin();
        new InventoryPage(page).openProductDetails(BACKPACK);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(page);

        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/inventory-item\\.html\\?id=4$"));
        PlaywrightAssertions.assertThat(productDetailsPage.productName())
                .hasText(BACKPACK);
        PlaywrightAssertions.assertThat(productDetailsPage.productDescription())
                .hasText("carry.allTheThings() with the sleek, streamlined Sly Pack that melds "
                        + "uncompromising style with unequaled laptop and tablet protection.");
        PlaywrightAssertions.assertThat(productDetailsPage.productPrice())
                .hasText("$29.99");
        PlaywrightAssertions.assertThat(productDetailsPage.addToCartButton())
                .isVisible();
    }

    @Test
    public void TC_008_addProductToCartUpdatesBadge() {
        Page page = openInventoryAfterLogin();
        InventoryPage inventoryPage = new InventoryPage(page);

        inventoryPage.addProductToCart(BACKPACK);

        PlaywrightAssertions.assertThat(inventoryPage.shoppingCartBadge())
                .hasText("1");
    }

    @Test
    public void TC_009_removeProductFromCart() {
        Page page = openInventoryAfterLogin();
        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(page);
        Locator backpackCartItem = cartPage.cartItem(BACKPACK);
        PlaywrightAssertions.assertThat(backpackCartItem).hasCount(1);

        cartPage.removeProduct(BACKPACK);

        PlaywrightAssertions.assertThat(cartPage.cartItem(BACKPACK)).hasCount(0);
        PlaywrightAssertions.assertThat(cartPage.shoppingCartBadge()).hasCount(0);
    }

    private Page openInventoryAfterLogin() {
        Page page = getTestContext().page();
        page.navigate(getTestContext().configuration().baseUrl());

        new LoginPage(page).login(STANDARD_USERNAME, STANDARD_PASSWORD);
        PlaywrightAssertions.assertThat(page)
                .hasURL(Pattern.compile(".*/inventory\\.html$"));
        return page;
    }
}
