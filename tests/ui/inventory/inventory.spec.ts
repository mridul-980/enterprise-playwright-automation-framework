import { test, expect } from '@playwright/test';
import { LoginPage } from '../../../src/pages/LoginPage';
import { InventoryPage } from '../../../src/pages/InventoryPage';

test('TC_005 - Verify inventory page after successful login', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);

  // Step 1: Navigate to the application
  await loginPage.navigateTo('/');

  // Step 2: Login with valid credentials
  await loginPage.login('standard_user', 'secret_sauce');

  // Step 3: Verify inventory page URL
  await expect(page).toHaveURL(/inventory.html/);

  // Step 4: Verify Products heading
  await expect(inventoryPage.productsHeading).toBeVisible();

  // Step 5: Verify product listings are displayed
  await expect(inventoryPage.inventoryItems.first()).toBeVisible();

  // Step 6: Verify the expected number of products
  await expect(inventoryPage.inventoryItems).toHaveCount(6);
});

test('TC_006 - Verify product details on inventory page', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await expect(inventoryPage.backpackName).toBeVisible();
  await expect(inventoryPage.backpackPrice).toHaveText('$29.99');
  await expect(inventoryPage.backpackAddToCartButton).toBeVisible();
});

test('TC_007 - Add a product to cart and verify cart badge', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();

  // Verify the cart badge shows one item
  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');

  // Verify the button changes after adding the product
  await expect(
    page.getByRole('button', { name: 'Remove' })
  ).toBeVisible();
});

test('TC_008 - Remove a product from cart', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);

  await loginPage.navigateTo('/');
  await loginPage.login('standard_user', 'secret_sauce');

  await inventoryPage.addBackpackToCart();
  await expect(page.locator('.shopping_cart_badge')).toHaveText('1');

  await inventoryPage.removeBackpackFromCart();

  // The cart badge should disappear when the cart becomes empty
  await expect(page.locator('.shopping_cart_badge')).toHaveCount(0);

  // The button should return to Add to cart
  await expect(inventoryPage.backpackAddToCartButton).toBeVisible();
});